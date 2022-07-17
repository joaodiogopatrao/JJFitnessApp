package com.doublej.jjfitnessapp.ui.testfragments

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.doublej.jjfitnessapp.R

import com.doublej.jjfitnessapp.databinding.CalculoMetabolismoFragmentBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class CalculoMetabolismoFragment : Fragment() {
    private lateinit var navView : BottomNavigationView
    private lateinit var heightSeekBar : SeekBar
    private lateinit var weightSeekBar : SeekBar
    private lateinit var ageSeekBar : SeekBar
    private lateinit var heightText : TextView
    private lateinit var weightText : TextView
    private lateinit var ageText : TextView
    private lateinit var maleButton : CardView
    private lateinit var calcularButton : CardView
    private lateinit var femaleButton : CardView
    private lateinit var resultsCardView : CardView
    private lateinit var maleButtonBackground : ImageView
    private lateinit var femaleButtonBackground : ImageView
    private lateinit var blackMaleSymbol : ImageView
    private lateinit var blackFemaleSymbol : ImageView
    private lateinit var maleText : TextView
    private lateinit var femaleText : TextView
    private lateinit var tmbProgressBar : ProgressBar
    private lateinit var tmProgressBar : ProgressBar
    private lateinit var tmGainProgressBar: ProgressBar
    private lateinit var sedentarioButton : CardView
    private lateinit var ativLigeiraButton : CardView
    private lateinit var ativModeradaButton : CardView
    private lateinit var ativIntensaButton : CardView
    private lateinit var sedentarioText : TextView
    private lateinit var ativLigeiraText : TextView
    private lateinit var ativModeradaText : TextView
    private lateinit var ativIntensaText : TextView
    private lateinit var sedentarioBackground : ImageView
    private lateinit var ativLigeiraBackground : ImageView
    private lateinit var ativModeradaBackground : ImageView
    private lateinit var ativIntensaBackground : ImageView
    private lateinit var blackSedentarioIcon : ImageView
    private lateinit var blackAtivLigeiraIcon : ImageView
    private lateinit var blackAtivModeradaIcon : ImageView
    private lateinit var blackAtivIntensaIcon : ImageView
    private lateinit var restartButton : CardView
    private lateinit var tmbText : TextView
    private lateinit var tmText : TextView
    private var isMale : Int = 0
    private var heightVal : Int = 170
    private var weightVal : Int = 70
    private var ageVal : Int = 20
    private var bmr : Float = 0.0f
    private var mr : Float = 0.0f
    private var activityLevel : Int = 0
    private var isCalcularMetabolismo : Boolean = false
    private var canRestartCalcMetabolismo : Boolean = false
    private var _binding: CalculoMetabolismoFragmentBinding? = null
    private lateinit var viewModel: CalculoMetabolismoViewModel
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CalculoMetabolismoFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        //navView = activity?.findViewById(R.id.nav_view)!!
       // navView.isVisible = false
        maleButton = root.findViewById(R.id.maleButton)
        femaleButton = root.findViewById(R.id.femaleButton)
        maleButtonBackground = root.findViewById(R.id.maleButtonBackground)
        femaleButtonBackground = root.findViewById(R.id.femaleButtonBackground)
        blackMaleSymbol = root.findViewById(R.id.blackMaleSymbol)
        blackFemaleSymbol = root.findViewById(R.id.blackFemaleSymbol)
        maleText = root.findViewById(R.id.maleText)
        femaleText = root.findViewById(R.id.femaleText)
        heightSeekBar = root.findViewById(R.id.heightSeekBar)
        weightSeekBar = root.findViewById(R.id.weightSeekBar)
        ageSeekBar = root.findViewById(R.id.idadeSeekBar)
        heightText = root.findViewById(R.id.heightText)
        weightText = root.findViewById(R.id.weightText)
        calcularButton = root.findViewById(R.id.calcularButton)
        ageText = root.findViewById(R.id.ageText)
        resultsCardView = root.findViewById(R.id.resultsCardView)
        tmbProgressBar = root.findViewById(R.id.tmbProgressBar)
        tmProgressBar = root.findViewById(R.id.tmProgressBar)
        tmGainProgressBar = root.findViewById(R.id.tmGainProgressBar)
        sedentarioButton = root.findViewById(R.id.sedentarioButton)
        ativLigeiraButton = root.findViewById(R.id.ativLigeiraButton)
        ativModeradaButton = root.findViewById(R.id.ativModeradaButton)
        ativIntensaButton = root.findViewById(R.id.ativIntensaButton)
        sedentarioText = root.findViewById(R.id.sedentarioText)
        ativLigeiraText = root.findViewById(R.id.ativLigeiraText)
        ativModeradaText = root.findViewById(R.id.ativModeradaText)
        ativIntensaText = root.findViewById(R.id.ativIntensaText)
        sedentarioBackground = root.findViewById(R.id.sedentarioBackground)
        ativLigeiraBackground = root.findViewById(R.id.ativLigeiraBackground)
        ativModeradaBackground = root.findViewById(R.id.ativModeradaBackground)
        ativIntensaBackground = root.findViewById(R.id.ativIntensaBackground)
        blackSedentarioIcon = root.findViewById(R.id.sedentarioIcon)
        blackAtivLigeiraIcon = root.findViewById(R.id.ativLigeiraIcon)
        blackAtivModeradaIcon = root.findViewById(R.id.ativModeradaIcon)
        blackAtivIntensaIcon = root.findViewById(R.id.ativIntensaIcon)
        restartButton = root.findViewById(R.id.restartButton)
        tmbText = root.findViewById(R.id.tmbText)
        tmText = root.findViewById(R.id.tmText)

        tmbProgressBar.progress = 0
        tmProgressBar.progress = 0
        tmGainProgressBar.progress = 0

        heightSeekBar.max = ((220 - 120) / 1)
        weightSeekBar.max = ((250 - 30) / 1)
        ageSeekBar.max = ((100 - 3) / 1)
        heightSeekBar.progress = 50
        weightSeekBar.progress = 40
        ageSeekBar.progress = 17

        maleButton.setOnClickListener {
            isMale = 1
            maleButtonBackground.isVisible = true
            femaleButtonBackground.isVisible = false
            blackMaleSymbol.isVisible = false
            blackFemaleSymbol.isVisible = true
            maleText.setTextColor(Color.parseColor("#FFFFFF"))
            femaleText.setTextColor(Color.parseColor("#737373"))


        }
        femaleButton.setOnClickListener {
            isMale = 2
            maleButtonBackground.isVisible = false
            femaleButtonBackground.isVisible = true
            blackMaleSymbol.isVisible = true
            blackFemaleSymbol.isVisible = false
            maleText.setTextColor(Color.parseColor("#737373"))
            femaleText.setTextColor(Color.parseColor("#FFFFFF"))

        }

        heightSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                heightVal = progress + 120
                heightText.text = heightVal.toString() + "cm"

            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped

            }
        })

        weightSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                weightVal = progress + 30
                weightText.text = weightVal.toString() + "kg"

            }


            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {


            }
        })

        ageSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                ageVal = progress + 3
                ageText.text = ageVal.toString()

            }


            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {


            }
        })

        calcularButton.setOnClickListener {
            isCalcularMetabolismo = true
            calcularMetabolismo(heightVal, weightVal,ageVal)
        }

        sedentarioButton.setOnClickListener {
            activityLevel = 1
            sedentarioBackground.isVisible = true
            ativLigeiraBackground.isVisible = false
            ativModeradaBackground.isVisible = false
            ativIntensaBackground.isVisible = false
            sedentarioText.setTextColor(Color.parseColor("#FFFFFF"))
            ativIntensaText.setTextColor(Color.parseColor("#737373"))
            ativModeradaText.setTextColor(Color.parseColor("#737373"))
            ativLigeiraText.setTextColor(Color.parseColor("#737373"))
            blackSedentarioIcon.isVisible = false
            blackAtivLigeiraIcon.isVisible = true
            blackAtivModeradaIcon.isVisible = true
            blackAtivIntensaIcon.isVisible = true
        }
        ativLigeiraButton.setOnClickListener {
            activityLevel = 2
            sedentarioBackground.isVisible = false
            ativLigeiraBackground.isVisible = true
            ativModeradaBackground.isVisible = false
            ativIntensaBackground.isVisible = false
            sedentarioText.setTextColor(Color.parseColor("#737373"))
            ativIntensaText.setTextColor(Color.parseColor("#737373"))
            ativModeradaText.setTextColor(Color.parseColor("#737373"))
            ativLigeiraText.setTextColor(Color.parseColor("#FFFFFF"))
            blackSedentarioIcon.isVisible = true
            blackAtivLigeiraIcon.isVisible = false
            blackAtivModeradaIcon.isVisible = true
            blackAtivIntensaIcon.isVisible = true
        }
        ativModeradaButton.setOnClickListener {
            activityLevel = 3
            sedentarioBackground.isVisible = false
            ativLigeiraBackground.isVisible = false
            ativModeradaBackground.isVisible = true
            ativIntensaBackground.isVisible = false
            sedentarioText.setTextColor(Color.parseColor("#737373"))
            ativIntensaText.setTextColor(Color.parseColor("#737373"))
            ativModeradaText.setTextColor(Color.parseColor("#FFFFFF"))
            ativLigeiraText.setTextColor(Color.parseColor("#737373"))
            blackSedentarioIcon.isVisible = true
            blackAtivLigeiraIcon.isVisible = true
            blackAtivModeradaIcon.isVisible = false
            blackAtivIntensaIcon.isVisible = true
        }
        ativIntensaButton.setOnClickListener {
            activityLevel = 4
            sedentarioBackground.isVisible = false
            ativLigeiraBackground.isVisible = false
            ativModeradaBackground.isVisible = false
            ativIntensaBackground.isVisible = true
            sedentarioText.setTextColor(Color.parseColor("#737373"))
            ativIntensaText.setTextColor(Color.parseColor("#FFFFFF"))
            ativModeradaText.setTextColor(Color.parseColor("#737373"))
            ativLigeiraText.setTextColor(Color.parseColor("#737373"))
            blackSedentarioIcon.isVisible = true
            blackAtivLigeiraIcon.isVisible = true
            blackAtivModeradaIcon.isVisible = true
            blackAtivIntensaIcon.isVisible = false
        }

        restartButton.setOnClickListener {
            canRestartCalcMetabolismo = true
            restartCalcImc()
        }

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CalculoMetabolismoViewModel::class.java)
        // TODO: Use the ViewModel
    }

    private fun calcularMetabolismo(height : Int, weight : Int, age : Int){


        if(isMale == 1){
            bmr = (9.99f * weight) + (6.25f * height) - (4.92f * age) + 5
        }
        if(isMale == 2){
            bmr =(9.99f * weight) + (6.25f * height) - (4.92f * age) - 161
        }
        if(activityLevel == 1){
            mr = bmr * 1.2f
        }
        if(activityLevel == 2){
            mr = bmr * 1.375f
        }
        if(activityLevel == 3){
            mr = bmr * 1.55f
        }
        if(activityLevel == 4){
            mr = bmr * 1.725f
        }

        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if(isCalcularMetabolismo == true){
                val paramsButton = calcularButton.layoutParams as ConstraintLayout.LayoutParams
                    val paramsRestart = restartButton.layoutParams as ConstraintLayout.LayoutParams
                calcularButton.requestLayout()
                    restartButton.requestLayout()

                if (paramsButton.matchConstraintPercentHeight > 0f) {
                    paramsButton.matchConstraintPercentHeight -= 0.123f
                }
                if (paramsButton.matchConstraintPercentHeight <= 0f) {
                    paramsButton.matchConstraintPercentHeight = 0f
                    if (resultsCardView.alpha < 1f) {
                        resultsCardView.alpha += 0.05f
                    }
                    if (resultsCardView.alpha >= 1f) {
                        resultsCardView.alpha = 1f
                        if (tmbProgressBar.progress < bmr){
                            tmbProgressBar.progress += 30
                            tmbText.text = (tmbProgressBar.progress).toString()

                        }
                        if(tmbProgressBar.progress >=bmr){
                            tmbProgressBar.progress = bmr.toInt()
                        }
                        if (tmProgressBar.progress < bmr){
                            tmProgressBar.progress += 30

                        }
                        if(tmProgressBar.progress >=bmr){
                            tmProgressBar.progress = bmr.toInt()
                        }
                        if (tmGainProgressBar.progress < mr){
                            tmGainProgressBar.progress += 30


                        }
                        if(tmGainProgressBar.progress >=mr){

                            tmGainProgressBar.progress = (mr ).toInt()

                            if(paramsRestart.matchConstraintPercentHeight < 0.2f){
                                paramsRestart.matchConstraintPercentHeight += 0.04f
                            }

                            if(paramsRestart.matchConstraintPercentHeight >= 0.2f){
                                paramsRestart.matchConstraintPercentHeight = 0.2f

                            }
                            if(paramsRestart.matchConstraintPercentWidth < 0.4f){
                                paramsRestart.matchConstraintPercentWidth += 0.08f
                            }
                            if(paramsRestart.matchConstraintPercentWidth >= 0.4f){
                                paramsRestart.matchConstraintPercentWidth = 0.4f
                                isCalcularMetabolismo = false
                            }
                        }
                    }

                    }
                    tmbText.text = (tmbProgressBar.progress).toString() + "kcal"
                    tmText.text = (tmGainProgressBar.progress).toString() + "kcal"
                    handler.postDelayed(this, 10)
                }
            }
        }, 0)

    }

    private fun restartCalcImc(){
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {



                    handler.postDelayed(this, 10)
                }

        }, 0)

    }

}