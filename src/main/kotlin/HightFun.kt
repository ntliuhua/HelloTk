//Kotlin中的高阶函数其实就是跟高等数学中的高阶函数一个概念，就是函数套函数，即f(g(x))。
// 什么意思呢？其实很好理解，就是将函数本身看作一种类型，作为别的函数的参数传入或者作为返回值返回。我们在前面其实就已经接触过高阶函数，
// 因为：arg.forEach(::println)中，forEach本身就是一个高阶函数，它接收的参数是：action: (T) -> Unit。来通过自定义是如何实现：
// 该函数返回一个函数
fun hfa(): (y: Int, z: Int) -> Int {
    return { a: Int, b: Int -> a + b }
}
// 该函数需要传入第二个参数是一个函数
fun hfb(int: Int, function: (string:String) -> Unit) {
    function(int.toString())
}
//说明 ：
//a函数不接收参数，但返回了一个Lambda表达式: (y: Int, z: Int) -> Int。
//b函数接收一个Int和一个Lambda表达式：(string:String) -> Unit。
//函数的引用使用两个冒号::，如果是类对象方法，就是obj::fun()。

//内联函数 inline 当我们使用高阶函数时，传入或返回的Lambda表达式实际上它会被编译成匿名类，那么也意味着每调用都会创建一个新的对象，这会造成明显的性能开销。
// 所以我们在使用高阶函数时一般都会在前面使用关键字inline来进行修饰，这代码是一个内联函数，
// 也就是说编译器会在编译时把我们实现的真实代码替换到每一个函数调用中，而不是使用匿名类的方式。
// 当然如果存在特殊情况，需要不内联，也可以使用oninline关键字。如：
inline fun infa(noinline function1: () -> Unit, function2: () -> Unit) {
    function1()
    function2()
}

//let、run、with、also和apply这5个高阶函数作用起基本差不多，只是在使用上有一点点区别。它们都是作用域函数，
//当你需要去定义一个变量在一个特定的作用域范围内，这些函数的是一个不错的选择；而且它们另一个作用就是可以避免写一些判断null的操作。
class Persion(var name: String, var age: Int) {
    override fun toString(): String {
        return "{$name, $age}"
    }
}

//use自动关闭资源  use高阶函数内部做了很多异常的处理和最后自动close释放资源，所以我们在使用上不需要自己去实现异常捕获和手动close，
//直接在大括号里使用最后执行的代码逻辑就可以了，也不怕内存泄漏。使用如
//File("build.gradle").inputStream().reader().buffered().use {
//    println(it.readLines())
//}

// SAM全称是Single Abstract Method，意思是单一抽象方法。Java8中开始对Lambda和SAM转换支持。
// 在使用上为：一个参数类型为只有一个方法的接口的方法时，调用时可用Lambda表达式做转换作为参数。而Kotlin中的SAM转换其实跟Java中的使用差不多，
// 但是得加多一个限制条件，那就是只支持Java接口的Java方法。什么意思？其实就是Kotlin只对调用Java代码支持SAM转换，
// Kotlin调Kotlin接口方法时是不支持SAM转换的。这是因为语言设计者认为，Kotlin本来就原生支持函数类型，根本没有必要再进行单一接口方法的定义。看看看如何使用：
//// Java代码
//public interface OnClickListener {
//    void onClick();
//}
//// Kotlin调用代码
//var onClickListener1 = object : OnClickListener {       // 匿名类的调用方式
//    override fun onClick() {
//        println("Hello World!")
//    }
//}
//var onClickListener2 = OnClickListener {                // SAM转换调用方式
//    println("Hello World!")
//}

//上述代码，如果要将onClickListener1和onClickListener2两个变量都传递给一个Java方法，可以这样：
//// Java代码
//public class ListenerManager {
//    List<OnClickListener> listenerList = new ArrayList();
//
//    public void addListener(OnClickListener listener) {
//        listenerList.add(listener);
//    }
//
//    public void removeListener(OnClickListener listener) {
//        listenerList.remove(listener);
//    }
//}
//// Kotlin调用代码
//val listenerManager = ListenerManager()
//listenerManager.addListener(onClickListener1)
//listenerManager.addListener(onClickListener2)

//    注解是对类、函数、函数参数、属性等做附加信息说明。Kotlin中使用注解跟Java中使用注解的概念和思想是一样的。
//    Kotlin中要声明注解，要使用 annotation 关键字放在类的前面：

@Target(AnnotationTarget.PROPERTY)
@Retention(AnnotationRetention.RUNTIME)
annotation class API(val say: String)
// 使用
class ANNO(@API("Hello") var name: String)
//上述代码声明了一个叫“API”的注解类，它的构造方法中定义了一个叫“say”的属性，它的标注对象是属性、作用时机是运行时，并将它在Class A的构造方法中使用。
