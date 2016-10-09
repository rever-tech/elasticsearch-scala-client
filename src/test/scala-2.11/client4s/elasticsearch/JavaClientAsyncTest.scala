package client4s.elasticsearch

import java.net.InetAddress
import java.util.concurrent.TimeUnit

import client4s.elasticsearch.ESImplicits._
import com.twitter.util.{Await, Duration}
import org.elasticsearch.action.delete.DeleteResponse
import org.elasticsearch.action.get.GetResponse
import org.elasticsearch.action.index.IndexResponse
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.xcontent.XContentFactory
import org.elasticsearch.index.query.TermQueryBuilder
import org.scalatest.FunSuite

import scala.util.Success


/**
 * Created by zkidkid on 10/6/16.
 */
class JavaClientAsyncTest extends FunSuite {
  // Init TransportClient url: https://www.elastic.co/guide/en/elasticsearch/client/java-api/current/transport-client.html
  val clusterName = "elasticsearch"
  val indexName = "java-client"
  val indexType = "test"
  val host = "127.0.0.1"
  val port = 9300
  val settings = Settings.builder()
    .put("cluster.name", clusterName)
    .build()
  val client = TransportClient.builder()
    .settings(settings)
    .build()
    .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(host), port))

  println(s"Num Connected Node " + client.connectedNodes().size())


  test("prepare env should successful") {

    if (client.admin().indices().prepareExists(indexName).execute().get().isExists) {
      client.admin().indices().prepareDelete(indexName).execute().get()
      Thread.sleep(1000)
    }
    client.admin.indices().prepareCreate(indexName).execute().get(5, TimeUnit.SECONDS).isAcknowledged
    Thread.sleep(1000)
    assert(true == client.admin().indices().prepareExists(indexName).execute().get().isExists)
  }

  test("simple index/get/query async should successful") {
    val id = "1"
    val user = XContentFactory.jsonBuilder()
      .startObject()
      .field("user", "elon")
      .field("age", Int.MaxValue)
      .endObject()
    val asyncIndexResp = client.prepareIndex(indexName, indexType, id).setSource(user).setRefresh(true).asyncGet()

    asyncIndexResp.onSuccess {
      resp: IndexResponse => {
        println(resp)
        assert(resp.getId.equals(id))
      }
    }
    asyncIndexResp onFailure { _ => assert(false) }
    Await.result(asyncIndexResp, Duration.fromSeconds(5))


    val getResp = client.prepareGet(indexName, indexType, id).asyncGet()
    getResp onSuccess {
      getResp: GetResponse => {
        println("Get Response: " + getResp)
        assert(getResp.getSourceAsMap.get("user").equals("elon"))
        assert(getResp.getSourceAsMap.get("age").equals(Int.MaxValue))
      }
    }


    Await.result(getResp, Duration.fromSeconds(5))


    val query = new TermQueryBuilder("user", "elon")
    println(s"Client Query ${query.toString}")
    val asyncSearchResp = client.prepareSearch(indexName).setQuery(query).asyncGet()
    asyncSearchResp.onSuccess {
      searchResp: SearchResponse => {
        assert(searchResp.getHits.totalHits() == 1)
        assert(searchResp.getHits.getAt(0).getId == id)
        assert(searchResp.getHits.getAt(0).getSource.get("user").equals("elon"))
        assert(searchResp.getHits.getAt(0).getSource.get("age").equals(Int.MaxValue))
      }
    }
    asyncSearchResp.onFailure {
      e: Throwable => assert(false)
    }

    Await.result(asyncSearchResp, Duration.fromSeconds(5))


    val asyncDelResp = client.prepareDelete(indexName, indexType, id).setRefresh(true).asyncGet()
    asyncDelResp onSuccess {
      delResp:DeleteResponse =>{
        assert(delResp.getId.equals(id))
        assert(client.prepareGet(indexName, indexType, id).get().isExists == false)
      }
    }
    asyncDelResp onFailure{e:Throwable=>assert(false)}


    Await.result(asyncDelResp, Duration.fromSeconds(5))
  }



  test("clear evn should successful") {
    val isAck = client.admin().indices().prepareDelete(indexName).execute().get().isAcknowledged
    assert(isAck)
    client.close()
  }


}
