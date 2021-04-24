package com.bluetoothdemo.bluetooth.scanner

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.*
import android.text.Editable
import android.util.Log
import com.bluetoothdemo.Constants
import com.bluetoothdemo.Utils


class BLEScanner {
    companion object {
        const val TAG: String = "BLEScanner"
        val instance: BLEScanner by lazy(
            mode = LazyThreadSafetyMode.SYNCHRONIZED,
            initializer = ::BLEScanner
        )
    }

    private var isScanning: Boolean = false
    private var callback: ScanCallback? = MyScanCallback()

    private val scanner: BluetoothLeScanner =
        BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner
    private val filters: ArrayList<ScanFilter> = ArrayList()
    private val filter: ScanFilter = ScanFilter.Builder()
        .setServiceUuid(Constants.broadcastUuid)
        .build()
    private val settings: ScanSettings = ScanSettings.Builder()
        .setReportDelay(0)
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    var listener: (Int) -> Unit = {}

    fun setOnDataChangedListener(e: (Int) -> Unit) {
        this.listener = e
    }

    init {
        filters.add(filter)
    }

    fun startScan() {
        if (!isScanning) {
            scanner.startScan(filters, settings, callback)
            isScanning = true
        }
    }

    fun stopScan() {
        if (isScanning) {
            isScanning = false
            scanner.stopScan(callback)
        }
    }

    inner class MyScanCallback : ScanCallback() {
        private val TAG = "BleScanCallback"

        private fun processScanResult(scanResult: ScanResult?) {
            val device: BluetoothDevice = scanResult?.device ?: return

            val record: ScanRecord? = scanResult.scanRecord

            record?.let {
                val dataStream: ByteArray =
                    it.manufacturerSpecificData.get(Constants.manufacture_id)
                val id = Utils.transformFromByteArr(dataStream)
                listener(id)
            }
        }

        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            processScanResult(result)
        }

        override fun onScanFailed(errorCode: Int) {
            super.onScanFailed(errorCode)

            val reason = when (errorCode) {
                SCAN_FAILED_ALREADY_STARTED -> "$errorCode - SCAN_FAILED_ALREADY_STARTED"
                SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> "$errorCode - SCAN_FAILED_APPLICATION_REGISTRATION_FAILED"
                SCAN_FAILED_FEATURE_UNSUPPORTED -> "$errorCode - SCAN_FAILED_FEATURE_UNSUPPORTED"
                SCAN_FAILED_INTERNAL_ERROR -> "$errorCode - SCAN_FAILED_INTERNAL_ERROR"
                else -> {
                    "$errorCode - UNDOCUMENTED"
                }
            }
            Log.e(TAG, "BT Scan failed: $reason")
        }
    }

}