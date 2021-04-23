package com.bluetoothdemo.bluetooth.advertiser

import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseSettings
import android.util.Log
import com.bluetoothdemo.bluetooth.scanner.BLEScanner

class BLEAdvertiser {
    companion object {
        const val TAG: String = "BLEAdvertiser"
        val instance: BLEAdvertiser by lazy(
            mode = LazyThreadSafetyMode.SYNCHRONIZED,
            initializer = ::BLEAdvertiser
        )
    }

    private var isAdvertising: Boolean = false

    inner class MyAdvertiseCallback(data: ByteArray) : AdvertiseCallback() {
        override fun onStartSuccess(settingsInEffect: AdvertiseSettings?) {
            super.onStartSuccess(settingsInEffect)
            Log.i(TAG, "onStartSuccess: ${settingsInEffect.toString()}")
            isAdvertising = true
        }

        override fun onStartFailure(errorCode: Int) {
            super.onStartFailure(errorCode)

            val reason: String = when (errorCode) {
                ADVERTISE_FAILED_ALREADY_STARTED -> "$errorCode - ADVERTISE_FAILED_ALREADY_STARTED"
                ADVERTISE_FAILED_FEATURE_UNSUPPORTED -> "$errorCode - ADVERTISE_FAILED_FEATURE_UNSUPPORTED"
                ADVERTISE_FAILED_INTERNAL_ERROR -> "$errorCode - ADVERTISE_FAILED_INTERNAL_ERROR"
                ADVERTISE_FAILED_TOO_MANY_ADVERTISERS -> "$errorCode - ADVERTISE_FAILED_TOO_MANY_ADVERTISERS"
                ADVERTISE_FAILED_DATA_TOO_LARGE -> "$errorCode - ADVERTISE_FAILED_DATA_TOO_LARGE"
                else -> {
                    "$errorCode - UNDOCUMENTED"
                }
            }
            Log.e(TAG, "onStartFailure: $reason")

            isAdvertising = false
        }
    }
}