import kotlin.properties.Delegates
import kotlin.reflect.KProperty

//  Kotlin的类要注意几点：使用关键字constructor来声明构造方法、成员变量默认必须在声明时就初始化或者在init块进行初始化，且可见性默认是publish。
class A1 {
    var mA: Int = 0          // 成员变量必须初始化
    var mB: Int = 0
    constructor(a: Int) {                   // 构造方法1(副构造方法)
        mA = a
    }
    constructor(a: Int, b:Int):this(a) {    // 构造方法2(副构造方法)
        mB = b
    }
    fun f() {
        println(mA)
    }
}
class A2(a: Int, b:Int) {          // 如果只有一个构造函数时可这样写，这叫做主构造方法，推荐类要有一个主构造方法
    var mA: Int = a
    var mB: Int = b
    fun f() {
        println(mA)
        println(mB)
    }
}
class A3(val a: Int, b:Int) {      // 构造函数的参数也是类全局变量
    var mB: Int
    init {                          // init可做一些初始化工作
        mB = b
    }
    fun f() {
        println(a)
    }
}
//属性 只能写一个属性
class A4 {
    var a = 0
        get() {
            println("get a")
            return field
        }
        set(value) {
            println("set b")
            field = value
        }
}

//延迟初始化属性  如果实在需要延迟初始化成员变量的话，当然也是有办法的。
// 变量初始化，使用关键字 lateinit表示延时初始化，如果是常量的话，要使用 by lazy表达式来处理，表示使用时才调用到：
class A5 {
    var a = 0                   // 变量立即初始化
    var b:String? = null
    lateinit var c:String       // 变量延迟初始化
    val d:Int by lazy {         // 只读变量的延迟初始化，只要第一次访问时就会执行大括号里的初始化代码
        println("init d")
        100
    }
//    var e:Int by lazy {         // 换成var变量的话，这句是报错的
//        println("init e")
//        100
//    }
}

//属性代理  上面介绍延心初始化只读变量时，使用的就是属性代码lazy，它只能用于只读变量的情况。
// 如果是变量的话，对应也有一个叫observable的属性代理。当然你也可以通过自定义类的方式来实现，代理类需要实现相应的setValue和getValue方法。如：
class A6 {
    val a by lazy { 100 }          // 语法已提供，也就是上述的延初始化只读变量
    var b by Delegates.observable(0) {
            property, oldValue, newValue -> println("$property,$oldValue,$newValue")
    }
    val c by B()                   // 自定义代理
    var d by B()                   // 有了setValue方法后，var声明的变量也可用于代码
}

class B {
    var value: String? = null
    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
        println("A6-c-GetValue  ${this.value}")
        return this.value ?: "abc"
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
        println("A6-d-GetValue  ${value}")
        this.value = value
    }
}

//在Java中可见性分为：publish、protected、default（包内可见）以及private。
//而在Kotlin中，可见性分为：publish（默认）、internal（模块内可见）、protected、以及private。

//接口继承 Kotlin中的继承关系跟Java中一样，也是单继承多实现原则，它的语法跟C++很象。如：
open class IA {                      // 可被继承的类，要使用open声明
    fun a() {
        println("this is A.a")
    }
}
interface IB {                       // 接口的声明
    fun b1()
    fun b2() {                      // 接口方法可以默认实现
        println("this is B.b2")
    }
}
abstract class IC {                  // 抽象类的声明
    abstract fun c1()               // 抽象方法，必须要子类实现
    open fun c2() {                 // open方法，可由子类重写或不重写
        println("this is C.c2")
    }
    fun c3() {                      // 子类不能实现
        println("this is C.c3")
    }
}

class Test1 : IA(), IB {              // 子类继承使用豆号+父类的构造方法
    override fun b1() {
        println("this is Test1.b1")
    }
    override fun b2() {           // b2方法选择性实现
        super.b2()
        println("this is Test1.b2")
    }
}

class Test2 : IC() {
    override fun c1() {             // 实现父类或接口的方法，要使用overried
        super.c3()
        println("test2.ovaerride.c1")
    }
    override fun c2() {
        super.c2()
    }
//    override fun c3() {          // 这句会报错，因为c3不能被重写
//    }
}

//判断继承关系
//Test1 is A
//Test1 is B
//继承中的构造函数参数
//open class G1(a:Int) {                     // 构造函数参数不加var，表示a只是构造函数中的一个参数
//}
//class B1(a:Int) : G1(a) {
//}
//open class G2(var a:Int) {                 // 构造函数参数加var，表示类中的方法可以访问这个属性
//}
//class B2(a:Int) : G2(a) {
//}
//open class G3(open var a:Int) {            // 构造函数参数加open var，表示子类可对该属性重写
//}
//class B3(override var a:Int) : G3(a) {
//}

//使用by关键字进行接口代理。如：
//interface AI {
//    fun a()
//}
//interface BI {
//    fun b()
//}
//class Testi1 : AI, BI {
//    override fun a() {
//    }
//    override fun b() {
//    }
//}
//class Testi2(a: AI, b: BI) : AI by a, BI by b {     // 使用by关键字代理实现接口
//}

//方法名冲突，使用super<A>来指定父类
//interface A {
//    fun a(){
//        println("A.a")
//    }
//}
//interface B {
//    fun a(){
//        println("B.a")
//    }
//}
//class Test1 : A, B {
//    override fun a() {                      // 接口方法冲突，可以使有super<父类>来指定使用哪个父类的方法
//        super<A>.a()
//    }
//}

//在Kotlin中单例模式最基本的实现就是使用object。如：
object OA {
    fun a() {
        println("OA.a")
    }
}

//数据类，使用data关键字声明类。如：
data class DA(val id:Int, val name:String)
//componentN方法的使用
class DB() {
    operator fun component1():Int {
        return 1
    }
    operator fun component2():String {
        return "子云心B"
    }
}

//Kotlin使用静态方法时，建议使用包级函数，就是直接在包下写函数，不需要在类里写方法。但是如果一定需要像Java一样在类里的静态方法，可以使用伴生对象。如：
class CA private constructor() {
    companion object {
        fun a(p:Int) {
            println(p)
        }

        var TAG = "A"
    }
}
class CB private constructor() {
    companion object {
        @JvmStatic
        fun b(p:Int) {
            println(p)
        }
        @JvmField
        var TAG = "B"
    }
}
//在Java的调用是这样，A和B的区别在于B的b方法上加了@JvmStatic，这样如果你的代码也需要运行在Java中便会比较方便地调用就可以这样做
//A.Companion.a(2);
//A.Companion.getTAG();
//B.b(3);
//String tag = B.TAG;

//Kotlin中的内部类就是等于Java中的静态内部类。我们在Android开发中往往在使用非静态内部类中产生内存泄露情况，所以一般情况下都是使用静态内部类来实现相关的逻辑。
// 而Kotlin考虑到此情况，所以默认就是静态内部类了。如果你一定需要使用非静态内部的话，需要使用inner关键字来声明内部类。如：
class OutA {
    class InA {         // 静态内部类
    }
}
class OutB {
    val b: Int = 0
    inner class InB {   // 非静态内部类
        val b: Int = 1
        fun fun1() {
            println(b)              // 这里引用的是内部类的b
            println(this@OutB.b)    // 这里引用的是外部类的b
        }
    }
}
//在内部类中如果需要使用外部类的方法以，可以使用this@Outter。同理也存在this@Inner的用法。

//在Kotlin中使用匿名内部类有一处跟Java中不一样，但是匿名内部类也可以同时实现继承。如：
//open class Test
//interface OnClickListener {
//    fun onClick()
//}
//class View {
//    var onClickListener: OnClickListener? = null
//}
//
//// 调用
//val view = View()
//view.onClickListener = object : OnClickListener {         // 正常创建
//    override fun onClick() {
//    }
//}
//view.onClickListener = object : Test(), OnClickListener { // 创建时同时继承于Test
//    override fun onClick() {
//    }
//}

//Android官方中已明确强烈不推荐使用枚举，使用注解来代替枚举。因为枚举占用的内存空间要比整型大。通过反编你就知明白，
// 枚举最终会转化成一个继承于Enum的类，并且在类中存在着N个静态该类的字段以及一个该类的数组。
// 所以在手机开发中，它跟简单的几个Int类型对比起来这份性能开销是完全没有优势的
enum class LogLevel(val abbreviation:String) {
    VERBOSE("V"),
    DEBUG("D"),
    INFO("I"),
    WARN("W"),
    ERROR("E"),
    ASSERT("A");      // 注意这个分号，这可能是Kotlin目前唯一需要用到的分号
    fun getTag(): String {
        // ordinal是下标、abbreviation是构造方法传入的值、name是名称
        return "$ordinal, $abbreviation , $name"
    }
}

//sealed密封类 密封类是Java中没有的概念，密封类就是一种特殊的抽象类，其实它就是子类可数的类。
//在Kotlin1.1之前，密封类的子类必须定义为其内部类，而在1.1之后子类只需要与密封类在同一个文件中即可。使用如：
sealed class SA{
    class A1(val a:Int):SA()
    class A2:SA()
    object A3:SA()
}

//内联类是Kotlin在1.3中才出现。它是对另外一个类型的包装，它类似于Java装箱类型的一种类型，编译器会尽可能使用被包装的类型进行优化。使用如：
inline class INA(val value: Int) {
    operator fun inc():INA {                     // ++运算符重载
        return INA(value + 1)
    }
}

//官方所有无符号的类型都是使用内联类来实现的。
//因为枚举的性能问题，一般可以使用内联类+伴生对象来代替。如：
inline class LogLevelc(val ordinal: Int) {
    companion object {
        val VERBOSE = LogLevelc(0)
        val DEBUG = LogLevelc(1)
        val INFO = LogLevelc(2)
        val WARN = LogLevelc(3)
        val ERROR = LogLevelc(4)
        val ASSERT = LogLevelc(5)
    }
}
