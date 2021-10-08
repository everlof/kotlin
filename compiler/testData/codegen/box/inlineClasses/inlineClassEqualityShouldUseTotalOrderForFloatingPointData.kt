// WITH_RUNTIME

@JvmInline
value class InlineFloat(val data: Float)

@JvmInline
value class InlineDouble(val data: Double)

fun box(): String {
    if (InlineFloat(0.0f) == InlineFloat(-0.0f)) throw AssertionError()
    if (InlineFloat(Float.NaN) != InlineFloat(Float.NaN)) throw AssertionError()

    if (InlineDouble(0.0) == InlineDouble(-0.0)) throw AssertionError()
    if (InlineDouble(Double.NaN) != InlineDouble(Double.NaN)) throw AssertionError()

    return "OK"
}
