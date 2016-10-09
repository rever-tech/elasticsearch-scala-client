package rever.client4s

import com.twitter.util.{Future, Promise}
import org.elasticsearch.action.support.AbstractListenableActionFuture
import org.elasticsearch.action.{ActionRequest, ActionRequestBuilder, ActionResponse}
import org.elasticsearch.threadpool.ThreadPool

import scala.annotation.tailrec


/**
 * Created by zkidkid on 10/6/16.
 */
object Elasticsearch {


  type Req[T <: ActionRequest[T]] = ActionRequest[T]
  type Resp = ActionResponse

  implicit class ZActionRequestBuilder[I <: Req[I], J <: Resp, K <: ActionRequestBuilder[I, J, K]](arb: ActionRequestBuilder[I, J, K]) {

    private[this] val promise = Promise[J]()

    val internalThreadPool: ThreadPool = internalThreadPool(arb,arb.getClass)

    @tailrec
    private[this] def internalThreadPool(arb: ActionRequestBuilder[I,J,K],cls: Class[_]): ThreadPool = {
      if (cls.getSimpleName.equals("ActionRequestBuilder")) {
        val f = cls.getDeclaredField("threadPool")
        f.setAccessible(true)
        f.get(arb).asInstanceOf[ThreadPool]
      }
      else
        internalThreadPool(arb,cls.getSuperclass)
    }

    def asyncGet(): Future[J] = {
      val listener = new AbstractListenableActionFuture[J, J](internalThreadPool) {
        override def onFailure(e: Throwable): Unit = promise.raise(e)

        override def onResponse(result: J): Unit = promise.setValue(result)

        override def convert(listenerResponse: J): J = listenerResponse
      }
      arb.execute(listener)
      promise
    }
  }

}
