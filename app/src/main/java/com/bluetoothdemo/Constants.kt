package com.bluetoothdemo

import android.os.ParcelUuid

class Constants {
    companion object {
        const val deviceName: String = "ryougi"
        val broadcastUuid: ParcelUuid = ParcelUuid.fromString("00001234-0000-1000-8000-00805F9B34FB")
        const val manufacture_id: Int = 0x1234
    }
}