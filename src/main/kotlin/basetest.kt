import java.util.regex.Pattern
import kotlin.reflect.KMutableProperty1

fun testCoroutine() {
//    log("Main函数开始")
//    coroutineDo(ParameterContext(true)) {								// 传入一个自定义的Context
//        val result = blockFun()
//        log("异步方法返结果：${result}")
//        result
//    }
//    log("Main函数结束")
    log("Main函数开始")
    coroutineDo2(ParameterContext(true)) {
        val result = blockFun2()
        log("异步方法返回结果：${result}")
        result
    }
    log("Main函数结束")
}

fun testHightFun(){
    //函数的引用使用两个冒号::，如果是类对象方法，就是obj::fun()。
    var aFun = hfa()                     // 返回一个函数
    var result = aFun(2, 3)           // 执行了返回的函数
    hfb(result, ::println)              // 输入println函数作为参数

    // let、run、with、also和apply 的调用
    var persion1 = Persion("子云心", 30)
    persion1.run {				// 直接能访问到类对象的let版本
        println("{${name}, ${age}}")        // 结果：{子云心, 15}
    }
    persion1.let {				// 返回表达式结果
        it.age = 15
        println("{${it.name}, ${it.age}}")  // 结果：{子云心, 15}
    }
    with(persion1) {			// 非扩展函数的run版本
        println("{${name}, ${age}}")        // 结果：{子云心, 15}
    }
    var persion2 = persion1.also {		// 同时返回新的对象的let版本
        it.age = 18
    }
    println(persion2)                       // 结果：{子云心, 18}
    var persion3 = persion1.apply {	// 同时返回新的对象的run版本
        age = 22
    }
    println(persion3)

//集合映射函数：filter、map、flatMap以及 asSequence 先来看看这三个函数的作用：
//filter：     保留满足条件的元素
//map:         集合中的所有元素一一映射到其他元素构成新集合
//flatMap:     集合中的所有元素一一映身到新集合并合并这些集合得到新集合
    val list1: List<Int> = listOf(1, 2, 3, 4)
    val list2 = list1.filter { it % 2 == 0 }
    println(list2)                          // 输出结果：[2,4]
    val list3 = list1.map { it * 2 }
    println(list3)                          // 输出结果：[2, 4, 6, 8]
    val list4 = list1.flatMap { 0 until it }
    println(list4)                          // 输出结果：[0, 0, 1, 0, 1, 2, 0, 1, 2, 3]

//asSequence：转换为懒序列 在集合后加上asSequence后，集合变成为懒序列，只有等到真正需要（调用forEach）时才会被执行，
//否则它就是一个公式不会被执行。下面来对比一下加了asSequence和没有加asSequence集合的输出结果：
    val list: List<Int> = listOf(1, 2, 3, 4)
    list.asSequence()
        .filter {
            print("filter:$it，")
            it % 2 == 0
        }.map {
            print("map:$it，")
            it * 2
        }.forEach {
            print("forEach:$it，")
        }
// 输出结果：filter:1，filter:2，map:2，forEach:4，filter:3，filter:4，map:4，forEach:8，
    list.filter {
        print("filter:$it，")
        it % 2 == 0
    }.map {
        print("map:$it，")
        it * 2
    }.forEach {
        print("forEach:$it，")
    }
// 输出结果：filter:1，filter:2，filter:3，filter:4，map:2，map:4，forEach:4，forEach:8，
//    sum、reduce和fold
//    先来看看这三个函数的作用：sum：所有元素求和  reduce:将元素依次按规则聚合，结果与元素类型一致  fold:给定初始化值版本的reduce
    val list11: List<Int> = listOf(1, 2, 3, 4)
    val list12 = list11.sum()
    println("")
    println(list12)              // 输出结果：10
    val list13 = list11.reduce { acc, i -> acc + i }
    println(list13)              // 输出结果：10
    val list14 = list11.fold("Hello") { acc, i -> acc + i }
    println(list14)              // 输出结果：Hello1234

//   上述代码声明了一个叫“API”的注解类，它的构造方法中定义了一个叫“say”的属性，它的标注对象是属性、作用时机是运行时，并将它在Class A的构造方法中使用。
//    val aObj = ANNO("子云心")
//    val kClass = ANNO::class
//    val properties = kClass.declaredMemberProperties.filter { it.annotations.isNotEmpty() }
//    properties.forEach {
//        var say = it.annotations.filterIsInstance<API>().first().say
//        val name = it as KMutableProperty1<ANNO?, String>
//        name.set(aObj, say + name.get(aObj))
//    }
//    println(aObj.name)      // 输出结果：Hello子云心

//  在Kotlin中完全可以照着Java中的正则表达式的写法使用Pattern类完成正则的匹配，
//  但其实Kotlin自身语法也存在一个Regex的类，使用它可以写出很Kotlin风格的代码。两套在Kotlin中是可以混着使用。如：
// Java版的正则
// Java版的正则
    val tsource = "Hello, This my phone number:0756-1234567"
    val tpattern = ".*(\\d{4}-\\d{7}).*"
    val matcher = Pattern.compile(tpattern).matcher(tsource)
    while (matcher.find()) {
        println(matcher.group())
        println(matcher.group(1))
    }

// Kotlin版的正则
    val source = "Hello, This my phone number:0756-1234567"
    val pattern = """.*(\d{4}-\d{7}).*"""
    Regex(pattern).findAll(source).toList().flatMap(MatchResult::groupValues).forEach(::println)

}

fun testClass(){
    var a = A1(3,5)
    a.f()
    println(a.mB)
    var b=A4()
    b.a = 5
    println("${b.a}")
    val lazya = A5()
    var x = lazya.d
    println(x)
    lazya.c = "hello"
    println(lazya.c)
    var y = A6()
    y.b = 5
    println(y.c)
    y.d = "hello 999"
    var z= Test1()
    z.a()
    z.b2()
    var z2= Test2()
    z2.c1()

    OA.a()

    var da = DA(1, "子云心")
    val id1 = da.id
    val id2 = da.component1()
    val name1 = da.name
    val name2 = da.component2()
    val(id, name) = da
    println(name)

    var db = DB()
    val(idb, nameb) = db
    println(nameb)

    CA.a(2)
    println(CA.TAG)
    CB.b(3)
    println(CB.TAG)

    val inA = OutA.InA()
    val inB = OutB().InB()

    println(LogLevel.ERROR.getTag())
    println(LogLevel.valueOf("DEBUG"))          // 获得DEBUG的对象
    LogLevel.values().map(::println)            // 获得所有对象

    var ina = INA(5)
    ina++
    println(ina.value.toString())

    val lc =LogLevelc.ASSERT
    println("LogLevel is ${lc.ordinal}")
}

fun testvalue() {
//    var a = "World"
//    var b = "Hello ${a}"
//    println("hello b:${b}")

//    var a = """
//<html>
//    <body>
//        ...
//    </body>
//</html>
//""".trimMargin()
//    println(a)

//    var array1: Array<String> = arrayOf("Hello", "World")
//    var array2: Array<Char> = arrayOf('H','e','l','l','o')
//    var array3: Array<Int> = arrayOf(1,2,3)
//    var array4: CharArray = charArrayOf('H','e','l','l','o')
//    var array5: IntArray = intArrayOf(1,2,3)
//    println(array5.joinToString("-"))
//    var result1: Boolean = 'H' !in array2
//    var result2: Boolean = 1 in array3
//    var result3: Boolean = array3.contains(1)
//    println(result1)
//    println(array2[2])
//    for (i in array2.indices) {
//        println(i)
//    }
//    for ((i, e) in array2.withIndex()) {
//        println("${i} ${e} ")
//    }

//    var a = 0..10          // [0,100]
//    var b = 0 until 10     // [0,100)，不包括100
//    var c = 10 downTo 0    // 倒序
//    var d = c.reversed()	// 创建一个基于原来倒序的区间
//    var e = 'a'..'z'        // 也适用于字母
//    var f = 0..10 step 2    // 值是：0 2 4 6 8 10
////    判断是否存在于区间内也是使用contains方法或使用关键字in。如：
//    var result1: Boolean = 1 in a
//    var result2: Boolean = 'A' !in e
//    var result3: Boolean = e.contains('a')
//    for (i in c) {
//        println(i.toString())
//    }
//    e.forEach {
//        println(it)
//    }

////    Java中的集合有：List<T>、Map<K,V>和Set<T>，而在Kotlin中将它们细分为可变集合和不可变集合。
////    其中不可变集合依然是List<T>、Map<K,V>和Set<T>，而它们对应的可变集合是MutableList<T>、MutableMap<K,V>和MutableSet<T>。其使用如下：
//    val list1: List<Int> = listOf(1, 2, 3)                  // 不能添加和删除元素
//    val list2: MutableList<Int> = mutableListOf(1, 2, 3)    // 可以添加和删除元素
//    val list3 = ArrayList<Int>()                            // 可以添加和删除的空List
//    val map1: Map<Int, String> = mapOf(1 to "一", 2 to "二")  // to 理解为K-V
//    val map2: MutableMap<Int, String> = mutableMapOf(1 to "一", 2 to "二")
//    val set1: Set<Int> = setOf(1, 2, 3)
//    val set2: MutableSet<Int> = mutableSetOf(1, 2, 3)
////    添加和删除元素可以如果是List或Set的话，使用add和remove方法，又或者直接使用+=和-=运算符，如果是Map的话，使用push方法或可像数组的形式进行添加。如：
//    list2.add(4)
//    list2.remove(3)
//    list2 += 5
//    list2 -= 2
//
//    map2.put(3, "三")
//    map2[4] = "四"
//    val a = list2[0]
//    list2[0] = 9
//    map2[3] = "四"
//    list2.forEach {
//        println(it)
//    }
//    map2.forEach {
//        println(it.value)
//    }

////    上面在Map的创建中传入的就是pari对象，它通过to关键字连接key和value值。使用如：
//    val pair1 = 1 to "一"           // 创建
//    var pair2 = Pair(2, "二")       // 不同的创建方式
//    val first = pair1.first         // 获得第一个元素
//    val second = pair1.second       // 获得第二个元素
//    val(x, y) = pair1               // 解构

////    Triple 三个元素版本的Pair,有时候一个函数需要返回多个参数，那完全可以使用Pari或Triple来实现
//    val triple = Triple(1, "一", "壹")
//    val first = triple.first
//    val second = triple.second
//    val third = triple.third
//    val (x, y, z) = triple

////    在Kotlin中，任意类型都有可空和不可空两种情况，在默认情况下定义的变量是不能为空的，若需要为空的情况，需要在类型后面加一个问号”?”，
//// 所以在Kotlin开发中减少了很多空指针崩溃的恶梦。我们来看看可空和不可空的定义:
//    val notNull:String = null               // 代码报错，编译不通过
//    val nullable:String? = null             // 正确的代码
//    val length1:Int = notNull.length        // 正确的代码
//    val length2:Int = nullable.length       // 代码报错，因为可能为空，不能直接获取长度
//    val length3:Int = nullable!!.length     // 正确的代码，表示认定一定不为空
//    val length4:Int? = nullable?.length     // 正确的代码，表示若为空，则返回空
//    val length5:Int = nullable?.length?:0   // 正确的代码，表示若为空，则返回0
////    不可空类型是可空类型的子类
//    var x: String = "Hello"
//    var y: String? = "World"
//    x = y                       // 代码报错
//    y = x                       // 代码正确，因为String是String?的子类


//// Kotlin code 调用java 类
//    var javaClass = JavaClass()
//    var a: String = javaClass.getA()    // 发生异常
//   var b: String? = javaClass.getA()

////    在Kotlin中，类型的强转换可以使用as和as?关键字。如：
//    open class Parent()
//    class Son() : Parent()
//    class Boy()
//    val parent = Parent()
//    val son: Son = parent as Son    // 转换成功
//    val boy1:Boy = parent as Boy    // 转换失败，程序抛出异常
//    val boy2:Boy? = parent as? Boy  // 转换失改，返回null

////    if else还是一样的if else
//    var a:Int = 1
//    if (a == 1) {
//        b = "true"
//    } else {
//        b = "false"
//    }
////    表达式方式，像函数返回值一样（一行时大括号省略了），也就是Java中的三元运算符
//    b = if (a == 1) "true" else "false"
//    println(b)

////    使用when代替switch
//    val a = 0
//    when(a) {
//        0-> b = "e"
//        1-> b = "二"
//        else -> b ="未知"
//    }
//// 表达式，像函数返回值一样
//    println(b)
//    b = when(a) {
//        0-> "一1"
//        1-> "二2"
//        else -> "未知"
//    }
//    println(b)
//// 表达式，when后不跟括号和对象，条件直接写在每个分支中
//    var x: String? = "sdafda"
//    b = when {
//        x is String -> x.length.toString()
//        x.isNullOrEmpty() -> "为空"
//        else -> "未知"
//    }
//    println(b)

////    在循环处声明一个名称用@连接，在break处指定跳出位置。如：
//    var array1: Array<Char> = arrayOf('H', 'e', 'l', 'l', 'o')
//    var array2: Array<Char> = arrayOf('W', 'o', 'r', 'l', 'd')
//    Outter@for(i in array1) {
//        Inner1@for(j in array2) {
//            break@Outter
//        }
//    }

////    给任意类实现Iterator,实现Iterator，也就是说可支持循环，需要实现next 和hasNext。如：
//    class A(val iterator: Iterator<Int>) {
//        operator fun next(): Int {
//            return iterator.next()
//        }
//
//        operator fun hasNext(): Boolean {
//            return iterator.hasNext()
//        }
//    }
//
//    class AList() {
//        private val list = ArrayList<Int>()
//
//        fun add(int: Int) {
//            list.add(int)
//        }
//
//        fun remove(int: Int) {
//            list.remove(int)
//        }
//
//        operator fun iterator(): A {
//            return A(list.iterator())
//        }
//    }
//
//// 调用示例
//    val list = AList()
//    list.add(1)
//    list.add(3)
//    list.add(5)
//// for循环形式
//    for (i in list) {
//        println(i)
//    }
//// while循环形式
//    val i = list.iterator()
//    while (i.hasNext()) {
//        println(i.next())
//    }

////    Kotlin中异常捕获跟Java中的语法一样，这里提一下其表达式的用法。如：
    val result = try {
        1
    } catch (e:Exception) {
        e.printStackTrace()
        0
    }
    println(result)

//    函数、具名参数、变长参数、默认参数和Lambda表达式
//    我们先来说说方法和函数的区别，在Java中只有方法没有函数，而在C++中又一般叫函数不叫方法，这是为什么呢？其实很好理解，
//    类里的funcation一般叫方法，而函数可以放在类的外边。所以也有说，方法其实是函数的一种特殊类型。Kotlin中对函数的语法如：
//    fun 方法名(参数1:参数类型, 参数N:参数类型):返回类型 { 函数体}

    //    函数的引用，使用两个分号::。如：
    class A() {
        fun fun1(p1:String, p2:Long):Int {
            return 1
        }
    }
// 调用
    val a =A()
    val f:(String, Long)->Int = a::fun1
    val r = f("Hello", 50L)
    println(r)

    //    变长参数使用关键字vararg来表示，使用起来就是可以接收多个不确定数量的同类型参数或者传入一个数组，
//    如果传入的是一个数组，需要在其前面加个*，表示把数组展开成一个个元素，目前只支持变长参数的展开数组，并非C++的指针。如：
    fun add(vararg ints: Int) {
        ints.forEach {
            println(it)
        }
    }
// 调用
    add(1, 2, 3, 4, 5)
    var list: IntArray = intArrayOf(1, 2, 3)
    add(*list)

    //    默认参数的出现大大减少了使用方法重载的麻烦。如：
    fun add(int:Int, string:String = "Hello") {
        println(int)
        println(string)
    }
    add(100)
    add(100, "子云心")
//    因为Java中是不存在默认参数的，所以如果你写的Kotlin代码也想提供给Java开发者调用的话，最好可以加上@JvmOverloads注解。如：
//    class A () {
//        @JvmOverloads
//        fun a(p:Int = 0) {
//            println(p)
//        }
//    }
//    Java中的调用也可以这样
//    new A().a();

//    Lambda表达式其实就是匿名函数比较有表示力的一种写法，Lambad表达式返回值是大括号里的最后一行。写法如下：
//    {[参数列表] –> [函数体，最后一行是返回值]}
//    ()->Unit					// 无参，返回值为Unit
//    (Int)->Int					// 传入整型，返回一个整型
//    (String, (String)->String)->Boolean		// 传入字符串、Lambda表式式，返回Boolean

// 匿名函数
    val sum = fun(a: Int, b: Int): Int {
        return a + b
    }
// Lambda表达式
    val suml = { a: Int, b: Int -> a + b }
// 调用
    sum(2, 3)
    suml.invoke(2, 3)
    var vsum =suml(1000,200)
    println(vsum)

//    var array: IntArray = intArrayOf(1, 2, 3)
// for循环的形式
//    for (item in array) {
//        println(item)
//    }
// forEach循环形式
//    array.forEach({ it -> println(it) })
//如果一个函数最后一个参数是Lambda表达式，可以将它移到括号外
//    array.forEach() { println(it) }
// 如果小括号没有参数，可以省略
//    array.forEach { println(it) }
// 入参、返回值与形参一致的函数可以用函数引用方式作为实参传入
//    array.forEach(::println)

    // 如果在Lambda中执行return，是直接对外边的函数执行，因为Lambda是表过式，并非函数。如果一定要return表达式，可以这样写：
    fun AA() {
        var array: IntArray = intArrayOf(1, 2, 3)
        array.forEach ForEach@ {
            if (it == 2) return@ForEach
            print(it)
        }
        println("AA End")
    }
    fun BB() {
        var array: IntArray = intArrayOf(1, 2, 3)
        array.forEach {
            if (it == 2) return
            print(it)
        }
        println("BB End")
    }
// 调用
    AA()          // 输出：13 AA End
    BB()          // 输出：1

//    运算符其实本身就是一个函数，所以可对其进行重载，只需要在函数前使用operator关键字。
//    class OA(var a: String) {
//        operator fun plus(other:OA):OA {
//            var result = (a.toInt() + other.a.toInt()).toString()
//            return OA(result)
//        }
//
//        override fun toString(): String {
//            return a.toString()
//        }
//    }
//    调用
//    var a1 = OA("2")
//    var a2 = OA("3")
//    println(a1 + a2)

    //    中缀表达式，使用infix关键字进行声明，它的使用看起来像是自定义运算符似的。如：
    class Book{
        infix fun on(any:Any):Boolean{
            return false
        }
    }
    class Desk {
    }
// 调用
    var result1 = Book() on Desk()
    var result2 = Book().on(Desk())
    println(result2)

    //    在Java中是不支持扩展方法，我们往往需要写一个工具方法就是通过静态方法来实现，而在Kotlin的扩展方法的定义要加.。如：
    fun String.multiply(int:Int):String {           // 扩展方法
        var stringBuilder = StringBuffer()
        for (i in 0 until int) {
            stringBuilder.append(this)
        }
        return stringBuilder.toString()
    }
    operator fun String.times(int:Int):String {     // 扩展方法+运算符重载，times对应的运算符是*
        var stringBuilder = StringBuffer()
        for (i in 0 until int) {
            stringBuilder.append(this)
        }
        return stringBuilder.toString()
    }
// 调用
    println("abc".multiply(2))          // 输出结果：abcabc
    println("def" * 3)                  // 输出结果：abcabc

////   除了方法可扩展外，属性也是可以进行扩展的
//    val String.a:String                 // 扩展属性
//        get() = "abc"
//// 调用
//    println("123".a)                    // 输出结果：abc

}