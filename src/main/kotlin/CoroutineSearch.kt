import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit
import kotlin.coroutines.*

fun starCoroutine() {
    log("Main函数开始")
    suspend {
        val result = suspendCoroutine<String> { continuation ->
            Thread {
                log("异步开始")
                Thread.sleep(2000)
                try {
                    continuation.resumeWith(Result.success("异步请求成功"))
                } catch (e: Exception) {
                    continuation.resumeWith(Result.failure(e))
                }
            }.start()
        }
        log("异步方法返回结果：${result}")
        result
    }.startCoroutine(object : Continuation<String> {
        override val context: CoroutineContext = EmptyCoroutineContext
        override fun resumeWith(result: Result<String>) {
            log("收到异步结果：${result}")
        }
    })
    log("Main函数结束")
}

fun coroutinebase() {
    log("Main函数开始")
    coroutineDo(ParameterContext(true)) {
        try {
            val result = blockDoBase {// coroutineContext: CoroutineContext ->	// 如果不使用this，便需要携带参数coroutineContext
                val isSuccess = this[ParameterContext]!!.isSuccess // 通过this获取外部传入的context元素
                blockFunBase(isSuccess)
            }
            log("异步方法返回结果：${result}")
            result
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    log("Main函数结束")
}


fun <T> coroutineDo(coroutineContext: CoroutineContext, block: suspend () -> T) {
    block.startCoroutine(object : Continuation<T> {
        override val context: CoroutineContext = EmptyCoroutineContext + coroutineContext        // 如果你需要多个CoroutineContext还可以使用加号进行添加
        override fun resumeWith(result: Result<T>) {
            log("收到异步结果：${result}")
        }
    })
}


suspend fun blockFun() = suspendCoroutine<String> { continuation ->
    Thread {
        val isSuccess = continuation.context[ParameterContext]!!.isSuccess			// 获取传入的context元素
        log("异步开始")
        Thread.sleep(2000)
        if (isSuccess) {
            continuation.resumeWith(Result.success("异步请求成功"))
        } else {
            continuation.resumeWith(Result.failure(Exception()))
        }
    }.start()
}

class ParameterContext(val isSuccess: Boolean) : AbstractCoroutineContextElement(Key) {	// 创建一个自定义的Context
    companion object Key : CoroutineContext.Key<ParameterContext>
}

fun log(msg: String) {
    println("【${Thread.currentThread().name}】$msg")
}

//拦截器
fun <T> coroutineDo2(coroutineContext: CoroutineContext, block: suspend () -> T) {
    block.startCoroutine(object : Continuation<T> {
        //        override val context: CoroutineContext = EmptyCoroutineContext + coroutineContext
        override val context: CoroutineContext = AsyncContext() + coroutineContext
        override fun resumeWith(result: Result<T>) {
            log("收到异步结果：${result}")
        }
    })
}

suspend fun blockFun2() = suspendCoroutine<String> { continuation ->
//    Thread {
    val isSuccess = continuation.context[ParameterContext]!!.isSuccess
    log("异步开始")
    Thread.sleep(2000)
    if (isSuccess) {
        continuation.resumeWith(Result.success("异步请求成功"))
    } else {
        continuation.resumeWith(Result.failure(Exception()))
    }
//    }.start()
}


val singleThreadExecutor = ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, LinkedBlockingQueue<Runnable>())

class AsyncContext() : AbstractCoroutineContextElement(ContinuationInterceptor), ContinuationInterceptor {
    override fun <T> interceptContinuation(continuation: Continuation<T>): Continuation<T> {
        return ThreadPoolContinuation(continuation)
    }
}

class ThreadPoolContinuation<T>(private val continuation: Continuation<T>) : Continuation<T> {
    override val context: CoroutineContext = continuation.context
    override fun resumeWith(result: Result<T>) {
        singleThreadExecutor.execute { continuation.resumeWith(result) }
    }
}

// 使用扩展函数的形式，使block带this对象，这里也可以在括号里加 coroutineContext: CoroutineContext 参数把Context带出去，如：block: (coroutineContext: CoroutineContext) -> T
suspend fun <T>blockDoBase(block: CoroutineContext.() -> T) = suspendCoroutine<T> { continuation ->
    try {
        continuation.resumeWith(Result.success(block(continuation.context)))
    } catch (e: Exception) {
        continuation.resumeWith(Result.failure(Exception()))
    }
}

fun blockFunBase(isSuccess: Boolean): String {
    log("异步开始")
    Thread.sleep(2000)
    if (isSuccess) {
        return "异步请求成功"
    } else {
        throw Exception()
    }
}


