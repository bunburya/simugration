package eu.bunburya.simugration.model.config

import kotlin.math.floor

class ImprovedPerlinNoise2D {

    fun noise(_x: Double, _y: Double): Double {

        // Find unit square containing point
        val X = floor(_x).toInt() and 255
        val Y = floor(_y).toInt() and 255

        // Find relative x and y within unit square
        val x = _x - floor(_x)
        val y = _y - floor(_y)

        // Compute fades
        val u = fade(x)
        val v = fade(y)

        // Get gradient values
        val gradients = doubleArrayOf (
            grad(p[p[X] + Y],           x,      y),
            grad(p[p[X + 1] + Y],       x - 1,  y),
            grad(p[p[X] + Y + 1],       x,      y - 1),
            grad(p[p[X + 1] + Y + 1],   x - 1,  y - 1)
        )

        return lerp(
            v,
            lerp(u, gradients[0], gradients[1]),
            lerp(u, gradients[2], gradients[3])
        )

    }

    fun modifiedNoise(x: Double, y: Double, frequency: Int, amplitude: Double): Double {
        return noise(frequency * x, frequency * y) * amplitude
    }

    fun octaveNoise(x: Double, y: Double, octaves: Int, persistence: Double = 1.0): Double {
        // TODO:  Use persistence
        var noiseValue = 0.0
        var freq = 1
        var amp = 1.0
        for (i in 1..octaves) {
            noiseValue += modifiedNoise(x, y, freq, amp)
            freq *= 2
            amp /= 2
        }
        return noiseValue
    }

    fun grad(hash: Int, x: Double, y: Double): Double = when (hash and 3) {
        0 -> x + y
        1 -> -x + y
        2 -> x - y
        3 -> -x - y
        else -> 0.0
    }

    fun fade(t: Double): Double = t * t * t * (t * (t * 6 - 15) + 10)

    fun lerp(t: Double, a: Double, b: Double): Double = a + t * (b - a)

    fun normalize(value: Double, rawRange: ClosedFloatingPointRange<Double>,
                  normizedRange: ClosedFloatingPointRange<Double>) {
        TODO()
    }

    companion object {
        // This is the permutation table used in the reference implementation by Perlin himself at
        // https://mrl.nyu.edu/~perlin/noise/
        private val permutations = intArrayOf(
            151, 160, 137, 91, 90, 15, 131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37,
            240, 21, 10, 23, 190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57,
            177, 33, 88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166,
            77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244, 102, 143,
            54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196, 135, 130, 116, 188, 159,
            86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123, 5, 202, 38, 147, 118, 126, 255, 82,
            85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42, 223, 183, 170, 213, 119, 248, 152, 2, 44, 154,
            163, 70, 221, 153, 101, 155, 167, 43, 172, 9, 129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178,
            185, 112, 104, 218, 246, 97, 228, 251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145,
            235, 249, 14, 239, 107, 49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4,
            150, 254, 138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180
        )
        val p = permutations + permutations
    }

}