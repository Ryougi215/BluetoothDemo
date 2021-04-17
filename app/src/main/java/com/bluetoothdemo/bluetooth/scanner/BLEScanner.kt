package com.bluetoothdemo.bluetooth.scanner

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanFilter
import android.bluetooth.le.ScanSettings
import com.bluetoothdemo.Constants


class BLEScanner() {
    companion object {
        const val TAG: String = "BLEScanner"
        val instance: BLEScanner by lazy(
            mode = LazyThreadSafetyMode.SYNCHRONIZED,
            initializer = ::BLEScanner
        )
    }

    private var isScanning: Boolean = false
    private var callback: ScanCallback? = null

    private val scanner: BluetoothLeScanner =
        BluetoothAdapter.getDefaultAdapter().bluetoothLeScanner
    private val filters: ArrayList<ScanFilter> = ArrayList()
    private val filter: ScanFilter = ScanFilter.Builder()
        .setDeviceName(Constants.deviceName)
        .build()
    private val settings: ScanSettings = ScanSettings.Builder()
        .setReportDelay(0)
        .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
        .build()

    init {
        filters.add(filter)
    }

    fun startScan(scanCallback: ScanCallback) {
        scanCallback.also { this.callback = it }

        if (!isScanning) {
            scanner.startScan(filters, settings, scanCallback)
            isScanning = true
        }
    }

    fun stopScan() {
        if (isScanning) {
            isScanning = false
            scanner.stopScan(callback)
        }
    }

}