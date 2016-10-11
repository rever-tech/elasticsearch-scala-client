# ElaticSearch Scala Client
A Scala client wrap elasticsarch native client and provide add-on method for non-block function call.
We support 2 common futures [Scala Future](http://docs.scala-lang.org/overviews/core/futures.html) [![Build Status](https://travis-ci.org/rever-tech/elasticsearch-scala-client.svg?branch=scala_future)](https://travis-ci.org/rever-tech/elasticsearch-scala-client) [Twitter Future](https://twitter.github.io/finagle/guide/Futures.html) [![Build Status](https://travis-ci.org/rever-tech/elasticsearch-scala-client.svg?branch=twitter_future)](https://travis-ci.org/rever-tech/elasticsearch-scala-client)

# Maven 
 - Twitter Future
    
        <dependency>
            <groupId>rever.client4s</groupId>
            <artifactId>elasticsearch-scala-client-twitter_2.11</artifactId>
            <version>2.4.0-1</version>
        </dependency>
 - Scala Future
    
        <dependency>
            <groupId>rever.client4s</groupId>
            <artifactId>elasticsearch-scala-client_2.11</artifactId>
            <version>2.4.0-1</version>
        </dependency>
 
# [SBT, Gradle, Ivy]
 - [Twitter Future](http://central.rever.vn/artifactory/webapp/#/artifacts/browse/tree/General/libs-release-local/rever/client4s/elasticsearch-scala-client-twitter_2.11)
 - [Scala Future](http://central.rever.vn/artifactory/webapp/#/artifacts/browse/tree/General/libs-release-local/rever/client4s/elasticsearch-scala-client_2.11)

# How to use
    import rever.client4s.Elasticsearch._
    val resp = client.prepareIndex(indexName, indexType).setSource(user).asyncGet()
    asyncIndexResp onSuccess {
      resp: IndexResponse => {
        assert(resp.getId.equals(id))
      }
    } onFailure { _ => assert(false) }

Please refer more in Test package [Scala Future](https://github.com/rever-tech/elasticsearch-scala-client/blob/scala_future/src/test/scala-2.11/rever/client4s/ElasticsearchAsyncTest.scala) [Twitter Future](https://github.com/rever-tech/elasticsearch-scala-client/blob/twitter_future/src/test/scala-2.11/rever/client4s/ElasticsearchAsyncTest.scala)

### Todos

 - Auto implicits conversion java collection on Response to scala collection
 - Benchmarks with native java client (use blocking) & other scala client
 - Fell free to add your request
 
License
----

MIT
```diff
- At Rever,we believe software should like sex, it's better when it's free ^^ -
```
