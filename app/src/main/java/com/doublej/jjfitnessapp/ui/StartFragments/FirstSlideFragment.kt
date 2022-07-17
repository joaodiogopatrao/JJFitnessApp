package com.doublej.jjfitnessapp.ui.StartFragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doublej.jjfitnessapp.R

class FirstSlideFragment : Fragment() {

    companion object {
        fun newInstance() = FirstSlideFragment()
    }

    private lateinit var viewModel: FirstSlideViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.first_slide_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(FirstSlideViewModel::class.java)
        // TODO: Use the ViewModel
    }

}