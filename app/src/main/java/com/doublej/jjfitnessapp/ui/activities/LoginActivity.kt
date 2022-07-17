package com.doublej.jjfitnessapp.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.doublej.jjfitnessapp.R
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle

class LoginActivity : AppCompatActivity() {

    //DECLARAÇÃO DAS VARIAVEIS
    private lateinit var loginEmailInput : TextInputEditText
    private lateinit var loginPasswordInput : TextInputEditText
    private lateinit var loginButton : CardView
    private lateinit var auth: FirebaseAuth
    private lateinit var loadingView : SpinKitView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        //INICIALIZAÇÃO DAS VARIAVEIS
        loginEmailInput = findViewById(R.id.emailLoginEditText)
        loginPasswordInput = findViewById(R.id.passwordLoginEditText)
        loginButton = findViewById(R.id.signInButton)
        auth = Firebase.auth
        loadingView = findViewById(R.id.spin_kitLogin)
        loginButton.setOnClickListener { signIn() }


    }


    //FUNÇÃO PARA REALIZAR O LOGIN POR MEIO DO MÉTODO "signInWithEmailAndPassword"
    fun signIn(){
        loadingView.alpha = 1f
        auth.signInWithEmailAndPassword(loginEmailInput.text.toString().trim(),loginPasswordInput.text.toString().trim())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    MotionToast.createColorToast(this,
                        "Login com sucesso",
                        "A autenticação foi bem sucedida!",
                        MotionToastStyle.SUCCESS,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this,R.font.montserrat))
                    var intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                    finish()

                } else {
                    loadingView.alpha = 0f
                    MotionToast.createToast(this,
                        "Login sem sucesso",
                        "Autenticação nao foi bem sucedida." + "\n" + "Volte a verificar os dados introduzidos.",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this,R.font.montserrat))
                }
            }
    }
}