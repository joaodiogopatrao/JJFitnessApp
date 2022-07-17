package com.doublej.jjfitnessapp.ui.StartFragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doublej.jjfitnessapp.R

class ThirdSlideFragment : Fragment() {

    companion object {
        fun newInstance() = ThirdSlideFragment()
    }

    private lateinit var viewModel: ThirdSlideViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.third_slide_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ThirdSlideViewModel::class.java)
        // TODO: Use the ViewModel
    }

}