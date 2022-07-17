package com.doublej.jjfitnessapp.ui.activities

import android.content.Intent
import android.graphics.Point
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.ui.StartFragments.*
import com.doublej.jjfitnessapp.ui.dashboard.ViewPagerDuration
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class StartActivity : AppCompatActivity() {
    private lateinit var logoCard : CardView
    private var canStartFirstAnimation : Boolean = true
    private var canStartSecondAnimation : Boolean = false
    private lateinit var adapter : MyViewPagerAdapterStart
    private lateinit var viewPager : ViewPager
    private lateinit var dotsIndicator : WormDotsIndicator
    private lateinit var welcomeText : TextView
    private lateinit var loginMenuButton : CardView
    private lateinit var registerMenuButton : CardView
    private lateinit var viewPagerDuration : ViewPagerDuration
    private var counter : Float = 0f
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        loginMenuButton = findViewById(R.id.iniciarSessaoCard)
        registerMenuButton = findViewById(R.id.criarContaCard)
        val displayMetrics = DisplayMetrics()
        windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val density : Float = displayMetrics.density
        Log.d("dimtest", displayMetrics.widthPixels.toString() + "|" + density.toString())
        setCurrentFragment(loadingScreenFragment())

        adapter = MyViewPagerAdapterStart(supportFragmentManager)
        viewPager = findViewById(R.id.startViewPager)
        adapter.addFragment(FirstSlideFragment())
        adapter.addFragment(SecondSlideFragment())
        adapter.addFragment(ThirdSlideFragment())
        adapter.addFragment(FourthSlideFragment())
        viewPager.adapter = adapter

        viewPagerDuration = viewPager as ViewPagerDuration
        viewPagerDuration.setScrollDurationFactor(10f)
        viewPagerAnimation()

        loginMenuButton.setOnClickListener {
            var intent = Intent(this@StartActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        registerMenuButton.setOnClickListener {
            var intent = Intent(this@StartActivity, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }



    }

    class MyViewPagerAdapterStart(manager: FragmentManager) : FragmentPagerAdapter(manager) {
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

    fun viewPagerAnimation(){
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                counter += 1f

                if(counter > 300f){

                    if(viewPager.currentItem + 1 < 4){
                    viewPager.setCurrentItem(viewPager.currentItem + 1, true)
                    }
                    if(viewPager.currentItem + 1== 4){
                        viewPager.setCurrentItem(0, true)
                    }
                    Log.d("viewPagerTest", viewPager.currentItem.toString())
                    counter = 0f

                }


                handler.postDelayed(this, 10)

            }

        }, 0)
    }




    fun setCurrentFragment(fragment : Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.loadingScreen, fragment)
            transaction.commit()

        }
    }


}