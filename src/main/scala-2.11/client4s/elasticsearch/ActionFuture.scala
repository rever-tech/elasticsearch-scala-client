package client4s.elasticsearch

import org.elasticsearch.action.support.AbstractListenableActionFuture
import org.elasticsearch.action.{ActionRequest, ActionRequestBuilder, ActionResponse}
import org.elasticsearch.threadpool.ThreadPool

import scala.concurrent._

/**
 * Created by zkidkid on 10/6/16.
 */
object ElasticSearch {


  type Req[T <: ActionRequest[T]] = ActionRequest[T]
  type Resp = ActionResponse
  type ReqBuilder[I <: Req[I], J <: Resp, K <: ActionRequestBuilder[I, J, K]] = ActionRequestBuilder[I, J, K]

  object ZActionRequestBuilder {
    val threadPool = new ThreadPool("client")
  }

  implicit class ZActionRequestBuilder[I <: Req[I], J <: Resp, K <: ActionRequestBuilder[I, J, K]](arb: ActionRequestBuilder[I, J, K]) {

    private[this] val promise = Promise[J]()

    def asyncGet(): Future[J] = {


      //      val threadPool = arb.getClass.getField("threadPool").setAccessible(true).asInstanceOf[ThreadPool]
      val listener = new AbstractListenableActionFuture[J, J](ZActionRequestBuilder.threadPool) {
        override def onFailure(e: Throwable): Unit = promise.failure(e)

        override def onResponse(result: J): Unit = promise.success(result)

        override def convert(listenerResponse: J): J = listenerResponse
      }
      arb.execute(listener)
      promise.future


    }

  }

}
