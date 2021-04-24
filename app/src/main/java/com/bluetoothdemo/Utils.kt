package com.bluetoothdemo

class Utils {
    companion object {
        fun transformFromByteArr(sourceData: ByteArray): Int {
            var id = 0u
            for ((index, byte) in sourceData.withIndex()) {
                id += byte.toUByte().toUInt().shl(8 * index)
            }
            return id.toInt()
        }

        fun transformToByteArr(source: Int): ByteArray {
            val target = byteArrayOf(0, 0, 0, 0)
            var index = 0
            while ((index < 4)) {
                target[index] = (source.shr(8 * index) and 0xff).toByte()
                index++
            }
            return target
        }
    }

}