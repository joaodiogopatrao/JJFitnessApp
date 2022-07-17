package com.doublej.jjfitnessapp.ui.testfragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doublej.jjfitnessapp.R

class DefinicoesFragment : Fragment() {

    companion object {
        fun newInstance() = DefinicoesFragment()
    }

    private lateinit var viewModel: DefinicoesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.definicoes_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(DefinicoesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}