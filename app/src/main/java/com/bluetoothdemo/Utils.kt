package com.bluetoothdemo

import java.nio.ByteBuffer

class Utils {
    companion object {
        fun transformFromByteArr(sourceData: ByteArray): Int {
            val buffer = ByteBuffer.wrap(sourceData)
            val intBuffer = buffer.asIntBuffer()
            val intArr = IntArray(intBuffer.limit())
            intBuffer.get(intArr)
            return intBuffer[0]
        }
    }
}