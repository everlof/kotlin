// WITH_RUNTIME

@JvmInline
value class Z1(val x: String)
@JvmInline
value class ZN(val z: Z1?)
@JvmInline
value class ZN2(val z: ZN)

fun zap(b: Boolean): ZN2? = if (b) null else ZN2(ZN(null))

fun eq(a: Any?, b: Any?) = a == b

fun box(): String {
    val x = zap(true)
    val y = zap(false)
    if (eq(x, y)) throw AssertionError()

    return "OK"
}