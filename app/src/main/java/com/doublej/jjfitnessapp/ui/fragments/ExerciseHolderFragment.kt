package com.doublej.jjfitnessapp.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.ui.testfragments.ExerciciosFragment

class ExerciseHolderFragment : Fragment() {

    companion object {
        fun newInstance() = ExerciseHolderFragment()
    }

    private lateinit var viewModel: ExerciseHolderViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val exerciseFragment = ExerciciosFragment()
        setCurrentFragment(exerciseFragment)



        return inflater.inflate(R.layout.exercise_holder_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExerciseHolderViewModel::class.java)
        // TODO: Use the ViewModel
    }
    fun setCurrentFragment(fragment : Fragment){
        Log.d("fragmentTEST", fragment.toString())
        if(fragment != null){
            val transaction = activity?.supportFragmentManager?.beginTransaction()!!
            transaction.replace(R.id.exerciseHolderLayout, fragment).addToBackStack(null)
            transaction.commit()

        }
    }

}