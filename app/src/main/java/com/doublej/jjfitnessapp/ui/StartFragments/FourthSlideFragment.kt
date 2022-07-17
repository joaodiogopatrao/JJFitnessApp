package com.doublej.jjfitnessapp.ui.StartFragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doublej.jjfitnessapp.R

class FourthSlideFragment : Fragment() {

    companion object {
        fun newInstance() = FourthSlideFragment()
    }

    private lateinit var viewModel: FourthSlideViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fourth_slide_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FourthSlideViewModel::class.java)
        // TODO: Use the ViewModel
    }

}