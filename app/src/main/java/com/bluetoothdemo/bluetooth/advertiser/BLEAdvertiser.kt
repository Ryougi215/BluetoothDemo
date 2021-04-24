package com.bluetoothdemo.bluetooth.advertiser

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.AdvertiseCallback
import android.bluetooth.le.AdvertiseData
import android.bluetooth.le.AdvertiseSettings
import android.bluetooth.le.BluetoothLeAdvertiser
import android.util.Log
import com.bluetoothdemo.Constants
import com.bluetoothdemo.Utils
import com.bluetoothdemo.bluetooth.scanner.BLEScanner

class BLEAdvertiser {
    companion object {
        const val TAG: String = "BLEAdvertiser"
        val instance: BLEAdvertiser by lazy(
            mode = LazyThreadSafetyMode.SYNCHRONIZED,
            initializer = ::BLEAdvertiser
        )
    }

    private val callback: AdvertiseCallback = MyAdvertiseCallback()

    private var isAdvertising: Boolean = false
    private val advertiser = BluetoothAdapter.getDefaultAdapter().bluetoothLeAdvertiser
    private val advertiseSettings = AdvertiseSettings.Builder()
        .setConnectable(true)
        .setAdvertiseMode(AdvertiseSettings.ADVERTISE_MODE_LOW_LATENCY)
        .setTxPowerLevel(AdvertiseSettings.ADVERTISE_TX_POWER_HIGH)
        .setTimeout(0)
        .build()


    fun startAdvertise(id: Int) {
        BluetoothAdapter.getDefaultAdapter().name = "ryougi"
        if (!isAdvertising) {
            val advertiseData = AdvertiseData.Builder()
                .addServiceUuid(Constants.broadcastUuid)
                .addManufacturerData(Constants.manufacture_id, Utils.transformToByteArr(id))
                .addServiceUuid(Constants.broadcastUuid)
                .build()

            advertiser.startAdvertising(advertiseSettings, advertiseData, callback)
            Log.i(
                TAG,
                "ble advertising started, id = $id, byteArr = ${Utils.transformToByteArr(id)}"
            )
        }
    }

    fun stopAdvertise() {
        if (isAdvertising) {
            advertiser.stopAdvertising(callback)
            Log.i(TAG, "ble advertising started")
        }
    }

    inner class MyAdvertiseCallback() : AdvertiseCallback() {
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