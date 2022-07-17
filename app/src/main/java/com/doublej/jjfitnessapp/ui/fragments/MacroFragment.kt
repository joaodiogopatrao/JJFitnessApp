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
import com.doublej.jjfitnessapp.databinding.MacroFragmentBinding
import com.github.mikephil.charting.charts.PieChart
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mackhartley.roundedprogressbar.RoundedProgressBar
import java.time.LocalDate
import java.time.Period

class MacroFragment : Fragment() {

    companion object {
        fun newInstance() = MacroFragment()
    }

    private lateinit var viewModel: MacroViewModel
    private var _binding: MacroFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var proteinProgress: RoundedProgressBar
    private lateinit var carbsProgress: RoundedProgressBar
    private lateinit var fatProgress: RoundedProgressBar
    private lateinit var proteinValueText: TextView
    private lateinit var carbsValueText: TextView
    private lateinit var fatsValueText: TextView
    private lateinit var pieChart: PieChart

    private var isViewShown: Boolean = false
    private var proteinValue: Int = 180
    private var carbValue: Int = 250
    private var fatValue: Int = 90
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var userHeight : Int = 0
    private var userWeight : Float = 0f
    private var dayOfBirth : Int = 0
    private var monthOfBirth : Int = 0
    private var yearOfBirth : Int = 0
    private var userAge : Int = 0
    private var basalValue : Int = 0
    private var metValue : Int = 0
    private var recommendedCalories : Int = 0
    private var recommendedProtein : Int = 0
    private var recommendedCarbs : Int = 0
    private var recommendedFats : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MacroFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        proteinProgress = root.findViewById(R.id.ProteinProgress)
        carbsProgress = root.findViewById(R.id.CarbsProgress)
        fatProgress = root.findViewById(R.id.FatProgress1)
        proteinValueText = root.findViewById(R.id.proteinValueText)
        carbsValueText = root.findViewById(R.id.carbsValueText)
        fatsValueText = root.findViewById(R.id.fatsValueText)
        database = FirebaseFirestore.getInstance()
        auth = Firebase.auth

        proteinProgress.setProgressPercentage(0.0)
        carbsProgress.setProgressPercentage(0.0)
        fatProgress.setProgressPercentage(0.0)
        proteinValueText.alpha = 0f
        carbsValueText.alpha = 0f
        fatsValueText.alpha = 0f

        isViewShown = true

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MacroViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser == true) {
            ImcFragment().removeImcData()
            MetabolicFragment().removeMetData()
        }
        if (isVisibleToUser == true && view != null && isViewShown == true) {
            removeMacroData()
            applyMacroData()


        } else {
            if (view != null) {
                isViewShown = true
                removeMacroData()
            }

        }
    }

    fun applyMacroData() {
        var userGender : String = "Homem"
        var userLevelOfActivity : String = "Sedentário"
        var userGoal : String = "Manter Peso"

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
                        recommendedCarbs = (recommendedCalories * 0.65 / 4f).toInt()
                        recommendedProtein = (recommendedCalories * 0.3 / 4f).toInt()
                        recommendedFats = (recommendedCalories * 0.15 / 9f).toInt()
                    }
                    if(userGoal == "Manter peso"){
                        recommendedCalories = metValue
                        recommendedCarbs = (recommendedCalories * 0.5 / 4f).toInt()
                        recommendedProtein = (recommendedCalories * 0.28 / 4f).toInt()
                        recommendedFats = (recommendedCalories * 0.22 / 9f).toInt()
                    }

                    if(userGoal == "Ganhar peso"){
                        recommendedCalories = metValue + 200
                        recommendedCarbs = (recommendedCalories * 0.38 / 4f).toInt()
                        recommendedProtein = (recommendedCalories * 0.28 / 4f).toInt()
                        recommendedFats = (recommendedCalories * 0.32 / 9f).toInt()
                    }
                    updateData()




                    //savePref(imc)
                    //dataRetrieved = true

                } else {
                    Log.d("firestore", "Documento não encontrado")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("firestore", "Falha na ligação ", exception)
            }




        proteinProgress.setProgressPercentage(70.0)
        carbsProgress.setProgressPercentage(37.0)
        fatProgress.setProgressPercentage(51.0)
        proteinValueText.text = 70.0.toString() + "g"
        carbsValueText.text = 37.0.toString() + "g"
        fatsValueText.text = 51.0.toString() + "g"
        val animation: ObjectAnimator = ObjectAnimator.ofFloat(proteinValueText, "alpha", 1f)
        animation.setDuration(1000)
        animation.start()
        val animation1: ObjectAnimator = ObjectAnimator.ofFloat(carbsValueText, "alpha", 1f)
        animation1.setDuration(1000)
        animation1.start()
        val animation2: ObjectAnimator = ObjectAnimator.ofFloat(fatsValueText, "alpha", 1f)
        animation2.setDuration(1000)
        animation2.start()
    }

    fun updateData(){

        proteinProgress.setProgressPercentage(recommendedProtein / 350f * 100f.toDouble())
        carbsProgress.setProgressPercentage(recommendedCarbs / 350f * 100f.toDouble())
        fatProgress.setProgressPercentage(recommendedFats / 350f * 100f.toDouble())
        proteinValueText.text = recommendedProtein.toString() + "g"
        carbsValueText.text = recommendedCarbs.toString() + "g"
        fatsValueText.text = recommendedFats.toString() + "g"
        val animation: ObjectAnimator = ObjectAnimator.ofFloat(proteinValueText, "alpha", 1f)
        animation.setDuration(1000)
        animation.start()
        val animation1: ObjectAnimator = ObjectAnimator.ofFloat(carbsValueText, "alpha", 1f)
        animation1.setDuration(1000)
        animation1.start()
        val animation2: ObjectAnimator = ObjectAnimator.ofFloat(fatsValueText, "alpha", 1f)
        animation2.setDuration(1000)
        animation2.start()

    }

    fun removeMacroData() {

        if (view != null) {
            proteinProgress.setProgressPercentage(0.0)
            carbsProgress.setProgressPercentage(0.0)
            fatProgress.setProgressPercentage(0.0)
            proteinValueText.alpha = 0f
            carbsValueText.alpha = 0f
            fatsValueText.alpha = 0f
            isViewShown = true
        }


    }
}