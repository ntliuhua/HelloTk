import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlin.system.measureTimeMillis

//Android开发者快速上手Kotlin https://blog.csdn.net/lyz_zyx/article/details/105576442
fun main() = run {
    //coroutlintest()
    //channeltest1()
    //pooducttest()
    //actortest()
    //brodcasttest()
    //selecttest()
    //sequencetest()
    //flowtest()
    //flowontest()
    //flowtryCatchTest()
    //flowCancelTest()
    //channelFlowtest()
    //flowBufferTest()
    //flowConflatetest()
    //flowcollectLatest()
    //flowziptest()
    //flowcombinetest()

    basetest()
}

fun basetest()=runBlocking{
    testvalue()
    //testClass()
    //testHightFun()
    //starCoroutine()
    //testCoroutine()
    //coroutinebase()
}
