package com.example.freedatingclg

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.analytics.FirebaseAnalytics

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(2000)

        // Ejemplo de evento de google analytics
        val analytics : FirebaseAnalytics = FirebaseAnalytics.getInstance(this)
        val bundle = Bundle()
        bundle.putString("message","Ejemplo de evento - Integración de Firebase completo")
        analytics.logEvent("InitScreen", bundle)


        //startActivity(Intent(this,MainActivity::class.java))
        startActivity(Intent(this,LoginActivity::class.java))
    }
}