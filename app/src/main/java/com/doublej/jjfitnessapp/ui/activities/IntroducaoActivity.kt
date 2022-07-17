package com.doublej.jjfitnessapp.ui.activities

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.databinding.FirstPageFragmentBinding
import com.doublej.jjfitnessapp.ui.IntroducaoFragments.FirstPageFragment
import com.doublej.jjfitnessapp.ui.IntroducaoFragments.SecondPageFragment
import com.doublej.jjfitnessapp.ui.IntroducaoFragments.ThirdPageFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

import android.view.View.OnTouchListener
import androidx.core.content.res.ResourcesCompat
import com.doublej.jjfitnessapp.ui.dashboard.ViewPagerDuration
import com.github.ybq.android.spinkit.SpinKitView
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle


class IntroducaoActivity : AppCompatActivity(), FirstPageFragment.onSomeEventListener, SecondPageFragment.onSomeEventListener, ThirdPageFragment.onSomeEventListener{

    //DECLARAÇAO DAS VARIAVEIS
    private var _bindingFirst: FirstPageFragmentBinding? = null
    private val binding get() = _bindingFirst!!
    private lateinit var adapter : MyViewPagerAdapter
    private lateinit var viewPager : ViewPager
    private lateinit var dotsIndicator : WormDotsIndicator
    private lateinit var db : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var firstNameText : String = ""
    private var lastNameText : String = ""
    private var goalValue : String = ""
    private var currentFitnessValue : String = ""
    private var genderValue : String = ""
    private var weightValue : Float = 0f
    private var heightValue : Int = 0
    private var dayValue : Int = 0
    private var monthValue : Int = 0
    private var yearValue : Int = 0
    private var username : String? = "Username"
    private lateinit var loadingView : SpinKitView
    private lateinit var viewPagerDuration : ViewPagerDuration






    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        setContentView(R.layout.activity_introducao)

        //OBTENÇÃO DADOS RECEBIDOS DA STARTACTIVITY
        val extras = intent.extras
        if (extras != null) {
            username = extras.getString("username")
            // and get whatever type user account id is
        }


        //CONFIGURAÇÃO DO VIEWPAGER
        adapter = MyViewPagerAdapter(supportFragmentManager)
        viewPager = findViewById(R.id.viewPager)
        adapter.addFragment(FirstPageFragment())
        adapter.addFragment(SecondPageFragment())
        adapter.addFragment(ThirdPageFragment())
        viewPager.adapter = adapter
        loadingView = findViewById(R.id.spin_kitIntroduction)
        dotsIndicator = findViewById(R.id.worm_dots_indicator)
        dotsIndicator.setViewPager(viewPager)
        viewPagerDuration = viewPager as ViewPagerDuration
        viewPagerDuration.setScrollDurationFactor(6f)
        viewPager.setOnTouchListener(OnTouchListener { v, event -> true })




        

        //REMOÇÃO DAS ACTIONS BARS (SISTEMA ANDROID)
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView) ?: return
        // Configure the behavior of the hidden system bars
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        // Hide both the status bar and the navigation bar
        windowInsetsController.hide(WindowInsetsCompat.Type.systemBars())



    }


        class MyViewPagerAdapter(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val fragmentList : MutableList<Fragment> = ArrayList()
        private val titleList : MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            Log.d("testposition", position.toString())

            return fragmentList[position]
        }

        override fun getCount(): Int {
            Log.d("ViewPagerTest", fragmentList.size.toString())
            return fragmentList.size
        }

        fun addFragment(fragment : Fragment){
            fragmentList.add(fragment)
        }
    }




    //FUNÇÃO PARA INSERIR OS DADOS INSERIDOS NO QUESTIONÁRIOS NA BASE DE DADOS
    fun insertDataDatabase(firstName : String, lastName : String, height : Int, weight : Float, gender : String, currentFitness : String, goal : String){
        loadingView.alpha = 1f
        val exerciseLikeArray : ArrayList<String> = arrayListOf()

        db = FirebaseFirestore.getInstance()
        auth = Firebase.auth
        var currentUser = auth.currentUser!!.uid
        val newData = hashMapOf(
            "firstname" to firstName,
            "lastname" to lastName,
            "height" to height,
            "weight" to weight,
            "goal" to goal,
            "dayOfBirth" to dayValue,
            "monthOfBirth" to monthValue,
            "yearOfBirth" to yearValue,
            "levelOfActivity" to currentFitness,
            "gender" to gender,
            "likedPlans" to exerciseLikeArray
        )
        db.collection("Users").document(currentUser).set(newData, SetOptions.merge()).addOnCompleteListener {
            MotionToast.createColorToast(this,
                "Dados adicionados!",
                "Os seus dados foram submetidos com sucesso",
                MotionToastStyle.INFO,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this,R.font.montserrat))
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)

        }
            .addOnFailureListener{
                MotionToast.createColorToast(this,
                    "Erro ao adicionar dados!",
                    "Verifique a sua ligação à Internet",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this,R.font.montserrat))
                loadingView.alpha = 0f
            }

    }

    //FUNÇÕES PARA OBTER OS DADOS INSERIDOS NAS 3 VIEWPAGERS POR MEIO DE INTERFACES

    override fun someEvent(firstName: String, lastName: String,day : Int, month: Int, year : Int){
        viewPager.setCurrentItem(viewPager.currentItem + 1, true);
        firstNameText = firstName
        lastNameText = lastName
        dayValue = day
        monthValue = month
        yearValue = year
    }
    override fun someEvent2(weight: String, height: String, gender : String){
        viewPager.setCurrentItem(viewPager.currentItem + 1, true);
        heightValue = height.toInt()
        weightValue = weight.toFloat()
        genderValue = gender

    }
    override fun someEvent3(goal : String, currentFitness: String){
        goalValue = goal
        currentFitnessValue = currentFitness
        insertDataDatabase(firstNameText,lastNameText,heightValue,weightValue,genderValue,currentFitnessValue,goalValue)

    }





}