package com.doublej.jjfitnessapp.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.res.ResourcesCompat
import com.doublej.jjfitnessapp.R
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class RegisterActivity : AppCompatActivity() {

    //DECLARAÇÃO DAS VARIAVEIS
    private lateinit var registerButton : CardView
    private lateinit var emailInput : TextInputEditText
    private lateinit var passwordInput : TextInputEditText
    private lateinit var usernameInput : TextInputEditText
    private lateinit var database : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var loadingView : SpinKitView
    private lateinit var loginlink : TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        //INICIALIZAÇÃO DAS VARIAVEIS
        registerButton = findViewById(R.id.registerButton)
        emailInput = findViewById(R.id.emailEditText)
        passwordInput = findViewById(R.id.passwordEditText)
        usernameInput = findViewById(R.id.nomeUtilizadorEditText)
        loadingView = findViewById(R.id.spin_kit)
        loginlink = findViewById(R.id.loginLink)
        auth = Firebase.auth
        registerButton.setOnClickListener { signUp() }
        loginlink.setOnClickListener {
            var intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()


        }
    }

    //FUNÇÃO PARA REALIZAR O REGISTO POR MEIO DO MÉTODO "createUserWithEmailAndPassword"
    private fun signUp() {

        loadingView.alpha = 1f
        val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
        val formatted = current.format(formatter)
        val email = (emailInput.text).toString().trim()
        val password = (passwordInput.text).toString().trim()
        val username = (usernameInput.text).toString()
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    var userId = auth.currentUser!!.uid

                    val user = hashMapOf(
                        "username" to username,
                        "age" to "20",
                        "height" to "null",
                        "weight" to "null",
                        "levelOfActivity" to "null",
                        "goal" to "null",
                        "Status" to "Offline"

                    )

                    database = FirebaseFirestore.getInstance()
                    database.collection("Users").document(userId)
                        .set(user).addOnCompleteListener { task ->
                            if(task.isSuccessful){
                                MotionToast.createColorToast(this,
                                    "Registo com sucesso",
                                    "A sua conta foi criada com sucesso!",
                                    MotionToastStyle.SUCCESS,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(this,R.font.montserrat))
                                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                                    val intent = Intent(this, IntroducaoActivity::class.java)
                                    intent.putExtra("username", username)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                            else{
                                MotionToast.createToast(this,
                                    "Registo sem sucesso",
                                    "Verifique a sua ligação à Internet",
                                    MotionToastStyle.ERROR,
                                    MotionToast.GRAVITY_BOTTOM,
                                    MotionToast.LONG_DURATION,
                                    ResourcesCompat.getFont(this,R.font.montserrat))
                            }
                        }
                }
                else{
                    loadingView.alpha = 0f
                    MotionToast.createColorToast(this,
                        "Registo sem sucesso",
                        "Houve um problema a criar a sua conta" + "\n" + "Verifique os campos mais uma vez.",
                        MotionToastStyle.ERROR,
                        MotionToast.GRAVITY_BOTTOM,
                        MotionToast.LONG_DURATION,
                        ResourcesCompat.getFont(this,R.font.montserrat))
                }
            }




    }
}