package com.doublej.jjfitnessapp.ui.testfragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doublej.jjfitnessapp.R

class NutritionTestFragment : Fragment() {

    companion object {
        fun newInstance() = NutritionTestFragment()
    }

    private lateinit var viewModel: NutritionTestViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.nutrition_test_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NutritionTestViewModel::class.java)
        // TODO: Use the ViewModel
    }

}