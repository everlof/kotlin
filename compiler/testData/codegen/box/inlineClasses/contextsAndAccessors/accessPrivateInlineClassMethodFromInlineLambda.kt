// WITH_RUNTIME

@JvmInline
value class R(private val r: Int) {
    fun test() = run { ok() }

    private fun ok() = "OK"
}

fun box() = R(0).test()