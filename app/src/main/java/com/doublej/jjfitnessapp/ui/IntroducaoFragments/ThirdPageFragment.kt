package com.doublej.jjfitnessapp.ui.IntroducaoFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.databinding.ThirdPageFragmentBinding
import com.google.android.material.textfield.TextInputEditText
import com.sackcentury.shinebuttonlib.ShineButton
import io.ghyeok.stickyswitch.widget.StickySwitch
import java.lang.ClassCastException

class ThirdPageFragment : Fragment() {
    private var _binding: ThirdPageFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var confirmButton : ShineButton
    private lateinit var goalSeekBar : SeekBar
    private lateinit var fitnessSeekBar: SeekBar
    private lateinit var metabolicState : SeekBar
    private lateinit var firstNameText : TextInputEditText
    private lateinit var lastNameText : TextInputEditText
    private lateinit var heightText : TextInputEditText
    private lateinit var weightText : TextInputEditText
    private lateinit var genderSwitch : StickySwitch
    private lateinit var thirdButton : ShineButton
    private lateinit var goalText : TextView
    private lateinit var fitnessText : TextView
    private lateinit var metabolicStateText : TextView
    private lateinit var checkImage : ImageView
    private lateinit var xImage : ImageView
    private lateinit var actionText : TextView
    private lateinit var viewModel: ThirdPageViewModel
    private var goalString : String = "Perder peso"
    private var fitnessString : String = "Moderado"
    private var isLocked : Boolean = false


    interface onSomeEventListener {
        fun someEvent3(goal : String, currentFitness : String)
    }
    lateinit var someEventListener3 : onSomeEventListener


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            someEventListener3 = context as onSomeEventListener
        }catch (e : ClassCastException) {
            throw ClassCastException(context.toString() + "error")
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ThirdPageFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root


        thirdButton = root.findViewById(R.id.thirdPageButton)
        goalSeekBar = root.findViewById(R.id.goalSeekBar)
        fitnessSeekBar = root.findViewById(R.id.fitnessSeekBar)
        goalText = root.findViewById(R.id.goalText)
        fitnessText = root.findViewById(R.id.fitnessText)
        metabolicStateText = root.findViewById(R.id.metabolicState)
        metabolicState = root.findViewById(R.id.metabolicStateSlider)

        thirdButton.setOnClickListener {
            callGoalAndCurrentFitness()
        }






        goalSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                when (progress) {
                    0 -> goalString = "Perder peso"
                    1 -> goalString = "Manter peso"
                    2 -> goalString = "Ganhar peso"
                }
                Log.d("stringTest", goalString)
                goalText.text = goalString



            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped

            }
        })

        metabolicState.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                when (progress) {
                    0 -> metabolicStateText.text = "Ganho peso com facilidade"
                    1 -> metabolicStateText.text = "Ganho peso com moderação"
                    2 -> metabolicStateText.text = "Tenho dificuldade em ganhar peso"
                }
                Log.d("stringTest", goalString)



            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped

            }
        })

        fitnessSeekBar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar, progress: Int, fromUser: Boolean) {
                when (progress) {
                    0 -> fitnessString = "Sedentário"
                    1 -> fitnessString = "Pouco ativo"
                    2 -> fitnessString = "Moderado"
                    3 -> fitnessString = "Ativo"
                    4 -> fitnessString = "Muito ativo"

                }
                fitnessText.text = fitnessString


            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped

            }
        })

        return root
    }
    fun callGoalAndCurrentFitness(){

        someEventListener3.someEvent3(goalString,fitnessString)
    }



}