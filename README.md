# ElaticSearch Scala Client
A Scala client wrap elasticsarch native client and provide add-on method for non-block function call.
We support 2 common futures [Scala Future](http://docs.scala-lang.org/overviews/core/futures.html) [![Build Status](https://travis-ci.org/rever-tech/elasticsearch-scala-client.svg?branch=scala_future)](https://travis-ci.org/rever-tech/elasticsearch-scala-client) [Twitter Future](https://twitter.github.io/finagle/guide/Futures.html) [![Build Status](https://travis-ci.org/rever-tech/elasticsearch-scala-client.svg?branch=twitter_future)](https://travis-ci.org/rever-tech/elasticsearch-scala-client)

# Maven

- Our repository

        <repository>
            <id>rever-repo</id>
            <name>rever-repo</name>
            <url>http://central.rever.vn/artifactory/libs-release-local</url>
        </repository> 
 
- Twitter Future
    
        <dependency>
            <groupId>rever.client4s</groupId>
            <artifactId>elasticsearch-scala-client-twitter_2.11</artifactId>
            <version>2.4.0-2</version>
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
        assert(true)
      }
    } onFailure { _ => assert(false) }

Please refer more use case in Test package [Scala Future](https://github.com/rever-tech/elasticsearch-scala-client/blob/scala_future/src/test/scala-2.11/rever/client4s/ElasticsearchAsyncTest.scala) [Twitter Future](https://github.com/rever-tech/elasticsearch-scala-client/blob/twitter_future/src/test/scala-2.11/rever/client4s/ElasticsearchAsyncTest.scala)

# Compatible Tests

| Elasticsearch Version | Test Status |
|:---------------------:|:-----------:|
|    2.4.0              | [![2.4.0](https://img.shields.io/badge/2.4.0-passing-brightgreen.svg?style=flat)](#)|
|    2.3.0              | [![2.3.0](https://img.shields.io/badge/2.3.0-passing-brightgreen.svg?style=flat)](#)|
|    2.2.0              | [![2.2.0](https://img.shields.io/badge/2.2.0-passing-brightgreen.svg?style=flat)](#)|
|    2.1.0              | [![2.1.0](https://img.shields.io/badge/2.1.0-passing-brightgreen.svg?style=flat)](#)|
|    2.0.0              | [![2.0.0](https://img.shields.io/badge/2.0.0-passing-brightgreen.svg?style=flat)](#)|
|    1.x                | [![1.x](https://img.shields.io/badge/1.x-unknown-lightgrey.svg?style=flat)](#)|

### Todos

 - Auto implicits conversion java collection on Response to scala collection
 - Benchmarks with native java client (use blocking) & other scala client
 - Feel free to add your request
 
License
----

[MIT](https://opensource.org/licenses/MIT)
```diff
- At Rever,we believe software should like sex, it's better when it's free ^^ -
```
