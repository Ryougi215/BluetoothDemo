package com.bluetoothdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.bluetoothdemo.bluetooth.advertiser.BLEAdvertiser
import com.bluetoothdemo.bluetooth.scanner.BLEScanner

class MainActivity : AppCompatActivity() {
    private val scanner: BLEScanner = BLEScanner.instance
    private val advertiser: BLEAdvertiser = BLEAdvertiser.instance

    private var inputId: EditText? = null
    private var receivedId: EditText? = null
    private var bt_startScan: Button? = null
    private var bt_startAdvertise: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bindWidgets()
        initListeners()
    }

    override fun onDestroy() {
        advertiser.stopAdvertise()
        scanner.stopScan()
        super.onDestroy()
    }

    private fun bindWidgets() {
        inputId = findViewById(R.id.id_to_send)
        receivedId = findViewById(R.id.id_received)
        bt_startScan = findViewById(R.id.bt_start_scan)
        bt_startAdvertise = findViewById(R.id.bt_start_advertise)
    }

    private fun initListeners() {
        bt_startScan?.setOnClickListener { scanner.startScan() }
        bt_startAdvertise?.setOnClickListener {
            inputId?.text?.let {
                if (it.isNotEmpty()) {
                    advertiser.startAdvertise(it.toString().toInt())
                } else {
                    Toast.makeText(this, "输入合法的id", Toast.LENGTH_SHORT).show()
                }
            }
        }
        scanner.setOnDataChangedListener { id -> receivedId?.setText(id.toString()) }
    }
}