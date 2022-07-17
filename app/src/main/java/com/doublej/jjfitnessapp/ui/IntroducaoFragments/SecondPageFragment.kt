package com.doublej.jjfitnessapp.ui.IntroducaoFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.databinding.SecondPageFragmentBinding
import com.google.android.material.textfield.TextInputEditText
import com.sackcentury.shinebuttonlib.ShineButton
import io.ghyeok.stickyswitch.widget.StickySwitch
import java.lang.ClassCastException

class SecondPageFragment : Fragment() {


    private lateinit var viewModel: SecondPageViewModel
    private var _binding: SecondPageFragmentBinding? = null
    private lateinit var secondPageButton: ShineButton
    public lateinit var genderPicker: StickySwitch
    public lateinit var heightEditText: TextInputEditText
    public lateinit var weightEditText: TextInputEditText
    private lateinit var checkImage: ImageView
    private lateinit var xImage: ImageView
    private lateinit var actionText: TextView
    private var isLocked: Boolean = false
    private var buttonChecker: Boolean = false

    private val binding get() = _binding!!

    interface onSomeEventListener {
        fun someEvent2(weight : String, height : String, gender : String)
    }
    lateinit var someEventListener2 : onSomeEventListener


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            someEventListener2 = context as onSomeEventListener
        }catch (e : ClassCastException) {
            throw ClassCastException(context.toString() + "error")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = SecondPageFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        secondPageButton = root.findViewById(R.id.secondPageButton)
        weightEditText = root.findViewById(R.id.WeightEditText)
        heightEditText = root.findViewById(R.id.heightEditText)
        genderPicker = root.findViewById(R.id.genderPicker)

        secondPageButton.setOnClickListener { callHeightWeightGender() }




        return root

    }


    fun callHeightWeightGender(){
        someEventListener2.someEvent2(weightEditText.text.toString(),heightEditText.text.toString(),genderPicker.getText())
    }
}