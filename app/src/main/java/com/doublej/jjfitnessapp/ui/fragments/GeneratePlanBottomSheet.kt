package com.doublej.jjfitnessapp.ui.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.databinding.GeneratePlanBottomSheetFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.roughike.swipeselector.SwipeItem
import com.roughike.swipeselector.SwipeSelector

class GeneratePlanBottomSheet : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = GeneratePlanBottomSheet()
    }

    private lateinit var viewModel: GeneratePlanBottomSheetViewModel
    private var _binding: GeneratePlanBottomSheetFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var selectorFitness : SwipeSelector
    private lateinit var selectorGoal : SwipeSelector
    private lateinit var restSelector : SwipeSelector
    private lateinit var generatePlanButton : CardView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GeneratePlanBottomSheetFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        selectorFitness = root.findViewById(R.id.fitnessLevelSelectorGenerate)
        selectorGoal = root.findViewById(R.id.goalSelectorGenerate)
        restSelector = root.findViewById(R.id.restSelectorGenerate)
        generatePlanButton = root.findViewById(R.id.generatePlanButton2)





        selectorFitness.setItems( // The first argument is the value for that item, and should in most cases be unique for the
            // current SwipeSelector, just as you would assign values to radio buttons.
            // You can use the value later on to check what the selected item was.
            // The value can be any Object, here we're using ints.
            SwipeItem(0, "Iniciante", "Nunca treinou antes, sendo esta a sua primeira interação com a área."),
            SwipeItem(1, "Intermediário", "Já frequentou um ginásio ao longo da sua vida."),
            SwipeItem(2, "Experiente", "Frequenta com regularidade o ginásio, sendo experiente na área."),
            SwipeItem(3, "Élite", "Frequenta assiduamente o ginásio, estando completamente familiarizado com a área")
        )
        selectorGoal.setItems( // The first argument is the value for that item, and should in most cases be unique for the
            // current SwipeSelector, just as you would assign values to radio buttons.
            // You can use the value later on to check what the selected item was.
            // The value can be any Object, here we're using ints.
            SwipeItem(0, "Perder gordura e manter massa magra", "Perder massa gorda, retendo o máximo de massa magra possivel."),
            SwipeItem(1, "Perder peso no geral", "Deseja perder peso, mesmo que isso implique a perda de massa magra"),
            SwipeItem(2, "Hipertrofia", "Aumentar a quantidade de massa magra."),
            SwipeItem(3, "Ganhar força", "Tem como principal objetivo o ganho de força")
        )
        restSelector.setItems( // The first argument is the value for that item, and should in most cases be unique for the
            // current SwipeSelector, just as you would assign values to radio buttons.
            // You can use the value later on to check what the selected item was.
            // The value can be any Object, here we're using ints.
            SwipeItem(0, "1 dia de descanso", "Incluir 1 dia de descanso ao domingo"),
            SwipeItem(1, "2 dias de descanso", "Incluir 2 dia de descanso, um ao domingo e outro a meio da semana"),
            SwipeItem(2, "3 dias de descanso", "Incluir 3 dias de descanso, em dias intercalados"),
            SwipeItem(3, "4 dias de descanso", "Tem como principal objetivo o ganho de força")
        )
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(GeneratePlanBottomSheetViewModel::class.java)
        // TODO: Use the ViewModel
    }

}