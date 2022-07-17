package com.doublej.jjfitnessapp.ui.fragments

import com.doublej.jjfitnessapp.ui.recycleradapters.nutrition.NutritionRecyclerAdapter
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.databinding.NutritionFragmentBinding
import com.github.ybq.android.spinkit.SpinKitView
import com.google.firebase.firestore.*
import android.util.TypedValue
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.DialogFragment
import com.doublej.jjfitnessapp.ui.models.nutrition.Foods
import com.doublej.jjfitnessapp.ui.models.nutrition.PlanFoodMonday


class NutritionFragment : Fragment() {

    companion object {
        fun newInstance() = NutritionFragment()
    }

    private lateinit var viewModel: NutritionViewModel
    private var _binding: NutritionFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var query : Query
    private lateinit var filterLayout : LinearLayout
    private lateinit var contentFilterLayout : ConstraintLayout
    private lateinit var headerFilterLayout : ConstraintLayout
    private lateinit var allLayout : ConstraintLayout
    private lateinit var vegetarianLayout : ConstraintLayout
    private lateinit var veganLayout : ConstraintLayout
    private lateinit var chosenFilterImage : ImageView
    private lateinit var chosenFilterText : TextView
    private lateinit var nutritionRecyclerView : RecyclerView
    private lateinit var nutritionRecyclerAdapter : NutritionRecyclerAdapter
    private lateinit var foods : ArrayList<Foods>
    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var spinkit : SpinKitView
    private lateinit var createPlanNutrition : CardView
    private lateinit var searchPlanNutrition : CardView
    private lateinit var mondayCardNutrition : CardView
    private lateinit var tuesdayCardNutrition : CardView
    private lateinit var wednesdayCardNutrition : CardView
    private lateinit var thursdayCardNutrition : CardView
    private lateinit var fridayCardNutrition : CardView
    private lateinit var saturdayCardNutrition : CardView
    private lateinit var sundayCardNutrition : CardView
    private lateinit var mondayHolderNutrition : CardView
    private lateinit var fireBaseFirestore : FirebaseFirestore
    private lateinit var tuesdayHolderNutrition : CardView
    private lateinit var wednesdayHolderNutrition : CardView
    private lateinit var thursdayHolderNutrition : CardView
    private lateinit var fridayHolderNutrition : CardView
    private lateinit var saturdayHolderNutrition : CardView
    private lateinit var sundayHolderNutrition : CardView
    private lateinit var mondayCardText : TextView
    private lateinit var tuesdayCardText : TextView
    private lateinit var wednesdayCardText : TextView
    private lateinit var thursdayCardText : TextView
    private lateinit var fridayCardText : TextView
    private lateinit var saturdayCardText : TextView
    private lateinit var sundayCardText : TextView
    private lateinit var mondayNumber : TextView
    private lateinit var tuesdayNumber : TextView
    private lateinit var wednesdayNumber : TextView
    private lateinit var thursdayNumber : TextView
    private lateinit var fridayNumber : TextView
    private lateinit var saturdayNumber : TextView
    private lateinit var sundayNumber : TextView
    private lateinit var planFoods : ArrayList<PlanFoodMonday>
    private lateinit var mondayCardConstraint : ConstraintLayout
    private lateinit var tuesdayCardConstraint : ConstraintLayout
    private lateinit var wednesdayCardConstraint : ConstraintLayout
    private lateinit var thursdayCardConstraint : ConstraintLayout
    private lateinit var fridayCardConstraint : ConstraintLayout
    private lateinit var saturdayCardConstraint : ConstraintLayout
    private lateinit var sundayCardConstraint : ConstraintLayout
    private lateinit var dayOfWeekText : TextView
    private var dayOfWeek : String = "Segunda"
    private lateinit var mondayFoods : ArrayList<String>
    private lateinit var tuesdayFoods : ArrayList<String>
    private lateinit var wednesdayFoods : ArrayList<String>
    private lateinit var thursdayFoods : ArrayList<String>
    private lateinit var fridayFoods : ArrayList<String>
    private lateinit var saturdayFoods : ArrayList<String>
    private lateinit var sundayFoods : ArrayList<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = NutritionFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root

        foods = arrayListOf()
        nutritionRecyclerAdapter = NutritionRecyclerAdapter(foods)
        nutritionRecyclerAdapter.setHasStableIds(true)
        nutritionRecyclerAdapter.submitList(foods)

        planFoods = arrayListOf()
        mondayFoods = arrayListOf()
        tuesdayFoods = arrayListOf()
        wednesdayFoods = arrayListOf()
        thursdayFoods = arrayListOf()
        fridayFoods = arrayListOf()
        saturdayFoods = arrayListOf()
        sundayFoods = arrayListOf()
        fireBaseFirestore = FirebaseFirestore.getInstance()
        createPlanNutrition = root.findViewById(R.id.createPlanCardNutrition)
        searchPlanNutrition = root.findViewById(R.id.searchPlanCardNutrition)
        mondayNumber = root.findViewById(R.id.mondayNumberNutrition)
        tuesdayNumber = root.findViewById(R.id.tuesdayNumberNutrition)
        wednesdayNumber = root.findViewById(R.id.wednesdayNumberNutrition)
        thursdayNumber = root.findViewById(R.id.thursdayNumberNutrition)
        fridayNumber = root.findViewById(R.id.fridayNumberNutrition)
        saturdayNumber = root.findViewById(R.id.saturdayNumberNutrition)
        sundayNumber = root.findViewById(R.id.sundayNumberNutrition)

        mondayCardNutrition = root.findViewById(R.id.mondayCardNutrition)
        tuesdayCardNutrition = root.findViewById(R.id.tuesdayCardNutrition)
        wednesdayCardNutrition = root.findViewById(R.id.wednesdayCardNutrition)
        thursdayCardNutrition = root.findViewById(R.id.thursdayCardNutrition)
        fridayCardNutrition = root.findViewById(R.id.fridayCardNutrition)
        saturdayCardNutrition = root.findViewById(R.id.saturdayCardNutrition)
        sundayCardNutrition = root.findViewById(R.id.sundayCardNutrition)
        mondayHolderNutrition = root.findViewById(R.id.mondayHolderNutrition)
        tuesdayHolderNutrition = root.findViewById(R.id.tuesdayHolderNutrition)
        wednesdayHolderNutrition = root.findViewById(R.id.wednesdayHolderNutrition)
        thursdayHolderNutrition = root.findViewById(R.id.thursdayHolderNutrition)
        fridayHolderNutrition = root.findViewById(R.id.fridayHolderNutrition)
        saturdayHolderNutrition = root.findViewById(R.id.saturdayHolderNutrition)
        sundayHolderNutrition = root.findViewById(R.id.sundayHolderNutrition)
        mondayCardText = root.findViewById(R.id.mondayCardText)
        tuesdayCardText = root.findViewById(R.id.tuesdayCardText)
        wednesdayCardText = root.findViewById(R.id.wednesdayCardText)
        thursdayCardText = root.findViewById(R.id.thursdayCardText)
        fridayCardText = root.findViewById(R.id.fridayCardText)
        saturdayCardText = root.findViewById(R.id.saturdayCardText)
        sundayCardText = root.findViewById(R.id.sundayCardText)
        mondayCardConstraint = root.findViewById(R.id.mondayCardNutritionConstraint)
        tuesdayCardConstraint = root.findViewById(R.id.tuesdayCardNutritionConstraint)
        wednesdayCardConstraint = root.findViewById(R.id.wednesdayCardNutritionConstraint)
        thursdayCardConstraint = root.findViewById(R.id.thursdayCardNutritionConstraint)
        fridayCardConstraint = root.findViewById(R.id.fridayCardNutritionConstraint)
        saturdayCardConstraint = root.findViewById(R.id.saturdayCardNutritionConstraint)
        sundayCardConstraint = root.findViewById(R.id.sundayCardNutritionConstraint)
        dayOfWeekText = root.findViewById(R.id.dayOfWeekText)
        dayOfWeekText.text = "Segunda-feira"


        searchPlanNutrition.setOnClickListener {
            val bottomSheet = SearchNutritionPlanBottomSheetFragment()
            bottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           )
            bottomSheet.show(childFragmentManager,"SearchNutritionSheet")
        }

        createPlanNutrition.setOnClickListener {
            val foodsRef = fireBaseFirestore.collection("Pratos")
            val bundle = Bundle()
            bundle.putStringArrayList("mondayFoods", mondayFoods)
            bundle.putStringArrayList("tuesdayFoods", tuesdayFoods)
            bundle.putStringArrayList("wednesdayFoods", wednesdayFoods)
            bundle.putStringArrayList("thursdayFoods", thursdayFoods)
            bundle.putStringArrayList("fridayFoods", fridayFoods)
            bundle.putStringArrayList("saturdayFoods", saturdayFoods)
            bundle.putStringArrayList("sundayFoods", sundayFoods)
            val bottomSheet = BottomSheetNutritionFragment()
            bottomSheet.setArguments(bundle)
            bottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           )
            bottomSheet.show(childFragmentManager,"foodSheet")

            for (i in mondayFoods.size -1 downTo 0)
            {
                query = foodsRef.whereEqualTo("nomePrato",mondayFoods[i])
                query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        spinkit.alpha = 1f
                        nutritionRecyclerView.alpha = 0f

                        if( error != null){
                            Log.d("error", "error")
                            return
                        }

                        for(dc : DocumentChange in value?.documentChanges!!){

                            val tempFoods = PlanFoodMonday(dc.document["nomePrato"].toString(),dc.document.getLong("Calorias"),dc.document.getLong("Carbohidratos"),dc.document.getLong("Proteinas"),dc.document.getLong("Gorduras"),dc.document.getLong("id"), "Segunda")
                            planFoods.add(tempFoods)
                            spinkit.alpha = 0f
                            nutritionRecyclerView.alpha = 1f


                        }

                    }

                })
            }
            for (i in tuesdayFoods.size -1 downTo 0)
            {
                query = foodsRef.whereEqualTo("nomePrato",tuesdayFoods[i])
                query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        spinkit.alpha = 1f
                        nutritionRecyclerView.alpha = 0f

                        if( error != null){
                            Log.d("error", "error")
                            return
                        }

                        for(dc : DocumentChange in value?.documentChanges!!){

                            val tempFoods = PlanFoodMonday(dc.document["nomePrato"].toString(),dc.document.getLong("Calorias"),dc.document.getLong("Carbohidratos"),dc.document.getLong("Proteinas"),dc.document.getLong("Gorduras"),dc.document.getLong("id"), "Segunda")
                            planFoods.add(tempFoods)
                            spinkit.alpha = 0f
                            nutritionRecyclerView.alpha = 1f


                        }

                    }

                })
            }

            for (i in wednesdayFoods.size -1 downTo 0)
            {
                query = foodsRef.whereEqualTo("nomePrato",wednesdayFoods[i])
                query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        spinkit.alpha = 1f
                        nutritionRecyclerView.alpha = 0f

                        if( error != null){
                            Log.d("error", "error")
                            return
                        }

                        for(dc : DocumentChange in value?.documentChanges!!){

                            val tempFoods = PlanFoodMonday(dc.document["nomePrato"].toString(),dc.document.getLong("Calorias"),dc.document.getLong("Carbohidratos"),dc.document.getLong("Proteinas"),dc.document.getLong("Gorduras"),dc.document.getLong("id"), "Segunda")
                            planFoods.add(tempFoods)
                            spinkit.alpha = 0f
                            nutritionRecyclerView.alpha = 1f


                        }

                    }

                })
            }
            for (i in thursdayFoods.size -1 downTo 0)
            {
                query = foodsRef.whereEqualTo("nomePrato",thursdayFoods[i])
                query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        spinkit.alpha = 1f
                        nutritionRecyclerView.alpha = 0f

                        if( error != null){
                            Log.d("error", "error")
                            return
                        }

                        for(dc : DocumentChange in value?.documentChanges!!){

                            val tempFoods = PlanFoodMonday(dc.document["nomePrato"].toString(),dc.document.getLong("Calorias"),dc.document.getLong("Carbohidratos"),dc.document.getLong("Proteinas"),dc.document.getLong("Gorduras"),dc.document.getLong("id"), "Segunda")
                            planFoods.add(tempFoods)
                            spinkit.alpha = 0f
                            nutritionRecyclerView.alpha = 1f


                        }

                    }

                })
            }
            for (i in fridayFoods.size -1 downTo 0)
            {
                query = foodsRef.whereEqualTo("nomePrato",fridayFoods[i])
                query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        spinkit.alpha = 1f
                        nutritionRecyclerView.alpha = 0f

                        if( error != null){
                            Log.d("error", "error")
                            return
                        }

                        for(dc : DocumentChange in value?.documentChanges!!){

                            val tempFoods = PlanFoodMonday(dc.document["nomePrato"].toString(),dc.document.getLong("Calorias"),dc.document.getLong("Carbohidratos"),dc.document.getLong("Proteinas"),dc.document.getLong("Gorduras"),dc.document.getLong("id"), "Segunda")
                            planFoods.add(tempFoods)
                            spinkit.alpha = 0f
                            nutritionRecyclerView.alpha = 1f


                        }

                    }

                })
            }
            for (i in saturdayFoods.size -1 downTo 0)
            {
                query = foodsRef.whereEqualTo("nomePrato",saturdayFoods[i])
                query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        spinkit.alpha = 1f
                        nutritionRecyclerView.alpha = 0f

                        if( error != null){
                            Log.d("error", "error")
                            return
                        }

                        for(dc : DocumentChange in value?.documentChanges!!){

                            val tempFoods = PlanFoodMonday(dc.document["nomePrato"].toString(),dc.document.getLong("Calorias"),dc.document.getLong("Carbohidratos"),dc.document.getLong("Proteinas"),dc.document.getLong("Gorduras"),dc.document.getLong("id"), "Segunda")
                            planFoods.add(tempFoods)
                            spinkit.alpha = 0f
                            nutritionRecyclerView.alpha = 1f


                        }

                    }

                })
            }
            for (i in sundayFoods.size -1 downTo 0)
            {
                query = foodsRef.whereEqualTo("nomePrato",sundayFoods[i])
                query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        spinkit.alpha = 1f
                        nutritionRecyclerView.alpha = 0f

                        if( error != null){
                            Log.d("error", "error")
                            return
                        }

                        for(dc : DocumentChange in value?.documentChanges!!){

                            val tempFoods = PlanFoodMonday(dc.document["nomePrato"].toString(),dc.document.getLong("Calorias"),dc.document.getLong("Carbohidratos"),dc.document.getLong("Proteinas"),dc.document.getLong("Gorduras"),dc.document.getLong("id"), "Segunda")
                            planFoods.add(tempFoods)
                            spinkit.alpha = 0f
                            nutritionRecyclerView.alpha = 1f


                        }

                    }

                })
            }
        }









        mondayCardNutrition.setOnClickListener {
            dayOfWeek = "Segunda"
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = 1000
            val mondayCardHolderparams = mondayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            mondayCardHolderparams.bottomToBottom = mondayCardConstraint.id
            mondayHolderNutrition.requestLayout()
            val tuesdayCardHolderparams = tuesdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            tuesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            tuesdayHolderNutrition.requestLayout()
            val wednesdayCardHolderparams = wednesdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            wednesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            wednesdayHolderNutrition.requestLayout()
            val thursdayCardHolderparams = thursdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            thursdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            thursdayHolderNutrition.requestLayout()
            val fridayCardHolderparams = fridayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            fridayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            fridayHolderNutrition.requestLayout()
            val saturdayCardHolderparams = saturdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            saturdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            saturdayHolderNutrition.requestLayout()
            val sundayCardHolderparams = sundayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            sundayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            sundayHolderNutrition.requestLayout()
            mondayHolderNutrition.cardElevation = 10f
            tuesdayHolderNutrition.cardElevation = 0f
            wednesdayHolderNutrition.cardElevation = 0f
            thursdayHolderNutrition.cardElevation = 0f
            fridayHolderNutrition.cardElevation = 0f
            saturdayHolderNutrition.cardElevation = 0f
            sundayHolderNutrition.cardElevation = 0f
            val animationShowMonday = AnimationSet(false)
            val animationHide = AnimationSet(false)
            animationShowMonday.addAnimation(fadeIn)
            mondayCardNutrition.setAnimation(animationShowMonday)
            dayOfWeekText.text = "Segunda-feira"
            dayOfWeekText.setAnimation(animationShowMonday)
            mondayHolderNutrition.setAnimation(animationShowMonday)
            mondayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.lightwhite))
            mondayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.neonblue))
            tuesdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))

            TransitionManager.beginDelayedTransition(sundayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(mondayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(tuesdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(wednesdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(thursdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(fridayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(saturdayCardNutrition,AutoTransition())
        }
        tuesdayCardNutrition.setOnClickListener {
            dayOfWeek = "Terça"
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = 1000
            val mondayCardHolderparams = mondayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            mondayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            mondayHolderNutrition.requestLayout()
            val tuesdayCardHolderparams = tuesdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            tuesdayCardHolderparams.bottomToBottom = tuesdayCardConstraint.id
            tuesdayHolderNutrition.requestLayout()
            val wednesdayCardHolderparams = wednesdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            wednesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            wednesdayHolderNutrition.requestLayout()
            val thursdayCardHolderparams = thursdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            thursdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            thursdayHolderNutrition.requestLayout()
            val fridayCardHolderparams = fridayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            fridayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            fridayHolderNutrition.requestLayout()
            val saturdayCardHolderparams = saturdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            saturdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            saturdayHolderNutrition.requestLayout()
            val sundayCardHolderparams = sundayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            sundayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            sundayHolderNutrition.requestLayout()
            mondayHolderNutrition.cardElevation = 0f
            tuesdayHolderNutrition.cardElevation = 10f
            wednesdayHolderNutrition.cardElevation = 0f
            thursdayHolderNutrition.cardElevation = 0f
            fridayHolderNutrition.cardElevation = 0f
            saturdayHolderNutrition.cardElevation = 0f
            sundayHolderNutrition.cardElevation = 0f
            val animationShowTuesday = AnimationSet(false)
            animationShowTuesday.addAnimation(fadeIn)
            tuesdayCardNutrition.setAnimation(animationShowTuesday)
            tuesdayHolderNutrition.setAnimation(animationShowTuesday)
            dayOfWeekText.text = "Terça-feira"
            dayOfWeekText.setAnimation(animationShowTuesday)
            mondayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            mondayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.lightwhite))
            tuesdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.neonblue))
            wednesdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            TransitionManager.beginDelayedTransition(sundayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(mondayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(tuesdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(wednesdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(thursdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(fridayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(saturdayCardNutrition,AutoTransition())
        }
        wednesdayCardNutrition.setOnClickListener {
            dayOfWeek = "Quarta"
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = 1000
            val mondayCardHolderparams = mondayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            mondayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            mondayHolderNutrition.requestLayout()
            val tuesdayCardHolderparams = tuesdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            tuesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            tuesdayHolderNutrition.requestLayout()
            val wednesdayCardHolderparams = wednesdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            wednesdayCardHolderparams.bottomToBottom = wednesdayCardConstraint.id
            wednesdayHolderNutrition.requestLayout()
            val thursdayCardHolderparams = thursdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            thursdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            thursdayHolderNutrition.requestLayout()
            val fridayCardHolderparams = fridayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            fridayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            fridayHolderNutrition.requestLayout()
            val saturdayCardHolderparams = saturdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            saturdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            saturdayHolderNutrition.requestLayout()
            mondayHolderNutrition.cardElevation = 0f
            tuesdayHolderNutrition.cardElevation = 0f
            wednesdayHolderNutrition.cardElevation = 10f
            thursdayHolderNutrition.cardElevation = 0f
            fridayHolderNutrition.cardElevation = 0f
            saturdayHolderNutrition.cardElevation = 0f
            sundayHolderNutrition.cardElevation = 0f
            val sundayCardHolderparams = sundayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            sundayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            sundayHolderNutrition.requestLayout()
            val animationShow = AnimationSet(false)
            val animationHide = AnimationSet(false)
            animationShow.addAnimation(fadeIn)
            wednesdayCardNutrition.setAnimation(animationShow)
            wednesdayHolderNutrition.setAnimation(animationShow)
            dayOfWeekText.text = "Quarta-feira"
            dayOfWeekText.setAnimation(animationShow)
            mondayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            mondayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.lightwhite))
            wednesdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.neonblue))
            thursdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            TransitionManager.beginDelayedTransition(sundayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(mondayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(tuesdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(wednesdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(thursdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(fridayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(saturdayCardNutrition,AutoTransition())
        }
        thursdayCardNutrition.setOnClickListener {
            dayOfWeek = "Quinta"
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = 1000
            val mondayCardHolderparams = mondayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            mondayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            mondayHolderNutrition.requestLayout()
            val tuesdayCardHolderparams = tuesdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            tuesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            tuesdayHolderNutrition.requestLayout()
            val wednesdayCardHolderparams = wednesdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            wednesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            wednesdayHolderNutrition.requestLayout()
            val thursdayCardHolderparams = thursdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            thursdayCardHolderparams.bottomToBottom = thursdayCardConstraint.id
            thursdayHolderNutrition.requestLayout()
            val fridayCardHolderparams = fridayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            fridayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            fridayHolderNutrition.requestLayout()
            val saturdayCardHolderparams = saturdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            saturdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            saturdayHolderNutrition.requestLayout()
            val sundayCardHolderparams = sundayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            sundayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            sundayHolderNutrition.requestLayout()
            mondayHolderNutrition.cardElevation = 0f
            tuesdayHolderNutrition.cardElevation = 0f
            wednesdayHolderNutrition.cardElevation = 0f
            thursdayHolderNutrition.cardElevation = 10f
            fridayHolderNutrition.cardElevation = 0f
            saturdayHolderNutrition.cardElevation = 0f
            sundayHolderNutrition.cardElevation = 0f
            val animationShow = AnimationSet(false)
            val animationHide = AnimationSet(false)
            animationShow.addAnimation(fadeIn)
            thursdayCardNutrition.setAnimation(animationShow)
            thursdayHolderNutrition.setAnimation(animationShow)
            dayOfWeekText.text = "Quinta-feira"
            dayOfWeekText.setAnimation(animationShow)
            mondayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            mondayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.lightwhite))
            thursdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.neonblue))
            fridayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))

            TransitionManager.beginDelayedTransition(sundayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(mondayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(tuesdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(wednesdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(thursdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(fridayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(saturdayCardNutrition,AutoTransition())
        }
        fridayCardNutrition.setOnClickListener {
            dayOfWeek = "Sexta"
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = 1000
            val mondayCardHolderparams = mondayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            mondayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            mondayHolderNutrition.requestLayout()
            val tuesdayCardHolderparams = tuesdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            tuesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            tuesdayHolderNutrition.requestLayout()
            val wednesdayCardHolderparams = wednesdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            wednesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            wednesdayHolderNutrition.requestLayout()
            val thursdayCardHolderparams = thursdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            thursdayCardHolderparams.bottomToBottom =  ConstraintLayout.LayoutParams.UNSET
            thursdayHolderNutrition.requestLayout()
            val fridayCardHolderparams = fridayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            fridayCardHolderparams.bottomToBottom = fridayCardConstraint.id
            fridayHolderNutrition.requestLayout()
            val saturdayCardHolderparams = saturdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            saturdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            saturdayHolderNutrition.requestLayout()
            val sundayCardHolderparams = sundayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            sundayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            sundayHolderNutrition.requestLayout()
            mondayHolderNutrition.cardElevation = 0f
            tuesdayHolderNutrition.cardElevation = 0f
            wednesdayHolderNutrition.cardElevation = 0f
            thursdayHolderNutrition.cardElevation = 0f
            fridayHolderNutrition.cardElevation = 10f
            saturdayHolderNutrition.cardElevation = 0f
            sundayHolderNutrition.cardElevation = 0f
            val animationShow = AnimationSet(false)
            val animationHide = AnimationSet(false)
            animationShow.addAnimation(fadeIn)
            fridayCardNutrition.setAnimation(animationShow)
            fridayHolderNutrition.setAnimation(animationShow)
            dayOfWeekText.text = "Sexta-feira"
            dayOfWeekText.setAnimation(animationShow)
            mondayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            mondayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.lightwhite))
            fridayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.neonblue))
            saturdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            TransitionManager.beginDelayedTransition(sundayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(mondayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(tuesdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(wednesdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(thursdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(fridayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(saturdayCardNutrition,AutoTransition())
        }
        saturdayCardNutrition.setOnClickListener {
            dayOfWeek = "Sábado"
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = 1000
            val mondayCardHolderparams = mondayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            mondayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            mondayHolderNutrition.requestLayout()
            val tuesdayCardHolderparams = tuesdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            tuesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            tuesdayHolderNutrition.requestLayout()
            val wednesdayCardHolderparams = wednesdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            wednesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            wednesdayHolderNutrition.requestLayout()
            val thursdayCardHolderparams = thursdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            thursdayCardHolderparams.bottomToBottom =  ConstraintLayout.LayoutParams.UNSET
            thursdayHolderNutrition.requestLayout()
            val fridayCardHolderparams = fridayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            fridayCardHolderparams.bottomToBottom =  ConstraintLayout.LayoutParams.UNSET
            fridayHolderNutrition.requestLayout()
            val saturdayCardHolderparams = saturdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            saturdayCardHolderparams.bottomToBottom = saturdayCardConstraint.id
            mondayHolderNutrition.cardElevation = 0f
            tuesdayHolderNutrition.cardElevation = 0f
            wednesdayHolderNutrition.cardElevation = 0f
            thursdayHolderNutrition.cardElevation = 0f
            fridayHolderNutrition.cardElevation = 0f
            saturdayHolderNutrition.cardElevation = 10f
            sundayHolderNutrition.cardElevation = 0f
            saturdayHolderNutrition.requestLayout()
            val sundayCardHolderparams = sundayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            sundayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            sundayHolderNutrition.requestLayout()
            val animationShow = AnimationSet(false)
            val animationHide = AnimationSet(false)
            animationShow.addAnimation(fadeIn)
            saturdayCardNutrition.setAnimation(animationShow)
            saturdayHolderNutrition.setAnimation(animationShow)
            dayOfWeekText.text = "Sábado"
            dayOfWeekText.setAnimation(animationShow)
            mondayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            mondayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.lightwhite))
            saturdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.neonblue))
            sundayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            TransitionManager.beginDelayedTransition(sundayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(mondayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(tuesdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(wednesdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(thursdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(fridayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(saturdayCardNutrition,AutoTransition())
        }
        sundayCardNutrition.setOnClickListener {
            dayOfWeek = "Domingo"
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = 1000
            val mondayCardHolderparams = mondayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            mondayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            mondayHolderNutrition.requestLayout()
            val tuesdayCardHolderparams = tuesdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            tuesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            tuesdayHolderNutrition.requestLayout()
            val wednesdayCardHolderparams = wednesdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            wednesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            wednesdayHolderNutrition.requestLayout()
            val thursdayCardHolderparams = thursdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            thursdayCardHolderparams.bottomToBottom =  ConstraintLayout.LayoutParams.UNSET
            thursdayHolderNutrition.requestLayout()
            val fridayCardHolderparams = fridayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            fridayCardHolderparams.bottomToBottom =  ConstraintLayout.LayoutParams.UNSET
            fridayHolderNutrition.requestLayout()
            val saturdayCardHolderparams = saturdayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            saturdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            saturdayHolderNutrition.requestLayout()
            val sundayCardHolderparams = sundayHolderNutrition.getLayoutParams() as ConstraintLayout.LayoutParams
            sundayCardHolderparams.bottomToBottom = sundayCardConstraint.id
            sundayHolderNutrition.requestLayout()
            mondayHolderNutrition.cardElevation = 0f
            tuesdayHolderNutrition.cardElevation = 0f
            wednesdayHolderNutrition.cardElevation = 0f
            thursdayHolderNutrition.cardElevation = 0f
            fridayHolderNutrition.cardElevation = 0f
            saturdayHolderNutrition.cardElevation = 0f
            sundayHolderNutrition.cardElevation = 10f
            val animationShow = AnimationSet(false)
            val animationHide = AnimationSet(false)
            animationShow.addAnimation(fadeIn)
            sundayCardNutrition.setAnimation(animationShow)
            sundayHolderNutrition.setAnimation(animationShow)
            dayOfWeekText.text = "Domingo"
            dayOfWeekText.setAnimation(animationShow)
            mondayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            mondayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayCardNutrition.setCardBackgroundColor(resources.getColor(R.color.lightwhite))
            sundayHolderNutrition.setCardBackgroundColor(resources.getColor(R.color.neonblue))

            TransitionManager.beginDelayedTransition(sundayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(mondayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(tuesdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(wednesdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(thursdayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(fridayCardNutrition,AutoTransition())
            TransitionManager.beginDelayedTransition(saturdayCardNutrition,AutoTransition())
        }
        filterLayout = root.findViewById(R.id.nutritionFilterLayout)
        contentFilterLayout = root.findViewById(R.id.nutritionContentFilterLayout)
        headerFilterLayout = root.findViewById(R.id.nutritionHeaderFilterConstraint)
        allLayout = root.findViewById(R.id.allLayout)
        vegetarianLayout = root.findViewById(R.id.vegetarianLayout)
        veganLayout = root.findViewById(R.id.veganLayout)
        filterLayout = root.findViewById(R.id.nutritionFilterLayout)
        chosenFilterImage = root.findViewById(R.id.nutritionChosenFilterImage)
        chosenFilterText = root.findViewById(R.id.nutritionChosenFilterText)
        nutritionRecyclerView = root.findViewById(R.id.recyclerViewNutrition)
        foods = arrayListOf()
        nutritionRecyclerView.layoutManager = LinearLayoutManager(this.context)
        nutritionRecyclerAdapter = NutritionRecyclerAdapter(foods)
        nutritionRecyclerAdapter.setHasStableIds(true)
        nutritionRecyclerAdapter.submitList(foods)
        nutritionRecyclerView.adapter = nutritionRecyclerAdapter
        nutritionRecyclerView.alpha = 1f
        nutritionRecyclerView.setHasFixedSize(true)
        spinkit = root.findViewById(R.id.spin_kitNutrition)
        firebaseFirestore = FirebaseFirestore.getInstance()

         chosenFilterText.text = "Tudo"
            chosenFilterImage.setImageDrawable(resources.getDrawable(R.drawable.chicken64))
            allLayout.setBackgroundColor(resources.getColor(R.color.lightneonblue))
            vegetarianLayout.setBackgroundColor(resources.getColor(R.color.white))
            veganLayout.setBackgroundColor(resources.getColor(R.color.white))
            contentFilterLayout.visibility = View.GONE
            TransitionManager.beginDelayedTransition(filterLayout, AutoTransition())
            foods.clear()
            spinkit.visibility = View.VISIBLE
            nutritionRecyclerAdapter.notifyDataSetChanged()
            val foodsRef = firebaseFirestore.collection("Pratos")
            query = foodsRef
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    //spinKit.alpha = 1f
                    nutritionRecyclerView.alpha = 0f

                    if( error != null){
                        Log.d("error", "error")
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){

                        if(dc.type == DocumentChange.Type.ADDED){
                            foods.add(dc.document.toObject(Foods::class.java))
                            spinkit.visibility = View.GONE
                            nutritionRecyclerView.alpha = 1f
                        }
                    }
                    nutritionRecyclerAdapter.notifyDataSetChanged()
                }

            })



        headerFilterLayout.setOnClickListener {
            if(contentFilterLayout.visibility == View.GONE){
                contentFilterLayout.visibility = View.VISIBLE
            }else{
                contentFilterLayout.visibility = View.GONE
            }
            TransitionManager.beginDelayedTransition(filterLayout, AutoTransition())

        }
        allLayout.setOnClickListener{
            chosenFilterText.text = "Sem restrições"
            chosenFilterImage.setImageDrawable(resources.getDrawable(R.drawable.chicken64))
            allLayout.setBackgroundColor(resources.getColor(R.color.lightneonblue))
            vegetarianLayout.setBackgroundColor(resources.getColor(R.color.white))
            veganLayout.setBackgroundColor(resources.getColor(R.color.white))
            contentFilterLayout.visibility = View.GONE
            TransitionManager.beginDelayedTransition(filterLayout, AutoTransition())
            foods.clear()
            spinkit.visibility = View.VISIBLE
            nutritionRecyclerAdapter.notifyDataSetChanged()
            val foodsRef = firebaseFirestore.collection("Pratos")
            query = foodsRef
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    //spinKit.alpha = 1f
                    nutritionRecyclerView.alpha = 0f

                    if( error != null){
                        Log.d("error", "error")
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){

                        if(dc.type == DocumentChange.Type.ADDED){
                            foods.add(dc.document.toObject(Foods::class.java))
                            spinkit.visibility = View.GONE
                            nutritionRecyclerView.alpha = 1f
                        }
                    }
                    nutritionRecyclerAdapter.notifyDataSetChanged()
                }

            })



        }
        vegetarianLayout.setOnClickListener{
            chosenFilterText.text = "Vegetariana"
            chosenFilterImage.setImageDrawable(resources.getDrawable(R.drawable.eggplant64))
            allLayout.setBackgroundColor(resources.getColor(R.color.white))
            vegetarianLayout.setBackgroundColor(resources.getColor(R.color.lightneonblue))
            veganLayout.setBackgroundColor(resources.getColor(R.color.white))
            contentFilterLayout.visibility = View.GONE
            TransitionManager.beginDelayedTransition(filterLayout, AutoTransition())
            foods.clear()
            spinkit.visibility = View.VISIBLE
            nutritionRecyclerAdapter.notifyDataSetChanged()
            val foodsRef = firebaseFirestore.collection("Pratos")
            query = foodsRef.whereEqualTo("Dieta","Vegetariana")
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    //spinKit.alpha = 1f
                    nutritionRecyclerView.alpha = 0f

                    if( error != null){
                        Log.d("error", "error")
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){

                        if(dc.type == DocumentChange.Type.ADDED){
                            foods.add(dc.document.toObject(Foods::class.java))
                            //spinKit.alpha = 0f
                            spinkit.visibility = View.GONE
                            nutritionRecyclerView.alpha = 1f
                        }
                    }
                    nutritionRecyclerAdapter.notifyDataSetChanged()
                }

            })

        }
        veganLayout.setOnClickListener{
            chosenFilterText.text = "Vegan"
            chosenFilterImage.setImageDrawable(resources.getDrawable(R.drawable.leaf64))
            allLayout.setBackgroundColor(resources.getColor(R.color.white))
            vegetarianLayout.setBackgroundColor(resources.getColor(R.color.white))
            veganLayout.setBackgroundColor(resources.getColor(R.color.lightneonblue))
            contentFilterLayout.visibility = View.GONE
            TransitionManager.beginDelayedTransition(filterLayout, AutoTransition())
            foods.clear()
            spinkit.visibility = View.VISIBLE
            nutritionRecyclerAdapter.notifyDataSetChanged()
            val foodsRef = firebaseFirestore.collection("Pratos")
            query = foodsRef.whereEqualTo("Dieta","Vegan")
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    //spinKit.alpha = 1f
                    nutritionRecyclerView.alpha = 0f

                    if( error != null){
                        Log.d("error", "error")
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){

                        if(dc.type == DocumentChange.Type.ADDED){
                            foods.add(dc.document.toObject(Foods::class.java))
                            //spinKit.alpha = 0f
                            spinkit.visibility = View.GONE
                            nutritionRecyclerView.alpha = 1f
                        }
                    }
                    nutritionRecyclerAdapter.notifyDataSetChanged()
                }

            })

        }

        nutritionRecyclerAdapter.setOnItemClickListener(object : NutritionRecyclerAdapter.onExerciseClick{
            override fun onItemClick(title: CharSequence) {

                if(dayOfWeek == "Segunda"){
                    animateMondayHolder()
                    mondayFoods.add(title.toString())
                    mondayNumber.text = mondayFoods.size.toString()
                }
                if(dayOfWeek == "Terça"){
                    animateTuesdayHolder()
                    tuesdayFoods.add(title.toString())
                    tuesdayNumber.text = tuesdayFoods.size.toString()
                }
                if(dayOfWeek == "Quarta"){
                    animateWednesdayHolder()
                    wednesdayFoods.add(title.toString())
                    wednesdayNumber.text = wednesdayFoods.size.toString()
                }
                if(dayOfWeek == "Quinta"){
                    animateThursdayHolder()
                    thursdayFoods.add(title.toString())

                    thursdayNumber.text = thursdayFoods.size.toString()
                }
                if(dayOfWeek == "Sexta"){
                    animateFridayHolder()
                    fridayFoods.add(title.toString())
                    fridayNumber.text = fridayFoods.size.toString()
                }
                if(dayOfWeek == "Sábado"){
                    animateSaturdayHolder()
                    saturdayFoods.add(title.toString())
                    saturdayNumber.text = saturdayFoods.size.toString()
                }
                if(dayOfWeek == "Domingo"){
                    animateSundayHolder()
                    sundayFoods.add(title.toString())
                    sundayNumber.text = sundayFoods.size.toString()
                }

            }

        })



        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(NutritionViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun Float.toDips() =
        TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this, resources.displayMetrics);


    fun animateMondayHolder(){
        var isIncreasing = true
        var widthValue = 35f
        var isAnimating = true
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if(isAnimating == true) {
                    val mondayHolderParams = mondayHolderNutrition.layoutParams
                    mondayHolderNutrition.layoutParams = mondayHolderParams
                    mondayHolderNutrition.requestLayout()

                    if (widthValue < 50f && isIncreasing == true) {
                        widthValue += 1.5f
                    }
                    if (widthValue >= 50f && isIncreasing == true) {
                        widthValue = 50f
                        isIncreasing = false
                    }
                    if (isIncreasing == false) {
                        if (widthValue > 35f) {
                            widthValue -= 1.5f
                        }
                        if (widthValue <= 35f) {
                            widthValue = 35f
                            isAnimating = false

                        }

                    }
                    val scale = context!!.resources.displayMetrics.density
                    val pixels = (widthValue * scale + 0.5f)
                    val convertedWidth = (widthValue).toDips().toInt()
                    mondayHolderParams.width = convertedWidth



                    handler.postDelayed(this, 10)
                }
            }

        }, 0)

    }
    fun animateTuesdayHolder(){
        var isIncreasing = true
        var widthValue = 35f
        var isAnimating = true
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if(isAnimating == true) {
                    val tuesdayHolderParams = tuesdayHolderNutrition.layoutParams
                    tuesdayHolderNutrition.layoutParams = tuesdayHolderParams
                    tuesdayHolderNutrition.requestLayout()

                    if (widthValue < 50f && isIncreasing == true) {
                        widthValue += 1.5f
                    }
                    if (widthValue >= 50f && isIncreasing == true) {
                        widthValue = 50f
                        isIncreasing = false
                    }
                    if (isIncreasing == false) {
                        if (widthValue > 35f) {
                            widthValue -= 1.5f
                        }
                        if (widthValue <= 35f) {
                            widthValue = 35f
                            isAnimating = false

                        }

                    }
                    val scale = context!!.resources.displayMetrics.density
                    val pixels = (widthValue * scale + 0.5f)
                    val convertedWidth = (widthValue).toDips().toInt()
                    tuesdayHolderParams.width = convertedWidth



                    handler.postDelayed(this, 10)
                }
            }

        }, 0)

    }

    fun animateWednesdayHolder(){
        var isIncreasing = true
        var widthValue = 35f
        var isAnimating = true
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if(isAnimating == true) {
                    val wednesdayHolderParams = wednesdayHolderNutrition.layoutParams
                    wednesdayHolderNutrition.layoutParams = wednesdayHolderParams
                    wednesdayHolderNutrition.requestLayout()

                    if (widthValue < 50f && isIncreasing == true) {
                        widthValue += 1.5f
                    }
                    if (widthValue >= 50f && isIncreasing == true) {
                        widthValue = 50f
                        isIncreasing = false
                    }
                    if (isIncreasing == false) {
                        if (widthValue > 35f) {
                            widthValue -= 1.5f
                        }
                        if (widthValue <= 35f) {
                            widthValue = 35f
                            isAnimating = false

                        }

                    }
                    val scale = context!!.resources.displayMetrics.density
                    val pixels = (widthValue * scale + 0.5f)
                    val convertedWidth = (widthValue).toDips().toInt()
                    wednesdayHolderParams.width = convertedWidth



                    handler.postDelayed(this, 10)
                }
            }

        }, 0)

    }
    fun animateThursdayHolder(){
        var isIncreasing = true
        var widthValue = 35f
        var isAnimating = true
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if(isAnimating == true) {
                    val thursdayHolderParams = thursdayHolderNutrition.layoutParams
                    thursdayHolderNutrition.layoutParams = thursdayHolderParams
                    thursdayHolderNutrition.requestLayout()

                    if (widthValue < 50f && isIncreasing == true) {
                        widthValue += 1.5f
                    }
                    if (widthValue >= 50f && isIncreasing == true) {
                        widthValue = 50f
                        isIncreasing = false
                    }
                    if (isIncreasing == false) {
                        if (widthValue > 35f) {
                            widthValue -= 1.5f
                        }
                        if (widthValue <= 35f) {
                            widthValue = 35f
                            isAnimating = false

                        }

                    }
                    val scale = context!!.resources.displayMetrics.density
                    val pixels = (widthValue * scale + 0.5f)
                    val convertedWidth = (widthValue).toDips().toInt()
                    thursdayHolderParams.width = convertedWidth



                    handler.postDelayed(this, 10)
                }
            }

        }, 0)

    }

    fun animateFridayHolder(){
        var isIncreasing = true
        var widthValue = 35f
        var isAnimating = true
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if(isAnimating == true) {
                    val fridayHolderParams = fridayHolderNutrition.layoutParams
                    fridayHolderNutrition.layoutParams = fridayHolderParams
                    fridayHolderNutrition.requestLayout()

                    if (widthValue < 50f && isIncreasing == true) {
                        widthValue += 1.5f
                    }
                    if (widthValue >= 50f && isIncreasing == true) {
                        widthValue = 50f
                        isIncreasing = false
                    }
                    if (isIncreasing == false) {
                        if (widthValue > 35f) {
                            widthValue -= 1.5f
                        }
                        if (widthValue <= 35f) {
                            widthValue = 35f
                            isAnimating = false

                        }

                    }
                    val scale = context!!.resources.displayMetrics.density
                    val pixels = (widthValue * scale + 0.5f)
                    val convertedWidth = (widthValue).toDips().toInt()
                    fridayHolderParams.width = convertedWidth



                    handler.postDelayed(this, 10)
                }
            }

        }, 0)

    }

    fun animateSaturdayHolder(){
        var isIncreasing = true
        var widthValue = 35f
        var isAnimating = true
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if(isAnimating == true) {
                    val saturdayHolderParams = saturdayHolderNutrition.layoutParams
                    saturdayHolderNutrition.layoutParams = saturdayHolderParams
                    saturdayHolderNutrition.requestLayout()

                    if (widthValue < 50f && isIncreasing == true) {
                        widthValue += 1.5f
                    }
                    if (widthValue >= 50f && isIncreasing == true) {
                        widthValue = 50f
                        isIncreasing = false
                    }
                    if (isIncreasing == false) {
                        if (widthValue > 35f) {
                            widthValue -= 1.5f
                        }
                        if (widthValue <= 35f) {
                            widthValue = 35f
                            isAnimating = false

                        }

                    }
                    val scale = context!!.resources.displayMetrics.density
                    val pixels = (widthValue * scale + 0.5f)
                    val convertedWidth = (widthValue).toDips().toInt()
                    saturdayHolderParams.width = convertedWidth



                    handler.postDelayed(this, 10)
                }
            }

        }, 0)

    }

    fun animateSundayHolder(){
        var isIncreasing = true
        var widthValue = 35f
        var isAnimating = true
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if(isAnimating == true) {
                    val sundayHolderParams = sundayHolderNutrition.layoutParams
                    sundayHolderNutrition.layoutParams = sundayHolderParams
                    sundayHolderNutrition.requestLayout()

                    if (widthValue < 50f && isIncreasing == true) {
                        widthValue += 1.5f
                    }
                    if (widthValue >= 50f && isIncreasing == true) {
                        widthValue = 50f
                        isIncreasing = false
                    }
                    if (isIncreasing == false) {
                        if (widthValue > 35f) {
                            widthValue -= 1.5f
                        }
                        if (widthValue <= 35f) {
                            widthValue = 35f
                            isAnimating = false

                        }

                    }
                    val scale = context!!.resources.displayMetrics.density
                    val pixels = (widthValue * scale + 0.5f)
                    val convertedWidth = (widthValue).toDips().toInt()
                    sundayHolderParams.width = convertedWidth



                    handler.postDelayed(this, 10)
                }
            }

        }, 0)

    }




}