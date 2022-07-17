package com.doublej.jjfitnessapp.ui.fragments

import android.animation.ObjectAnimator
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.databinding.MetabolicFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase

import me.itangqi.waveloadingview.WaveLoadingView
import java.time.LocalDate
import java.time.Period
import java.util.*

class MetabolicFragment : Fragment() {

    companion object {
        fun newInstance() = MetabolicFragment()
    }

    private lateinit var viewModel: MetabolicViewModel
    private var _binding: MetabolicFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var waveProgressBasal : WaveLoadingView
    private lateinit var waveProgressMet : WaveLoadingView
    private lateinit var waveProgressCal : WaveLoadingView
    private var basalValue : Int = 0
    private var metValue : Int = 0
    private var recommendedCalories : Int = 0
    private var userHeight : Int = 0
    private var userWeight : Float = 0f
    private var userGender : String = "Homem"
    private var userLevelOfActivity : String = "Moderado"
    private var userGoal : String = "Manter peso"
    private var userAge : Int = 0
    private var dayOfBirth : Int = 0
    private var monthOfBirth : Int = 0
    private var yearOfBirth : Int = 0
    private var isViewShown : Boolean = false
    private lateinit var metBasText : TextView
    private lateinit var taxaMetText : TextView
    private lateinit var taxaCalText : TextView
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = MetabolicFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        waveProgressBasal = root.findViewById(R.id.waveLoadingView)
        waveProgressMet = root.findViewById(R.id.waveLoadingView2)
        waveProgressCal = root.findViewById(R.id.waveLoadingView3)
        metBasText = root.findViewById(R.id.MetBasalText)
        taxaMetText = root.findViewById(R.id.MetTaxaText)
        taxaCalText = root.findViewById(R.id.metCalText)
        metBasText.alpha = 0f
        taxaMetText.alpha = 0f
        taxaCalText.alpha = 0f
        isViewShown = true
        database = FirebaseFirestore.getInstance()
        auth = Firebase.auth
        val userId = auth.currentUser!!.uid



        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MetabolicViewModel::class.java)
        // TODO: Use the ViewModel
    }


    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser == true){
            MacroFragment().removeMacroData()
            ImcFragment().removeImcData()
        }
        if(isVisibleToUser == true && view != null && isViewShown == true){
            removeMetData()
            updateData()


        }else{
            if(view != null) {
                isViewShown = true
                removeMetData()
            }

        }
    }
    fun applyMetData(){
        Log.d("testValues", userGoal)
        if(userGender == "Homem"){
            basalValue = ((9.99f * userWeight) + (6.25f * userHeight) - (4.92f * userAge) + 5).toInt()
        }
        if(userGender == "Mulher"){
            basalValue =((9.99f * userWeight) + (6.25f * userHeight) - (4.92f * userAge) - 161).toInt()
        }
        if(userLevelOfActivity == "Sedentário"){
            metValue = (basalValue * 1.2f).toInt()
        }
        if(userLevelOfActivity == "Pouco ativo"){
            metValue = (basalValue * 1.300f).toInt()
        }
        if(userLevelOfActivity == "Moderado"){
            metValue = (basalValue * 1.375f).toInt()
        }
        if(userLevelOfActivity == "Ativo"){
            metValue = (basalValue * 1.55f).toInt()
        }
        if(userLevelOfActivity == "Muito ativo"){
            metValue = (basalValue * 1.725f).toInt()
        }
        if(userGoal == "Perder peso"){
            recommendedCalories = metValue - 200
        }
        if(userGoal == "Manter peso"){
            recommendedCalories = metValue
        }

        if(userGoal == "Ganhar peso"){
            recommendedCalories = metValue + 200
        }

        waveProgressBasal.progressValue = ((basalValue * 100f ) / 4000f).toInt()
        waveProgressMet.progressValue = ((metValue * 100f ) / 4000f).toInt()
        waveProgressCal.progressValue = ((recommendedCalories * 100f ) / 4000f).toInt()
        metBasText.text = basalValue.toString() + "kcal"
        taxaMetText.text = metValue.toString() + "kcal"
        taxaCalText.text = recommendedCalories.toString() + "kcal"
        val animation: ObjectAnimator = ObjectAnimator.ofFloat(metBasText, "alpha", 1f)
        animation.setDuration(1000)
        animation.start()
        val animation1: ObjectAnimator = ObjectAnimator.ofFloat(taxaMetText, "alpha", 1f)
        animation1.setDuration(1000)
        animation1.start()
        val animation2: ObjectAnimator = ObjectAnimator.ofFloat(taxaCalText, "alpha", 1f)
        animation2.setDuration(1000)
        animation2.start()

    }

    fun removeMetData(){
        if(view != null) {
            waveProgressBasal.progressValue = 0
            waveProgressMet.progressValue = 0
            waveProgressCal.progressValue = 0
            metBasText.alpha = 0f
            taxaMetText.alpha = 0f
            taxaCalText.alpha = 0f
        }


    }

    fun updateData(){
        database = FirebaseFirestore.getInstance()
        auth = Firebase.auth
        val userId = auth.currentUser!!.uid
        database.collection("Users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    userHeight = document.getLong("height")?.toInt()!!
                    userWeight = document.getLong("weight")?.toFloat()!!
                    userGender = document.getString("gender").toString()
                    userLevelOfActivity = document.getString("levelOfActivity").toString()
                    userGoal = document.getString("goal").toString()
                    dayOfBirth = document.getLong("dayOfBirth")?.toInt()!!
                    monthOfBirth = document.getLong("monthOfBirth")?.toInt()!!
                    yearOfBirth = document.getLong("yearOfBirth")?.toInt()!!
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        userAge = Period.between(
                            LocalDate.of(yearOfBirth, monthOfBirth, dayOfBirth),
                            LocalDate.now()
                        ).years.toInt()
                        Log.d("ageTest", userAge.toString())
                    }

                    //savePref(imc)
                    applyMetData()
                    //dataRetrieved = true

                } else {
                    Log.d("firestore", "Documento não encontrado")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("firestore", "Falha na ligação ", exception)
            }

    }


}