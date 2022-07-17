package com.doublej.jjfitnessapp.ui.activities

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.databinding.FragmentNutricaoBinding
import com.doublej.jjfitnessapp.ui.fragments.DashboardFragment
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mxn.soul.flowingdrawer_core.FlowingDrawer
import me.ertugrul.lib.SuperBottomBar
import com.doublej.jjfitnessapp.ui.fragments.NutritionFragment
import com.doublej.jjfitnessapp.ui.testfragments.ExerciciosFragment


class MainActivity : AppCompatActivity() {

    //DECLARAÇÃO DAS VARIAVEIS
    private lateinit var bottomBar : SuperBottomBar
    private var _binding: FragmentNutricaoBinding? = null
    private lateinit var hamburgerIcon : ImageView
    private var mAuth: FirebaseAuth? = null
    private var user: FirebaseUser? = null
    private var mAuthListner: FirebaseAuth.AuthStateListener? = null
    private var permissioncode : Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
        }
        getWindow().setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )


        //INICIALIZAÇÃO DAS VARIAVEIS
        hamburgerIcon = findViewById(R.id.hamburgerIcon)
        hamburgerIcon.setOnClickListener{openBottomDrawer()}
        mAuth = FirebaseAuth.getInstance()
        mAuthListner = FirebaseAuth.AuthStateListener {
                firebaseAuth: FirebaseAuth ->
            user = firebaseAuth.currentUser
            if(user != null){
            }
            else{

            }

        }

        //VERIFICAR PERMISSÕES PARA O SENSOR DE CONTAGEM DE PASSOS
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACTIVITY_RECOGNITION),
                permissioncode)
        }

        mAuth!!.addAuthStateListener(mAuthListner!!)
        bottomBar = findViewById(R.id.bottomBar)


        val firstFragment = ExerciciosFragment()
        val secondFragment = NutritionFragment()
        val thirdFragment = DashboardFragment()

        setCurrentFragment(firstFragment)

        //CONFIGURAÇÃO DA BOTTOMBAR
        bottomBar.onItemSelected = { pos ->
            if(pos == 0){
                setCurrentFragment(firstFragment)
            }
            if(pos == 1){
                setCurrentFragment(secondFragment)
            }
            if(pos == 2){
                setCurrentFragment(thirdFragment)
            }
        }
    }

    fun setCurrentFragment(fragment : Fragment){
        if(fragment != null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.commit()

        }
    }
    fun openBottomDrawer(){
        var bottomSheetDialog : BottomSheetDialog = BottomSheetDialog(this@MainActivity,R.style.BottomSheetDialogTheme)
        var bottomSheetView : View = LayoutInflater.from(applicationContext).inflate(R.layout.bottom_sheet_fragment,findViewById(R.id.bottom_drawer_1)) as LinearLayout
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.window
        bottomSheetDialog.show()

    }


}