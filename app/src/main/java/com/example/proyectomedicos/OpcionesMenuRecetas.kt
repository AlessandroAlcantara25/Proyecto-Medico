package com.example.proyectomedicos

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomedicos.databinding.OpcionesMenuRecetasBinding
import com.example.proyectomedicos.databinding.OpcionesPacienteBinding

class OpcionesMenuRecetas : AppCompatActivity() {

    private lateinit var mBinding: OpcionesMenuRecetasBinding

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = OpcionesMenuRecetasBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupBottomNav()

        val buttonReceta = findViewById<Button>(R.id.btReceta)
        buttonReceta.setOnClickListener {
            val intent = Intent(this, AgregarMedicina::class.java)
            startActivity(intent)
        }

        val buttonBuscarReceta = findViewById<Button>(R.id.btbuscarReceta)
        buttonBuscarReceta.setOnClickListener {
            val intent = Intent(this, ListadoReceta::class.java)
            startActivity(intent)
        }

        val buttonEditar = findViewById<Button>(R.id.btnActualizarRece)
        buttonEditar.setOnClickListener {
            val intent = Intent(this, EditarReceta::class.java)
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
