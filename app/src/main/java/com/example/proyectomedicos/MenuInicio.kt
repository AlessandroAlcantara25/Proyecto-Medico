package com.example.proyectomedicos

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomedicos.databinding.MenuBinding

class MenuInicio : AppCompatActivity() {

    private lateinit var mBinding: MenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = MenuBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        setupBottomNav()

        val buttonPaciente = findViewById<ImageButton>(R.id.gestionpaciente)
        buttonPaciente.setOnClickListener {
            val intent = Intent(this, OpcionesMenu::class.java)
            startActivity(intent)
        }


        val buttonRecetas = findViewById<ImageButton>(R.id.gestionreceta)
        buttonRecetas.setOnClickListener {
            val intent = Intent(this, OpcionesMenuRecetas::class.java)
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


