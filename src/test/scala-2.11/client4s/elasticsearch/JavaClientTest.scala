package client4s.elasticsearch

import java.net.InetAddress
import java.util.concurrent.TimeUnit

import org.elasticsearch.client.transport.TransportClient
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.common.xcontent.XContentFactory
import org.elasticsearch.index.query.TermQueryBuilder
import org.scalatest.FunSuite

/**
 * Created by zkidkid on 10/6/16.
 */
class JavaClientTest extends FunSuite {
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
    val isAck = client.admin.indices().prepareCreate(indexName).execute().get(5, TimeUnit.SECONDS).isAcknowledged
    assert(isAck)
  }

  test("simple index/get/query sync should successful") {
    val id = "1"
    val user = XContentFactory.jsonBuilder()
      .startObject()
      .field("user", "elon")
      .field("age", Int.MaxValue)
      .endObject()
    val indexResp = client.prepareIndex(indexName, indexType, id).setSource(user).setRefresh(true).get()

    assert(indexResp.getId.equals(id))
    val getResp = client.prepareGet(indexName, indexType, id).get()
    assert(getResp.getSourceAsMap.get("user").equals("elon"))
    assert(getResp.getSourceAsMap.get("age").equals(Int.MaxValue))

    val query = new TermQueryBuilder("user", "elon")
    val searchResp = client.prepareSearch(indexName).setQuery(query).get()
    assert(searchResp.getHits.totalHits() == 1)
    assert(searchResp.getHits.getAt(0).getId == id)
    assert(searchResp.getHits.getAt(0).getSource.get("user").equals("elon"))
    assert(searchResp.getHits.getAt(0).getSource.get("age").equals(Int.MaxValue))

    val delResp = client.prepareDelete(indexName, indexType, id).setRefresh(true).get()
    assert(delResp.getId.equals(id))
    assert(client.prepareGet(indexName, indexType, id).get().isExists == false)
  }



  test("clear evn should successful") {
    val isAck = client.admin().indices().prepareDelete(indexName).execute().get().isAcknowledged
    assert(isAck)
    client.close()
  }


}
