package com.example.eva2iot.network

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.io.OutputStream
import java.util.UUID

class BluetoothController(context: Context) {

    private var bluetoothAdapter: BluetoothAdapter? = null
    private var bluetoothSocket: BluetoothSocket? = null
    private var outputStream: OutputStream? = null


    private val UUID_SERIAL_PORT_SERVICE = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    init {
        val bluetoothManager = context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
    }


    @SuppressLint("MissingPermission")
    suspend fun conectarDispositivo(macAddress: String): Boolean {

        return withContext(Dispatchers.IO) {
            try {
                val dispositivo = bluetoothAdapter?.getRemoteDevice(macAddress)
                bluetoothSocket = dispositivo?.createRfcommSocketToServiceRecord(UUID_SERIAL_PORT_SERVICE)


                //bluetoothAdapter?.cancelDiscovery()

                bluetoothSocket?.connect()
                outputStream = bluetoothSocket?.outputStream
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
            }
        }
    }


    suspend fun enviarComando(comando: String) {
        withContext(Dispatchers.IO) {
            try {
                outputStream?.write(comando.toByteArray())
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun desconectar() {
        try {
            bluetoothSocket?.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}