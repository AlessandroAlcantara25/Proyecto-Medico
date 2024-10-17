package com.example.proyectomedicos

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.proyectomedicos.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding : ActivityMainBinding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        mBinding.tvLRegistrar.setOnClickListener {
            val intent = Intent(this, Registrarse::class.java)
            startActivity(intent)
        }

        mBinding.btnIngresar.setOnClickListener {
            val email = mBinding.etLEmail.text.toString().trim()
            val pass = mBinding.etLPassword.text.toString().trim()


            if (email.isNotEmpty() && pass.isNotEmpty()){

                firebaseAuth.signInWithEmailAndPassword(email , pass).addOnCompleteListener {
                    if (it.isSuccessful){
                        val intent = Intent(this, MenuInicio::class.java)
                        startActivity(intent)
                    }else{
                        Toast.makeText(this, "No tiene una cuenta registrada o la contraseña o email son incorrectos" , Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, "No se permiten campos vacios" , Toast.LENGTH_SHORT).show()
            }
        }


        val etPassword = findViewById<EditText>(R.id.etLPassword)
        val imageViewPassword = findViewById<ImageView>(R.id.ivContraseña)

        imageViewPassword.setOnClickListener {
            if (etPassword.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                // Cambiar a modo oculto
                etPassword.inputType =
                    InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                imageViewPassword.setImageResource(R.drawable.ic_closed_eye)
            } else {
                // Cambiar a modo visible
                etPassword.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                imageViewPassword.setImageResource(R.drawable.ic_open_eye)
            }

            etPassword.setSelection(etPassword.text.length)

        }
    }
}
