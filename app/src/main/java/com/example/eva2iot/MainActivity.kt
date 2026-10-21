package com.example.eva2iot

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.eva2iot.data.DatabaseHelper
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        dbHelper = DatabaseHelper(this)


        val hashAdmin = aplicarHashSHA256("1234")
        dbHelper.registrarUsuarioSeguro("admin", hashAdmin)

        val etUsuario = findViewById<EditText>(R.id.etUsuario)
        val etPassword = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (usuario.isNotEmpty() && password.isNotEmpty()) {


                val hashedPassword = aplicarHashSHA256(password)


                if (dbHelper.validarCredenciales(usuario, hashedPassword)) {
                    Toast.makeText(this, "Acceso concedido", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, DashboardActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun aplicarHashSHA256(texto: String): String {
        val bytes = texto.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.joinToString("") { "%02x".format(it) }
    }
}