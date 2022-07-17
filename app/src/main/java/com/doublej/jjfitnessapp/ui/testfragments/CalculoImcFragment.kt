package com.doublej.jjfitnessapp.ui.testfragments

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.databinding.CalculoImcFragmentBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.math.RoundingMode
import java.text.DecimalFormat


class CalculoImcFragment : Fragment() {

    private var _binding: CalculoImcFragmentBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CalculoImcFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

}