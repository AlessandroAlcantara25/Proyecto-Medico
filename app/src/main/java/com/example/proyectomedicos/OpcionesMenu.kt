package com.example.proyectomedicos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomedicos.databinding.MenuBinding
import com.example.proyectomedicos.databinding.OpcionesPacienteBinding

class OpcionesMenu : AppCompatActivity() {

    private lateinit var mBinding: OpcionesPacienteBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = OpcionesPacienteBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupBottomNav()

        val buttonPaciente = findViewById<Button>(R.id.btpaciente)
        buttonPaciente.setOnClickListener {
            val intent = Intent(this, InicioPaciente::class.java)
            startActivity(intent)
        }

        val buttonBuscar = findViewById<Button>(R.id.btbuscar)
        buttonBuscar.setOnClickListener {
            val intent = Intent(this, ListadoPaciente::class.java)
            startActivity(intent)
        }

        val buttonEditar = findViewById<Button>(R.id.btnActualizar)
        buttonEditar.setOnClickListener {
            val intent = Intent(this, EditarPaciente::class.java)
            startActivity(intent)
        }
    }

    private fun setupBottomNav() {
        mBinding.bottomNav.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.action_home -> {
                    val intent = Intent(this, MenuInicio::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_regresar -> {
                    val intent = Intent(this, MenuInicio::class.java)
                    startActivity(intent)
                    true
                }
                R.id.action_cerrar -> {
                    // Cerrar sesión y volver a MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}