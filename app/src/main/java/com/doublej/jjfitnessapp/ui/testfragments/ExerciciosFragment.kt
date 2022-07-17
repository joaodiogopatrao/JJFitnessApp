package com.doublej.jjfitnessapp.ui.testfragments

import com.doublej.jjfitnessapp.ui.recycleradapters.exercises.RecyclerAdapter
import android.hardware.Sensor
import android.hardware.SensorManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.databinding.ExerciciosFragmentBinding
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.TypedValue
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.DecelerateInterpolator
import android.widget.*

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.charts.LineChart
import com.github.ybq.android.spinkit.SpinKitView
import com.google.android.material.progressindicator.CircularProgressIndicator
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.*
import com.google.firebase.firestore.ktx.firestoreSettings
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import com.doublej.jjfitnessapp.ui.dashboard.*
import com.doublej.jjfitnessapp.ui.fragments.BottomSheetFragment
import com.doublej.jjfitnessapp.ui.fragments.GeneratePlanBottomSheet
import com.doublej.jjfitnessapp.ui.fragments.SearchPlanBottomSheetFragment
import com.doublej.jjfitnessapp.ui.models.exercises.Exercises
import com.doublej.jjfitnessapp.ui.models.exercises.PlanExercisesMonday


class ExerciciosFragment : Fragment(), BottomSheetFragment.onExerciseAddListener {

    companion object {
        fun newInstance() = ExerciciosFragment()
    }

    private lateinit var viewModel: ExerciciosViewModel
    private var _binding: ExerciciosFragmentBinding? = null
    private val binding get() = _binding!!
    private var scoreList = ArrayList<Score>()
    private lateinit var db : FirebaseDatabase
    private lateinit var dashboardLineChart : LineChart
    private lateinit var fireBaseFirestore : FirebaseFirestore
    private lateinit var goalStepTest : TextView
    private lateinit var circularProgress : CircularProgressIndicator
    private lateinit var recyclerView : RecyclerView
    private lateinit var myAdapter : RecyclerAdapter
    private lateinit var exercises : ArrayList<Exercises>
    private lateinit var planExercises : ArrayList<PlanExercisesMonday>
    private var sensorManager : SensorManager? = null
    private var running = true
    private lateinit var stepSensor : Sensor
    private lateinit var upperCard : CardView
    private lateinit var lowerCard : CardView
    private lateinit var cardioCard : CardView
    private lateinit var yogaCard : CardView
    private lateinit var upperCardText : TextView
    private lateinit var lowerCardText : TextView
    private lateinit var cardioCardText : TextView
    private lateinit var upperLinearLayout : LinearLayout
    private lateinit var lowerLinearLayout : LinearLayout
    private lateinit var cardioLinearLayout : LinearLayout
    private lateinit var yogaLinearLayout : LinearLayout
    private var ACTIVITY_RECOGNITION_REQUEST_CODE : Int = 100
    private lateinit var stepsProgress : antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
    private lateinit var calProgress : antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
    private lateinit var kmProgress : antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator
    private lateinit var spinKit : SpinKitView
    private lateinit var filterLayout : LinearLayout
    private lateinit var contentFilterLayout : ConstraintLayout
    private lateinit var headerFilterLayout : ConstraintLayout
    private lateinit var chestLayout : ConstraintLayout
    private lateinit var armsLayout : ConstraintLayout
    private lateinit var shouldersLayout : ConstraintLayout
    private lateinit var backLayout : ConstraintLayout
    private lateinit var lowerBodyLayout : ConstraintLayout
    private lateinit var cardioLayout : ConstraintLayout
    private lateinit var chosenFilterText : TextView
    private lateinit var chosenFilterImage : ImageView
    private lateinit var filterArrow : ImageView
    private lateinit var query : Query
    private lateinit var mondayCard : CardView
    private lateinit var tuesdayCard : CardView
    private lateinit var wednesdayCard : CardView
    private lateinit var thursdayCard : CardView
    private lateinit var fridayCard : CardView
    private lateinit var saturdayCard : CardView
    private lateinit var sundayCard : CardView
    private var dayOfTheWeek : String = "Segunda"
    private lateinit var mondayExercises : ArrayList<String>
    private lateinit var tuesdayExercises : ArrayList<String>
    private lateinit var wednesdayExercises : ArrayList<String>
    private lateinit var thursdayExercises : ArrayList<String>
    private lateinit var fridayExercises : ArrayList<String>
    private lateinit var saturdayExercises : ArrayList<String>
    private lateinit var sundayExercises : ArrayList<String>
    private lateinit var mondayNumber : TextView
    private lateinit var tuesdayNumber : TextView
    private lateinit var wednesdayNumber : TextView
    private lateinit var thursdayNumber : TextView
    private lateinit var fridayNumber : TextView
    private lateinit var saturdayNumber : TextView
    private lateinit var sundayNumber : TextView
    private lateinit var createPlanButton : CardView
    private lateinit var searchPlanButton : CardView
    private lateinit var generatePlanButton : CardView
    private lateinit var mondayHolder : CardView
    private lateinit var tuesdayHolder : CardView
    private lateinit var wednesdayHolder : CardView
    private lateinit var thursdayHolder : CardView
    private lateinit var fridayHolder : CardView
    private lateinit var saturdayHolder : CardView
    private lateinit var sundayHolder : CardView
    private lateinit var exerciseLayout : FrameLayout
    private lateinit var dayOfWeekText : TextView
    private lateinit var mondayCardText : TextView
    private lateinit var tuesdayCardText : TextView
    private lateinit var wednesdayCardText : TextView
    private lateinit var thursdayCardText : TextView
    private lateinit var fridayCardText : TextView
    private lateinit var saturdayCardText : TextView
    private lateinit var sundayCardText : TextView
    private lateinit var mondayCardConstraint : ConstraintLayout
    private lateinit var tuesdayCardConstraint : ConstraintLayout
    private lateinit var wednesdayCardConstraint : ConstraintLayout
    private lateinit var thursdayCardConstraint : ConstraintLayout
    private lateinit var fridayCardConstraint : ConstraintLayout
    private lateinit var saturdayCardConstraint : ConstraintLayout
    private lateinit var sundayCardConstraint : ConstraintLayout



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ExerciciosFragmentBinding.inflate(inflater, container, false)

        val root: View = binding.root
        mondayExercises = arrayListOf()
        tuesdayExercises = arrayListOf()
        wednesdayExercises = arrayListOf()
        thursdayExercises = arrayListOf()
        fridayExercises = arrayListOf()
        saturdayExercises = arrayListOf()
        sundayExercises = arrayListOf()
        mondayCardConstraint = root.findViewById(R.id.mondayCardConstraint)
        tuesdayCardConstraint = root.findViewById(R.id.tuesdayCardConstraint)
        wednesdayCardConstraint = root.findViewById(R.id.wednesdayCardConstraint)
        thursdayCardConstraint = root.findViewById(R.id.thursdayCardConstraint)
        fridayCardConstraint = root.findViewById(R.id.fridayCardConstraint)
        saturdayCardConstraint = root.findViewById(R.id.saturdayCardConstraint)
        sundayCardConstraint = root.findViewById(R.id.sundayCardConstraint)
        exerciseLayout = root.findViewById(R.id.exercisesLayout)
        mondayHolder = root.findViewById(R.id.mondayHolder)
        tuesdayHolder = root.findViewById(R.id.tuesdayHolder)
        wednesdayHolder = root.findViewById(R.id.wednesdayHolder)
        thursdayHolder = root.findViewById(R.id.thursdayHolder)
        fridayHolder = root.findViewById(R.id.fridayHolder)
        saturdayHolder = root.findViewById(R.id.saturdayHolder)
        sundayHolder = root.findViewById(R.id.sundayHolder)
        dayOfWeekText = root.findViewById(R.id.exerciseDayOfWeekText)

        searchPlanButton = root.findViewById(R.id.searchPlanCard)
        generatePlanButton = root.findViewById(R.id.generatePlanCard)

        planExercises = arrayListOf()


        fireBaseFirestore = FirebaseFirestore.getInstance()
        recyclerView = root.findViewById(R.id.recyclerView)
        spinKit = root.findViewById(R.id.spin_kit)
        mondayNumber = root.findViewById(R.id.mondayNumber)
        tuesdayNumber = root.findViewById(R.id.tuesdayNumber)
        wednesdayNumber = root.findViewById(R.id.wednesdayNumber)
        thursdayNumber = root.findViewById(R.id.thursdayNumber)
        fridayNumber = root.findViewById(R.id.fridayNumber)
        saturdayNumber = root.findViewById(R.id.saturdayNumber)
        sundayNumber = root.findViewById(R.id.sundayNumber)
        filterLayout = root.findViewById(R.id.FilterLayout)
        contentFilterLayout = root.findViewById(R.id.contentFilterLayout)
        headerFilterLayout = root.findViewById(R.id.headerFilterConstraint)
        chestLayout = root.findViewById(R.id.chestLayout)
        armsLayout = root.findViewById(R.id.armsLayout)
        shouldersLayout = root.findViewById(R.id.shouldersLayout)
        backLayout = root.findViewById(R.id.backLayout)
        lowerBodyLayout = root.findViewById(R.id.lowerBodyLayout)
        cardioLayout = root.findViewById(R.id.cardioLayout)
        chosenFilterText = root.findViewById(R.id.chosenFilterText)
        chosenFilterImage = root.findViewById(R.id.chosenFilterImage)
        filterArrow = root.findViewById(R.id.filterArrow)
        mondayCard = root.findViewById(R.id.mondayCard)
        tuesdayCard = root.findViewById(R.id.tuesdayCard)
        wednesdayCard = root.findViewById(R.id.wednesdayCard)
        thursdayCard = root.findViewById(R.id.thursdayCard)
        fridayCard = root.findViewById(R.id.fridayCard)
        saturdayCard = root.findViewById(R.id.saturdayCard)
        sundayCard = root.findViewById(R.id.sundayCard)
        mondayCardText = root.findViewById(R.id.exerciseMondayCardText)
        tuesdayCardText = root.findViewById(R.id.exerciseTuesdayCardText)
        wednesdayCardText = root.findViewById(R.id.exerciseWednesdayCardText)
        thursdayCardText = root.findViewById(R.id.exerciseThursdayCardText)
        fridayCardText = root.findViewById(R.id.exerciseFridayCardText)
        saturdayCardText = root.findViewById(R.id.exerciseSaturdayCardText)
        sundayCardText = root.findViewById(R.id.exerciseSundayCardText)
        createPlanButton = root.findViewById(R.id.createPlanCard)
        recyclerView.layoutManager = LinearLayoutManager(this.context)
        exercises = arrayListOf()
        myAdapter = RecyclerAdapter(exercises,mondayExercises)
        myAdapter.setHasStableIds(true)
        myAdapter.submitList(exercises)
        recyclerView.adapter = myAdapter
        recyclerView.alpha = 1f
        recyclerView.setHasFixedSize(true)
        spinKit.alpha = 1f

        // recyclerView.addOnScrollListener()
        recyclerView.itemAnimator = null
        fireBaseFirestore.clearPersistence()
        val settings = firestoreSettings {
            isPersistenceEnabled = false
        }






        searchPlanButton.setOnClickListener {
            val bottomSheet = SearchPlanBottomSheetFragment()
            bottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           )
            bottomSheet.show(childFragmentManager,"SearchExerciseSheet")
        }
         generatePlanButton.setOnClickListener {
            val bottomSheet = GeneratePlanBottomSheet()
            bottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           )
            bottomSheet.show(childFragmentManager,"GenerateExerciseSheet")
        }



        createPlanButton.setOnClickListener {



            val exerciseRef = fireBaseFirestore.collection("Exercicios")
            val bundle = Bundle()
            bundle.putStringArrayList("mondayExercises", mondayExercises)
            bundle.putStringArrayList("tuesdayExercises", tuesdayExercises)
            bundle.putStringArrayList("wednesdayExercises", wednesdayExercises)
            bundle.putStringArrayList("thursdayExercises", thursdayExercises)
            bundle.putStringArrayList("fridayExercises", fridayExercises)
            bundle.putStringArrayList("saturdayExercises", saturdayExercises)
            bundle.putStringArrayList("sundayExercises", sundayExercises)
            val bottomSheet = BottomSheetFragment()
            bottomSheet.setArguments(bundle)
            bottomSheet.setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetDialogTheme                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           )
            bottomSheet.show(childFragmentManager,"exerciseSheet")

            for (i in mondayExercises.size -1 downTo 0)
            {
                query = exerciseRef.whereEqualTo("nomeExercicio",mondayExercises[i])
                query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        spinKit.alpha = 1f
                        recyclerView.alpha = 0f

                        if( error != null){
                            Log.d("error", "error")
                            return
                        }

                        for(dc : DocumentChange in value?.documentChanges!!){
                            val tempEquipamento: ArrayList<String> = ArrayList()
                            tempEquipamento.add(dc.document.get("equipamento").toString())
                            val tempExercies = PlanExercisesMonday(dc.document["nomeExercicio"].toString(),dc.document.getLong("dificuldade"),dc.document["seccao"].toString(),tempEquipamento,dc.document.getLong("id"), "Segunda")
                            planExercises.add(tempExercies)
                            spinKit.alpha = 0f
                            recyclerView.alpha = 1f


                        }

                    }

                })
            }
            for (i in tuesdayExercises.size -1 downTo 0)
            {
                query = exerciseRef.whereEqualTo("nomeExercicio",tuesdayExercises[i])
                query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        spinKit.alpha = 1f
                        recyclerView.alpha = 0f

                        if( error != null){
                            Log.d("error", "error")
                            return
                        }

                        for(dc : DocumentChange in value?.documentChanges!!){
                            val tempEquipamento: ArrayList<String> = ArrayList()
                            tempEquipamento.add(dc.document.get("equipamento").toString())
                            val tempExercies = PlanExercisesMonday(dc.document["nomeExercicio"].toString(),dc.document.getLong("dificuldade"),dc.document["seccao"].toString(),tempEquipamento,dc.document.getLong("id"), "Terça")
                            planExercises.add(tempExercies)
                            spinKit.alpha = 0f
                            recyclerView.alpha = 1f

                        }

                    }

                })
            }

            for (i in wednesdayExercises.size -1 downTo 0)
            {
                query = exerciseRef.whereEqualTo("nomeExercicio",wednesdayExercises[i])
                query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        spinKit.alpha = 1f
                        recyclerView.alpha = 0f

                        if( error != null){
                            Log.d("error", "error")
                            return
                        }

                        for(dc : DocumentChange in value?.documentChanges!!){
                            val tempEquipamento: ArrayList<String> = ArrayList()
                            tempEquipamento.add(dc.document.get("equipamento").toString())
                            Log.d("testValuePlan", dc.document["nomeExercicio"].toString())
                            val tempExercies = PlanExercisesMonday(dc.document["nomeExercicio"].toString(),dc.document.getLong("dificuldade"),dc.document["seccao"].toString(),tempEquipamento,dc.document.getLong("id"), "Quarta")
                            planExercises.add(tempExercies)
                            spinKit.alpha = 0f
                            recyclerView.alpha = 1f

                        }

                    }

                })
            }
            for (i in thursdayExercises.size -1 downTo 0)
            {
                query = exerciseRef.whereEqualTo("nomeExercicio",thursdayExercises[i])
                query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        spinKit.alpha = 1f
                        recyclerView.alpha = 0f

                        if( error != null){
                            Log.d("error", "error")
                            return
                        }

                        for(dc : DocumentChange in value?.documentChanges!!){
                            val tempEquipamento: ArrayList<String> = ArrayList()
                            tempEquipamento.add(dc.document.get("equipamento").toString())
                            Log.d("testValuePlan", dc.document["nomeExercicio"].toString())
                            val tempExercies = PlanExercisesMonday(dc.document["nomeExercicio"].toString(),dc.document.getLong("dificuldade"),dc.document["seccao"].toString(),tempEquipamento,dc.document.getLong("id"), "Quarta")
                            planExercises.add(tempExercies)
                            spinKit.alpha = 0f
                            recyclerView.alpha = 1f

                        }

                    }

                })
            }

            for (i in fridayExercises.size -1 downTo 0)
            {
                query = exerciseRef.whereEqualTo("nomeExercicio",fridayExercises[i])
                query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        spinKit.alpha = 1f
                        recyclerView.alpha = 0f

                        if( error != null){
                            Log.d("error", "error")
                            return
                        }

                        for(dc : DocumentChange in value?.documentChanges!!){
                            val tempEquipamento: ArrayList<String> = ArrayList()
                            tempEquipamento.add(dc.document.get("equipamento").toString())
                            Log.d("testValuePlan", dc.document["nomeExercicio"].toString())
                            val tempExercies = PlanExercisesMonday(dc.document["nomeExercicio"].toString(),dc.document.getLong("dificuldade"),dc.document["seccao"].toString(),tempEquipamento,dc.document.getLong("id"), "Quarta")
                            planExercises.add(tempExercies)
                            spinKit.alpha = 0f
                            recyclerView.alpha = 1f

                        }

                    }

                })
            }
            for (i in saturdayExercises.size -1 downTo 0)
            {
                query = exerciseRef.whereEqualTo("nomeExercicio",saturdayExercises[i])
                query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        spinKit.alpha = 1f
                        recyclerView.alpha = 0f

                        if( error != null){
                            Log.d("error", "error")
                            return
                        }

                        for(dc : DocumentChange in value?.documentChanges!!){
                            val tempEquipamento: ArrayList<String> = ArrayList()
                            tempEquipamento.add(dc.document.get("equipamento").toString())
                            Log.d("testValuePlan", dc.document["nomeExercicio"].toString())
                            val tempExercies = PlanExercisesMonday(dc.document["nomeExercicio"].toString(),dc.document.getLong("dificuldade"),dc.document["seccao"].toString(),tempEquipamento,dc.document.getLong("id"), "Quarta")
                            planExercises.add(tempExercies)
                            spinKit.alpha = 0f
                            recyclerView.alpha = 1f

                        }

                    }

                })
            }
            for (i in sundayExercises.size -1 downTo 0)
            {
                query = exerciseRef.whereEqualTo("nomeExercicio",saturdayExercises[i])
                query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                        spinKit.alpha = 1f
                        recyclerView.alpha = 0f

                        if( error != null){
                            Log.d("error", "error")
                            return
                        }

                        for(dc : DocumentChange in value?.documentChanges!!){
                            val tempEquipamento: ArrayList<String> = ArrayList()
                            tempEquipamento.add(dc.document.get("equipamento").toString())
                            Log.d("testValuePlan", dc.document["nomeExercicio"].toString())
                            val tempExercies = PlanExercisesMonday(dc.document["nomeExercicio"].toString(),dc.document.getLong("dificuldade"),dc.document["seccao"].toString(),tempEquipamento,dc.document.getLong("id"), "Quarta")
                            planExercises.add(tempExercies)
                            spinKit.alpha = 0f
                            recyclerView.alpha = 1f

                        }

                    }

                })
            }






        }

        mondayCard.setOnClickListener {
            dayOfTheWeek = "Segunda"
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = 1000
            val mondayCardHolderparams = mondayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            mondayCardHolderparams.bottomToBottom = mondayCardConstraint.id
            mondayHolder.requestLayout()
            val tuesdayCardHolderparams = tuesdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            tuesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            tuesdayHolder.requestLayout()
            val wednesdayCardHolderparams = wednesdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            wednesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            wednesdayHolder.requestLayout()
            val thursdayCardHolderparams = thursdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            thursdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            thursdayHolder.requestLayout()
            val fridayCardHolderparams = fridayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            fridayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            fridayHolder.requestLayout()
            val saturdayCardHolderparams = saturdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            saturdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            saturdayHolder.requestLayout()
            val sundayCardHolderparams = sundayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            sundayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            sundayHolder.requestLayout()
            mondayHolder.cardElevation = 10f
            tuesdayHolder.cardElevation = 0f
            wednesdayHolder.cardElevation = 0f
            thursdayHolder.cardElevation = 0f
            fridayHolder.cardElevation = 0f
            saturdayHolder.cardElevation = 0f
            sundayHolder.cardElevation = 0f
            val animationShowMonday = AnimationSet(false)
            val animationHide = AnimationSet(false)
            animationShowMonday.addAnimation(fadeIn)
            mondayCard.setAnimation(animationShowMonday)
            dayOfWeekText.text = "Segunda-feira"
            dayOfWeekText.setAnimation(animationShowMonday)
            mondayHolder.setAnimation(animationShowMonday)
            mondayCard.setCardBackgroundColor(resources.getColor(R.color.lightwhite))
            mondayHolder.setCardBackgroundColor(resources.getColor(R.color.neonblue))
            tuesdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            TransitionManager.beginDelayedTransition(sundayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(mondayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(tuesdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(wednesdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(thursdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(fridayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(saturdayCard,AutoTransition())
        }
        tuesdayCard.setOnClickListener {
            dayOfTheWeek = "Terça"
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = 1000
            val mondayCardHolderparams = mondayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            mondayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            mondayHolder.requestLayout()
            val tuesdayCardHolderparams = tuesdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            tuesdayCardHolderparams.bottomToBottom = tuesdayCardConstraint.id
            tuesdayHolder.requestLayout()
            val wednesdayCardHolderparams = wednesdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            wednesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            wednesdayHolder.requestLayout()
            val thursdayCardHolderparams = thursdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            thursdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            thursdayHolder.requestLayout()
            val fridayCardHolderparams = fridayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            fridayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            fridayHolder.requestLayout()
            val saturdayCardHolderparams = saturdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            saturdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            saturdayHolder.requestLayout()
            val sundayCardHolderparams = sundayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            sundayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            sundayHolder.requestLayout()
            mondayHolder.cardElevation = 0f
            tuesdayHolder.cardElevation = 10f
            wednesdayHolder.cardElevation = 0f
            thursdayHolder.cardElevation = 0f
            fridayHolder.cardElevation = 0f
            saturdayHolder.cardElevation = 0f
            sundayHolder.cardElevation = 0f
            val animationShowMonday = AnimationSet(false)
            val animationHide = AnimationSet(false)
            animationShowMonday.addAnimation(fadeIn)
            tuesdayCard.setAnimation(animationShowMonday)
            dayOfWeekText.text = "Terça-feira"
            dayOfWeekText.setAnimation(animationShowMonday)
            tuesdayHolder.setAnimation(animationShowMonday)
            mondayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            mondayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayCard.setCardBackgroundColor(resources.getColor(R.color.lightwhite))
            tuesdayHolder.setCardBackgroundColor(resources.getColor(R.color.neonblue))
            wednesdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))

            TransitionManager.beginDelayedTransition(sundayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(mondayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(tuesdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(wednesdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(thursdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(fridayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(saturdayCard,AutoTransition())
        }
        wednesdayCard.setOnClickListener {
            dayOfTheWeek = "Quarta"
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = 1000
            val mondayCardHolderparams = mondayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            mondayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            mondayHolder.requestLayout()
            val tuesdayCardHolderparams = tuesdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            tuesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            tuesdayHolder.requestLayout()
            val wednesdayCardHolderparams = wednesdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            wednesdayCardHolderparams.bottomToBottom = wednesdayCardConstraint.id
            wednesdayHolder.requestLayout()
            val thursdayCardHolderparams = thursdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            thursdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            thursdayHolder.requestLayout()
            val fridayCardHolderparams = fridayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            fridayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            fridayHolder.requestLayout()
            val saturdayCardHolderparams = saturdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            saturdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            saturdayHolder.requestLayout()
            val sundayCardHolderparams = sundayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            sundayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            sundayHolder.requestLayout()
            val animationShowMonday = AnimationSet(false)
            val animationHide = AnimationSet(false)
            animationShowMonday.addAnimation(fadeIn)
            mondayHolder.cardElevation = 0f
            tuesdayHolder.cardElevation = 0f
            wednesdayHolder.cardElevation = 10f
            thursdayHolder.cardElevation = 0f
            fridayHolder.cardElevation = 0f
            saturdayHolder.cardElevation = 0f
            sundayHolder.cardElevation = 0f
            wednesdayCard.setAnimation(animationShowMonday)
            dayOfWeekText.text = "Quarta-feira"
            dayOfWeekText.setAnimation(animationShowMonday)
            wednesdayHolder.setAnimation(animationShowMonday)
            mondayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            mondayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayCard.setCardBackgroundColor(resources.getColor(R.color.lightwhite))
            wednesdayHolder.setCardBackgroundColor(resources.getColor(R.color.neonblue))
            thursdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            TransitionManager.beginDelayedTransition(sundayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(mondayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(tuesdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(wednesdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(thursdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(fridayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(saturdayCard,AutoTransition())
        }
        thursdayCard.setOnClickListener {
            dayOfTheWeek = "Quinta"
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = 1000
            val mondayCardHolderparams = mondayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            mondayCardHolderparams.bottomToBottom =ConstraintLayout.LayoutParams.UNSET
            mondayHolder.requestLayout()
            val tuesdayCardHolderparams = tuesdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            tuesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            tuesdayHolder.requestLayout()
            val wednesdayCardHolderparams = wednesdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            wednesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            wednesdayHolder.requestLayout()
            val thursdayCardHolderparams = thursdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            thursdayCardHolderparams.bottomToBottom = thursdayCardConstraint.id
            thursdayHolder.requestLayout()
            val fridayCardHolderparams = fridayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            fridayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            fridayHolder.requestLayout()
            val saturdayCardHolderparams = saturdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            saturdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            saturdayHolder.requestLayout()
            val sundayCardHolderparams = sundayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            sundayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            sundayHolder.requestLayout()
            mondayHolder.cardElevation = 0f
            tuesdayHolder.cardElevation = 0f
            wednesdayHolder.cardElevation = 0f
            thursdayHolder.cardElevation = 10f
            fridayHolder.cardElevation = 0f
            saturdayHolder.cardElevation = 0f
            sundayHolder.cardElevation = 0f
            val animationShowMonday = AnimationSet(false)
            val animationHide = AnimationSet(false)
            animationShowMonday.addAnimation(fadeIn)
            thursdayCard.setAnimation(animationShowMonday)
            dayOfWeekText.text = "Quinta-feira"
            dayOfWeekText.setAnimation(animationShowMonday)
            thursdayHolder.setAnimation(animationShowMonday)
            mondayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            mondayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayCard.setCardBackgroundColor(resources.getColor(R.color.lightwhite))
            thursdayHolder.setCardBackgroundColor(resources.getColor(R.color.neonblue))
            fridayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))

            TransitionManager.beginDelayedTransition(sundayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(mondayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(tuesdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(wednesdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(thursdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(fridayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(saturdayCard,AutoTransition())
        }
        fridayCard.setOnClickListener {
            dayOfTheWeek = "Sexta"
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = 1000
            val mondayCardHolderparams = mondayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            mondayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            mondayHolder.requestLayout()
            val tuesdayCardHolderparams = tuesdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            tuesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            tuesdayHolder.requestLayout()
            val wednesdayCardHolderparams = wednesdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            wednesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            wednesdayHolder.requestLayout()
            val thursdayCardHolderparams = thursdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            thursdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            thursdayHolder.requestLayout()
            val fridayCardHolderparams = fridayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            fridayCardHolderparams.bottomToBottom = fridayCardConstraint.id
            fridayHolder.requestLayout()
            val saturdayCardHolderparams = saturdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            saturdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            saturdayHolder.requestLayout()
            val sundayCardHolderparams = sundayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            sundayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            sundayHolder.requestLayout()
            mondayHolder.cardElevation = 0f
            tuesdayHolder.cardElevation = 0f
            wednesdayHolder.cardElevation = 0f
            thursdayHolder.cardElevation = 0f
            fridayHolder.cardElevation = 10f
            saturdayHolder.cardElevation = 0f
            sundayHolder.cardElevation = 0f
            val animationShowMonday = AnimationSet(false)
            val animationHide = AnimationSet(false)
            animationShowMonday.addAnimation(fadeIn)
            fridayCard.setAnimation(animationShowMonday)
            dayOfWeekText.text = "Sexta-feira"
            dayOfWeekText.setAnimation(animationShowMonday)
            fridayHolder.setAnimation(animationShowMonday)
            mondayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            mondayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayCard.setCardBackgroundColor(resources.getColor(R.color.lightwhite))
            fridayHolder.setCardBackgroundColor(resources.getColor(R.color.neonblue))
            saturdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            TransitionManager.beginDelayedTransition(sundayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(mondayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(tuesdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(wednesdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(thursdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(fridayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(saturdayCard,AutoTransition())
        }
        saturdayCard.setOnClickListener {
            dayOfTheWeek = "Sábado"
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = 1000
            val mondayCardHolderparams = mondayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            mondayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            mondayHolder.requestLayout()
            val tuesdayCardHolderparams = tuesdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            tuesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            tuesdayHolder.requestLayout()
            val wednesdayCardHolderparams = wednesdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            wednesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            wednesdayHolder.requestLayout()
            val thursdayCardHolderparams = thursdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            thursdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            thursdayHolder.requestLayout()
            val fridayCardHolderparams = fridayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            fridayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            fridayHolder.requestLayout()
            val saturdayCardHolderparams = saturdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            saturdayCardHolderparams.bottomToBottom = saturdayCardConstraint.id
            saturdayHolder.requestLayout()
            val sundayCardHolderparams = sundayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            sundayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            sundayHolder.requestLayout()
            mondayHolder.cardElevation = 0f
            tuesdayHolder.cardElevation = 0f
            wednesdayHolder.cardElevation = 0f
            thursdayHolder.cardElevation = 0f
            fridayHolder.cardElevation = 0f
            saturdayHolder.cardElevation = 10f
            sundayHolder.cardElevation = 0f
            val animationShowMonday = AnimationSet(false)
            val animationHide = AnimationSet(false)
            animationShowMonday.addAnimation(fadeIn)
            saturdayCard.setAnimation(animationShowMonday)
            dayOfWeekText.text = "Sábado"
            dayOfWeekText.setAnimation(animationShowMonday)
            saturdayHolder.setAnimation(animationShowMonday)
            mondayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            mondayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayCard.setCardBackgroundColor(resources.getColor(R.color.lightwhite))
            saturdayHolder.setCardBackgroundColor(resources.getColor(R.color.neonblue))
            sundayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))

            TransitionManager.beginDelayedTransition(sundayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(mondayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(tuesdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(wednesdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(thursdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(fridayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(saturdayCard,AutoTransition())
        }
        sundayCard.setOnClickListener {
            dayOfTheWeek = "Domingo"
            val fadeIn = AlphaAnimation(0f, 1f)
            fadeIn.interpolator = DecelerateInterpolator() //add this
            fadeIn.duration = 1000
            val mondayCardHolderparams = mondayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            mondayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            mondayHolder.requestLayout()
            val tuesdayCardHolderparams = tuesdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            tuesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            tuesdayHolder.requestLayout()
            val wednesdayCardHolderparams = wednesdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            wednesdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            wednesdayHolder.requestLayout()
            val thursdayCardHolderparams = thursdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            thursdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            thursdayHolder.requestLayout()
            val fridayCardHolderparams = fridayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            fridayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            fridayHolder.requestLayout()
            val saturdayCardHolderparams = saturdayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            saturdayCardHolderparams.bottomToBottom = ConstraintLayout.LayoutParams.UNSET
            saturdayHolder.requestLayout()
            val sundayCardHolderparams = sundayHolder.getLayoutParams() as ConstraintLayout.LayoutParams
            sundayCardHolderparams.bottomToBottom = sundayCardConstraint.id
            sundayHolder.requestLayout()
            mondayHolder.cardElevation = 0f
            tuesdayHolder.cardElevation = 0f
            wednesdayHolder.cardElevation = 0f
            thursdayHolder.cardElevation = 0f
            fridayHolder.cardElevation = 0f
            saturdayHolder.cardElevation = 0f
            sundayHolder.cardElevation = 10f
            val animationShowMonday = AnimationSet(false)
            val animationHide = AnimationSet(false)
            animationShowMonday.addAnimation(fadeIn)
            sundayCard.setAnimation(animationShowMonday)
            dayOfWeekText.text = "Domingo"
            dayOfWeekText.setAnimation(animationShowMonday)
            sundayHolder.setAnimation(animationShowMonday)
            mondayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            mondayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            tuesdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            wednesdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            thursdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            fridayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayCard.setCardBackgroundColor(resources.getColor(R.color.transparent))
            saturdayHolder.setCardBackgroundColor(resources.getColor(R.color.transparent))
            sundayCard.setCardBackgroundColor(resources.getColor(R.color.lightwhite))
            sundayHolder.setCardBackgroundColor(resources.getColor(R.color.neonblue))
            TransitionManager.beginDelayedTransition(sundayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(mondayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(tuesdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(wednesdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(thursdayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(fridayCard,AutoTransition())
            TransitionManager.beginDelayedTransition(saturdayCard,AutoTransition())
        }


        headerFilterLayout.setOnClickListener {
            if(contentFilterLayout.visibility == View.GONE){
                contentFilterLayout.visibility = View.VISIBLE
            }else{
                contentFilterLayout.visibility = View.GONE
            }
            TransitionManager.beginDelayedTransition(filterLayout, AutoTransition())


        }

        chestLayout.setOnClickListener{
            chosenFilterText.text = "Peito"
            chosenFilterImage.setImageDrawable(resources.getDrawable(R.drawable.chest01))
            chestLayout.setBackgroundColor(resources.getColor(R.color.lightneonblue))
            armsLayout.setBackgroundColor(resources.getColor(R.color.white))
            shouldersLayout.setBackgroundColor(resources.getColor(R.color.white))
            backLayout.setBackgroundColor(resources.getColor(R.color.white))
            lowerBodyLayout.setBackgroundColor(resources.getColor(R.color.white))
            cardioLayout.setBackgroundColor(resources.getColor(R.color.white))
            contentFilterLayout.visibility = View.GONE
            TransitionManager.beginDelayedTransition(filterLayout, AutoTransition())
            exercises.clear()
            myAdapter.notifyDataSetChanged()
            val exerciseRef = fireBaseFirestore.collection("Exercicios")
            query = exerciseRef.whereEqualTo("seccao","Peito")
            query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    spinKit.alpha = 1f
                    recyclerView.alpha = 0f

                    if( error != null){
                        Log.d("error", "error")
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){

                        if(dc.type == DocumentChange.Type.ADDED){
                            exercises.add(dc.document.toObject(Exercises::class.java))
                            spinKit.alpha = 0f
                            recyclerView.alpha = 1f
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }

            })



        }
        armsLayout.setOnClickListener{
            chosenFilterText.text = "Braços"
            chosenFilterImage.setImageDrawable(resources.getDrawable(R.drawable.biceps))
            chestLayout.setBackgroundColor(resources.getColor(R.color.white))
            armsLayout.setBackgroundColor(resources.getColor(R.color.lightneonblue))
            shouldersLayout.setBackgroundColor(resources.getColor(R.color.white))
            backLayout.setBackgroundColor(resources.getColor(R.color.white))
            lowerBodyLayout.setBackgroundColor(resources.getColor(R.color.white))
            cardioLayout.setBackgroundColor(resources.getColor(R.color.white))
            contentFilterLayout.visibility = View.GONE
            TransitionManager.beginDelayedTransition(filterLayout, AutoTransition())
            exercises.clear()
            myAdapter.notifyDataSetChanged()
            val exerciseRef = fireBaseFirestore.collection("Exercicios")
            query = exerciseRef.whereEqualTo("seccao","Bíceps")
            query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    spinKit.alpha = 1f
                    recyclerView.alpha = 0f

                    if( error != null){
                        Log.d("error", "error")
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){

                        if(dc.type == DocumentChange.Type.ADDED){
                            exercises.add(dc.document.toObject(Exercises::class.java))
                            spinKit.alpha = 0f
                            recyclerView.alpha = 1f
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }

            })

        }
        shouldersLayout.setOnClickListener{
            chosenFilterText.text = "Ombros"
            chosenFilterImage.setImageDrawable(resources.getDrawable(R.drawable.shoulder01))
            chestLayout.setBackgroundColor(resources.getColor(R.color.white))
            armsLayout.setBackgroundColor(resources.getColor(R.color.white))
            shouldersLayout.setBackgroundColor(resources.getColor(R.color.lightneonblue))
            backLayout.setBackgroundColor(resources.getColor(R.color.white))
            lowerBodyLayout.setBackgroundColor(resources.getColor(R.color.white))
            cardioLayout.setBackgroundColor(resources.getColor(R.color.white))
            contentFilterLayout.visibility = View.GONE
            TransitionManager.beginDelayedTransition(filterLayout, AutoTransition())
            exercises.clear()
            myAdapter.notifyDataSetChanged()
            val exerciseRef = fireBaseFirestore.collection("Exercicios")
            query = exerciseRef.whereEqualTo("seccao","Ombros")
            query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    spinKit.alpha = 1f
                    recyclerView.alpha = 0f

                    if( error != null){
                        Log.d("error", "error")
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){

                        if(dc.type == DocumentChange.Type.ADDED){
                            exercises.add(dc.document.toObject(Exercises::class.java))
                            spinKit.alpha = 0f
                            recyclerView.alpha = 1f
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }

            })

        }
        backLayout.setOnClickListener{
            chosenFilterText.text = "Costas"
            chosenFilterImage.setImageDrawable(resources.getDrawable(R.drawable.back))
            chestLayout.setBackgroundColor(resources.getColor(R.color.white))
            armsLayout.setBackgroundColor(resources.getColor(R.color.white))
            shouldersLayout.setBackgroundColor(resources.getColor(R.color.white))
            backLayout.setBackgroundColor(resources.getColor(R.color.lightneonblue))
            lowerBodyLayout.setBackgroundColor(resources.getColor(R.color.white))
            cardioLayout.setBackgroundColor(resources.getColor(R.color.white))
            contentFilterLayout.visibility = View.GONE
            TransitionManager.beginDelayedTransition(filterLayout, AutoTransition())
            exercises.clear()
            myAdapter.notifyDataSetChanged()
            val exerciseRef = fireBaseFirestore.collection("Exercicios")
            query = exerciseRef.whereEqualTo("seccao","Costas")
            query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    spinKit.alpha = 1f
                    recyclerView.alpha = 0f

                    if( error != null){
                        Log.d("error", "error")
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){

                        if(dc.type == DocumentChange.Type.ADDED){
                            exercises.add(dc.document.toObject(Exercises::class.java))
                            spinKit.alpha = 0f
                            recyclerView.alpha = 1f
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }

            })


        }
        lowerBodyLayout.setOnClickListener{
            chosenFilterText.text = "Corpo Inferior"
            chosenFilterImage.setImageDrawable(resources.getDrawable(R.drawable.legs01))
            chestLayout.setBackgroundColor(resources.getColor(R.color.white))
            armsLayout.setBackgroundColor(resources.getColor(R.color.white))
            shouldersLayout.setBackgroundColor(resources.getColor(R.color.white))
            backLayout.setBackgroundColor(resources.getColor(R.color.white))
            lowerBodyLayout.setBackgroundColor(resources.getColor(R.color.lightneonblue))
            cardioLayout.setBackgroundColor(resources.getColor(R.color.white))
            contentFilterLayout.visibility = View.GONE
            TransitionManager.beginDelayedTransition(filterLayout, AutoTransition())
            exercises.clear()
            myAdapter.notifyDataSetChanged()
            val exerciseRef = fireBaseFirestore.collection("Exercicios")
            query = exerciseRef.whereEqualTo("seccao","Corpo Inferior")
            query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    spinKit.alpha = 1f
                    recyclerView.alpha = 0f

                    if( error != null){
                        Log.d("error", "error")
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){

                        if(dc.type == DocumentChange.Type.ADDED){
                            exercises.add(dc.document.toObject(Exercises::class.java))
                            spinKit.alpha = 0f
                            recyclerView.alpha = 1f
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }

            })

        }
        cardioLayout.setOnClickListener{
            chosenFilterText.text = "Cardio"
            chosenFilterImage.setImageDrawable(resources.getDrawable(R.drawable.heart01))
            chestLayout.setBackgroundColor(resources.getColor(R.color.white))
            armsLayout.setBackgroundColor(resources.getColor(R.color.white))
            shouldersLayout.setBackgroundColor(resources.getColor(R.color.white))
            backLayout.setBackgroundColor(resources.getColor(R.color.white))
            lowerBodyLayout.setBackgroundColor(resources.getColor(R.color.white))
            cardioLayout.setBackgroundColor(resources.getColor(R.color.lightneonblue))
            contentFilterLayout.visibility = View.GONE
            TransitionManager.beginDelayedTransition(filterLayout, AutoTransition())
            exercises.clear()
            myAdapter.notifyDataSetChanged()
            val exerciseRef = fireBaseFirestore.collection("Exercicios")
            query = exerciseRef.whereEqualTo("seccao","Cardio")
            query.addSnapshotListener(object : EventListener<QuerySnapshot>{
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    spinKit.alpha = 1f
                    recyclerView.alpha = 0f

                    if( error != null){
                        Log.d("error", "error")
                        return
                    }

                    for(dc : DocumentChange in value?.documentChanges!!){

                        if(dc.type == DocumentChange.Type.ADDED){
                            exercises.add(dc.document.toObject(Exercises::class.java))
                            spinKit.alpha = 0f
                            recyclerView.alpha = 1f
                        }
                    }
                    myAdapter.notifyDataSetChanged()
                }

            })

        }







        myAdapter.setOnItemClickListener(object : RecyclerAdapter.onExerciseClick{
            override fun onItemClick(title: CharSequence) {



                Log.d("posClick", dayOfTheWeek)
                if(dayOfTheWeek == "Segunda"){
                    animateMondayHolder()
                    mondayExercises.add(title.toString())
                    mondayNumber.text = mondayExercises.size.toString()
                }
                if(dayOfTheWeek == "Terça"){
                    animateTuesdayHolder()
                    tuesdayExercises.add(title.toString())
                    tuesdayNumber.text = tuesdayExercises.size.toString()
                }
                if(dayOfTheWeek == "Quarta"){
                    animateWednesdayHolder()
                    wednesdayExercises.add(title.toString())
                    wednesdayNumber.text = wednesdayExercises.size.toString()
                }
                if(dayOfTheWeek == "Quinta"){
                    animateThursdayHolder()
                    thursdayExercises.add(title.toString())

                    thursdayNumber.text = thursdayExercises.size.toString()
                }
                if(dayOfTheWeek == "Sexta"){
                    animateFridayHolder()
                    fridayExercises.add(title.toString())
                    fridayNumber.text = fridayExercises.size.toString()
                }
                if(dayOfTheWeek == "Sábado"){
                    animateSaturdayHolder()
                    saturdayExercises.add(title.toString())
                    saturdayNumber.text = saturdayExercises.size.toString()
                }
                if(dayOfTheWeek == "Domingo"){
                    animateSundayHolder()
                    sundayExercises.add(title.toString())
                    sundayNumber.text = sundayExercises.size.toString()
                }

            }

        })







        getData()





        val exerciseRef = fireBaseFirestore.collection("Exercicios")
        query = exerciseRef.whereEqualTo("seccao","Bíceps")
        //  * Chat.class instructs the adapter to convert each DocumentSnapshot to a Chat object
        //  * Chat.class instructs the adapter to convert each DocumentSnapshot to a Chat object


        query.addSnapshotListener(object : EventListener<QuerySnapshot>{
           override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
               spinKit.alpha = 1f
               recyclerView.alpha = 0f

               if( error != null){
                   Log.d("error", "error")
                   return
               }

               for(dc : DocumentChange in value?.documentChanges!!){

                   if(dc.type == DocumentChange.Type.ADDED){
                       exercises.add(dc.document.toObject(Exercises::class.java))
                       spinKit.alpha = 0f
                       recyclerView.alpha = 1f
                   }
               }
               myAdapter.notifyDataSetChanged()
           }

       })














/*

       if(isPermissionGranted()){
           requestPermission()
       }

        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!!
*/


        return root
    }

    fun getData(){

    }

   /* private fun requestPermission() {
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q){
           ActivityCompat.requestPermissions(this.requireActivity(),
           arrayOf(android.Manifest.permission.ACTIVITY_RECOGNITION),
               ACTIVITY_RECOGNITION_REQUEST_CODE)
       }
    }

    private fun isPermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ContextCompat.checkSelfPermission(this.requireActivity(),Manifest
                .permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED
        } else {
            Log.d("checkpermission", "wtf")
            return false

        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            ACTIVITY_RECOGNITION_REQUEST_CODE ->{
                if((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)){
                   Log.d("checkpermission", "Granted")
                }
            }
        }
    }
*/
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ExerciciosViewModel::class.java)
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
                    val mondayHolderParams = mondayHolder.layoutParams
                    mondayHolder.layoutParams = mondayHolderParams
                    mondayHolder.requestLayout()

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
                    val tuesdayHolderParams = tuesdayHolder.layoutParams
                    tuesdayHolder.layoutParams = tuesdayHolderParams
                    tuesdayHolder.requestLayout()

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
                    val wednesdayHolderParams = wednesdayHolder.layoutParams
                    wednesdayHolder.layoutParams = wednesdayHolderParams
                    wednesdayHolder.requestLayout()

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
                    val thursdayHolderParams = thursdayHolder.layoutParams
                    thursdayHolder.layoutParams = thursdayHolderParams
                    thursdayHolder.requestLayout()

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
                    val FridayHolderParams = fridayHolder.layoutParams
                    fridayHolder.layoutParams = FridayHolderParams
                    fridayHolder.requestLayout()

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
                    FridayHolderParams.width = convertedWidth



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
                if (isAnimating == true) {
                    val saturdayHolderParams = saturdayHolder.layoutParams
                    saturdayHolder.layoutParams = saturdayHolderParams
                    saturdayHolder.requestLayout()

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
                if (isAnimating == true) {
                    val sundayHolderParams = sundayHolder.layoutParams
                    sundayHolder.layoutParams = sundayHolderParams
                    sundayHolder.requestLayout()
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


    /* override fun onResume() {
         super.onResume()
         running = true
         if(stepSensor == null){
             Log.d("sensortest", "sensor not working")
         }else{
             Log.d("Sensortest", "sensor working")
             sensorManager?.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_NORMAL)
         }

     }

     override fun onSensorChanged(p0: SensorEvent?) {
         Log.d("runningTest", running.toString())
         if(running){
             if(p0 == null){
                 Log.d("sensorTest", "Not Counting Steps")
             }
             if (p0 != null) {
                 Log.d("sensorTest", "Counting Steps")

             }
         }
     }

     override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

     }

 */

    fun addExercisesMonday(Exercise:String){
        mondayExercises.add(Exercise)
        Log.d("exerciseslist", Exercise)
    }

    override fun onExerciseAddListener(exerciseName: String) {
        Log.d("exerciseAddTest", exerciseName)
        mondayExercises.add(exerciseName)
    }

    fun setCurrentFragment(fragment : Fragment){
        if(fragment != null){
            //exerciseLayout.visibility = View.GONE
            val transaction = childFragmentManager.beginTransaction()
            transaction.replace(R.id.exercisesLayout, fragment)
            transaction.addToBackStack(null);

            transaction.commit()

        }
    }



}
