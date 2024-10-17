package com.example.proyectomedicos

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyectomedicos.databinding.RegistrarCuentaBinding
import com.google.firebase.auth.FirebaseAuth

class Registrarse : AppCompatActivity() {

    private lateinit var mBinding: RegistrarCuentaBinding

    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = RegistrarCuentaBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        mBinding.tvRIngresar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnRegistrar.setOnClickListener {
            val email = mBinding.etREmail.text.toString().trim()
            val pass = mBinding.etRPassword.text.toString().trim()
            val confirmPass = mBinding.etConfirmPassword.text.toString().trim()

            if (email.isNotEmpty() && pass.isNotEmpty() && confirmPass.isNotEmpty()) {
                if (pass == confirmPass) {
                    firebaseAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                    }
                } else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No se permiten campos vacíos", Toast.LENGTH_SHORT).show()
            }
        }

        val etPassword = findViewById<EditText>(R.id.etRPassword)
        val imageViewPassword = findViewById<ImageView>(R.id.ivContraseña1)
        val etConfirmPassword = findViewById<EditText>(R.id.etConfirmPassword)
        val imageViewConfirmPassword = findViewById<ImageView>(R.id.ivContraseña2)

        // Función para cambiar la visibilidad de la contraseña
        fun togglePasswordVisibility(editText: EditText, imageView: ImageView) {
            if (editText.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
                // Cambiar a modo oculto
                editText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                imageView.setImageResource(R.drawable.ic_closed_eye)
            } else {
                // Cambiar a modo visible
                editText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                imageView.setImageResource(R.drawable.ic_open_eye)
            }
            // Colocar el cursor al final del texto
            editText.setSelection(editText.text.length)
        }

        // Configurar el clic para cambiar la visibilidad de la contraseña principal
        imageViewPassword.setOnClickListener {
            togglePasswordVisibility(etPassword, imageViewPassword)
        }

        // Configurar el clic para cambiar la visibilidad de la confirmación de contraseña
        imageViewConfirmPassword.setOnClickListener {
            togglePasswordVisibility(etConfirmPassword, imageViewConfirmPassword)
        }
    }
}