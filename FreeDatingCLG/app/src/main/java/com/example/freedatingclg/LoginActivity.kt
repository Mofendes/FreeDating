package com.example.freedatingclg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.freedatingclg.databinding.ActivityLoginBinding
import com.example.freedatingclg.ui.bMiperfil.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.FirebaseDatabase
import java.util.Objects.toString
import kotlin.system.exitProcess


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.bResetPass.setOnClickListener {
            showAlertReset()
        }

        // Usuario ya creado
        binding.bLogin.setOnClickListener {

            if (binding.emailTextView.text.isNotEmpty() && binding.passwordTextView.text.isNotEmpty()){
                FirebaseAuth.getInstance()
                    .signInWithEmailAndPassword(binding.emailTextView.text.toString(),
                        binding.passwordTextView.text.toString())
                    .addOnCompleteListener {
                        if (it.isSuccessful){
                            nextScreen(binding.emailTextView.text.toString()) // Le paso el email del usuario cómo extra en el intent para arrancar nueva actividad
                        }else{
                            Toast.makeText(applicationContext, it.exception?.message,Toast.LENGTH_LONG).show()
                            //showAlertErrorLogin()
                        }
                    }
            }

        }

        // Usuario nuevo
        binding.bRegister.setOnClickListener {
            // Registro em la aplicación
            if (binding.emailTextView.text.isNotEmpty() && binding.passwordTextView.text.isNotEmpty()){

                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.emailTextView.text.toString(),
                        binding.passwordTextView.text.toString())
                        .addOnCompleteListener {
                            if (it.isSuccessful){
                                val correo = binding.emailTextView.text.toString()
                                // Generamos un INSERT en la colección de usuarios con el ID (correo electrónico)
                                insertNewUser(correo)

                                nextScreen(correo) // Le paso el email del usuario cómo extra en el intent para arrancar nueva actividad
                            }else{
                                //showAlertErrorRegister()
                                Toast.makeText(applicationContext, it.exception?.message,Toast.LENGTH_LONG).show()
                            }
                        }
            }
        }

    }

    private fun insertNewUser(correo: String) {

        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Usuarios")

        val user = User()
        user.correo = correo.replace('.','*',false) //  correo.replace('.','*',false)

        // DEBUG
        println(user.toString())

        myRef.child(user.correo).setValue(user).addOnCompleteListener{
            //println("2. Ha conseguido escribir")
            if (it.isSuccessful){
                //uploadFoto(correoo)
                Toast.makeText(applicationContext,"Successfully created new user",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(applicationContext,"Failed to INSERT new user",Toast.LENGTH_SHORT).show()
            }
        }

        //myRef.setValue("Hello, World! putamierda. El usuario es $correo")

    }

    /**
    private fun showAlertErrorRegister() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ERROR!")
        builder.setMessage("Error al registrar un nuevo usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showAlertErrorLogin() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("ERROR!")
        builder.setMessage("Error al autentificar el usuario")
        builder.setPositiveButton("Aceptar",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    */


    private fun showAlertReset() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Importante!")
        builder.setMessage("Este señor se lo creyó todo")
        builder.setPositiveButton("Sí, es cierto",null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun nextScreen(email: String) {
        // login successfully
        //startActivity(Intent(this,MainActivity::class.java))

        val homeIntent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            //putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
    }

    // Para que no haga backpressed a la SplashScreen Activity
    override fun onBackPressed() {
        moveTaskToBack(true);
        exitProcess(-1)
    }
}