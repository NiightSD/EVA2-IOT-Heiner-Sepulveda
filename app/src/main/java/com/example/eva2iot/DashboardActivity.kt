package com.example.eva2iot

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.eva2iot.network.BluetoothController
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    private lateinit var bluetoothController: BluetoothController


    private val MAC_ADDRESS_IOT = "00:00:00:00:00:00" // Dirección MAC del dispositivo IoT PARA VINCULAR

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)


        bluetoothController = BluetoothController(this)


        val btnEncender = findViewById<Button>(R.id.btnEncenderDispositivo)

        btnEncender.setOnClickListener {

            lifecycleScope.launch {
                Toast.makeText(this@DashboardActivity, "Intentando conectar...", Toast.LENGTH_SHORT).show()


                val conectado = bluetoothController.conectarDispositivo(MAC_ADDRESS_IOT)

                if (conectado) {
                    bluetoothController.enviarComando("1")
                    Toast.makeText(this@DashboardActivity, "¡Dato '1' enviado con éxito!", Toast.LENGTH_LONG).show()
                    bluetoothController.desconectar()
                } else {
                    Toast.makeText(this@DashboardActivity, "Error: El celular rechazó la conexión.", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}