package com.doublej.jjfitnessapp.ui.fragments

import com.doublej.jjfitnessapp.ui.recycleradapters.exercises.FridayRecyclerAdapter
import com.doublej.jjfitnessapp.ui.models.exercises.SaturdayRecyclerAdapter
import com.doublej.jjfitnessapp.ui.recycleradapters.exercises.SundayRecyclerAdapter
import com.doublej.jjfitnessapp.ui.recycleradapters.exercises.ThursdayRecyclerAdapter
import com.doublej.jjfitnessapp.ui.recycleradapters.exercises.TuesdayRecyclerAdapter
import com.doublej.jjfitnessapp.ui.recycleradapters.exercises.WednesdayRecyclerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.databinding.BottomSheetFragmentDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doublej.jjfitnessapp.ui.models.exercises.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import com.doublej.jjfitnessapp.ui.recycleradapters.exercises.mondayRecyclerAdapter
import kotlin.collections.ArrayList


class BottomSheetFragment : BottomSheetDialogFragment(){

    private var _binding: BottomSheetFragmentDialogBinding? = null
    private val binding get() = _binding!!
    private lateinit var query : Query
    private lateinit var db : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var fireBaseFirestore : FirebaseFirestore
    private lateinit var exerciseName : String
    private lateinit var mondayData : ArrayList<String>
    private lateinit var tuesdayData : ArrayList<String>
    private lateinit var wednesdayData : ArrayList<String>
    private lateinit var thursdayData : ArrayList<String>
    private lateinit var fridayData : ArrayList<String>
    private lateinit var saturdayData : ArrayList<String>
    private lateinit var sundayData : ArrayList<String>
    private lateinit var mondayDataExercises : Map<String,String>
    private lateinit var planExercisesMonday: ArrayList<PlanExercisesMonday>
    private lateinit var planExercisesTuesday: ArrayList<PlanExercisesTuesday>
    private lateinit var planExercisesWednesday: ArrayList<PlanExercisesWednesday>
    private lateinit var planExercisesThursday: ArrayList<PlanExercisesThursday>
    private lateinit var planExercisesFriday: ArrayList<PlanExercisesFriday>
    private lateinit var planExercisesSaturday: ArrayList<PlanExercisesSaturday>
    private lateinit var planExercisesSunday: ArrayList<PlanExercisesSunday>
    private lateinit var dayOfWeek : String
    private lateinit var mondayRecyclerView : RecyclerView
    private lateinit var mondayAdapter : mondayRecyclerAdapter
    private lateinit var tuesdayRecyclerView : RecyclerView
    private lateinit var tuesdayAdapter : TuesdayRecyclerAdapter
    private lateinit var wednesdayRecyclerView : RecyclerView
    private lateinit var wednesdayAdapter : WednesdayRecyclerAdapter
    private lateinit var thursdayRecyclerView : RecyclerView
    private lateinit var thursdayAdapter : ThursdayRecyclerAdapter
    private lateinit var fridayRecyclerView : RecyclerView
    private lateinit var fridayAdapter : FridayRecyclerAdapter
    private lateinit var saturdayRecyclerView : RecyclerView
    private lateinit var saturdayAdapter : SaturdayRecyclerAdapter
    private lateinit var sundayRecyclerView : RecyclerView
    private lateinit var sundayAdapter : SundayRecyclerAdapter
    private lateinit var createPlanButton : CardView
    private lateinit var repsArrayTest : ArrayList<Int>


    interface onExerciseAddListener {
        fun onExerciseAddListener(exerciseName: String)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = BottomSheetFragmentDialogBinding.inflate(inflater, container, false)
        val root: View = binding.root
        fireBaseFirestore = FirebaseFirestore.getInstance()
        db = FirebaseFirestore.getInstance()

        val exerciseRef = fireBaseFirestore.collection("Exercicios")
        val mondayExercises = this.arguments!!.getStringArrayList("mondayExercises")
        val tuesdayExercises = this.arguments!!.getStringArrayList("tuesdayExercises")
        val wednesdayExercises = this.arguments!!.getStringArrayList("wednesdayExercises")
        val thursdayExercises = this.arguments!!.getStringArrayList("thursdayExercises")
        val fridayExercises = this.arguments!!.getStringArrayList("fridayExercises")
        val saturdayExercises = this.arguments!!.getStringArrayList("saturdayExercises")
        val sundayExercises = this.arguments!!.getStringArrayList("sundayExercises")
        mondayData = arrayListOf()
        tuesdayData = arrayListOf()
        wednesdayData = arrayListOf()
        thursdayData = arrayListOf()
        fridayData = arrayListOf()
        saturdayData = arrayListOf()
        sundayData = arrayListOf()
        mondayDataExercises = mapOf()
        planExercisesMonday = arrayListOf()
        planExercisesTuesday = arrayListOf()
        planExercisesWednesday = arrayListOf()
        planExercisesThursday = arrayListOf()
        planExercisesFriday = arrayListOf()
        planExercisesSaturday = arrayListOf()
        planExercisesSunday = arrayListOf()
        repsArrayTest = arrayListOf()
        createPlanButton = root.findViewById(R.id.createPlanButton2)
        mondayRecyclerView = root.findViewById(R.id.mondayRecycler)
        tuesdayRecyclerView = root.findViewById(R.id.tuesdayRecycler)
        wednesdayRecyclerView = root.findViewById(R.id.wednesdayRecycler)
        thursdayRecyclerView = root.findViewById(R.id.thursdayRecycler)
        fridayRecyclerView = root.findViewById(R.id.fridayRecycler)
        saturdayRecyclerView = root.findViewById(R.id.saturdayRecycler)
        sundayRecyclerView = root.findViewById(R.id.sundayRecycler)
        mondayRecyclerView.layoutManager = LinearLayoutManager(this.context)
        tuesdayRecyclerView.layoutManager = LinearLayoutManager(this.context)
        wednesdayRecyclerView.layoutManager = LinearLayoutManager(this.context)
        thursdayRecyclerView.layoutManager = LinearLayoutManager(this.context)
        fridayRecyclerView.layoutManager = LinearLayoutManager(this.context)
        saturdayRecyclerView.layoutManager = LinearLayoutManager(this.context)
        sundayRecyclerView.layoutManager = LinearLayoutManager(this.context)
        auth = Firebase.auth
        var currentUser = auth.currentUser!!.uid
        var testTemp: ArrayList<String> = arrayListOf()
        val subcollectionReference = hashMapOf(
            "subcollectionReference" to testTemp,
        )
        val likeCounter = hashMapOf(
            "Gostos" to 0
        )

        createPlanButton.setOnClickListener {
            Log.d("testedocumento", "Document already exists.")

            fireBaseFirestore.collection("Planos de exercícios").document(currentUser).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        if(document.exists() == false){
                            fireBaseFirestore.collection("Planos de exercícios").document(currentUser)
                                .set(subcollectionReference, SetOptions.merge())
                        }
                        if(document.exists() == true){
                            Log.d("testeVariable", "continue")
                        }
                    } else {
                        Log.d("testeVariable", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("testeVariable", "get failed with ", exception)
                }

            var exerciseDataTemp: ArrayList<Int> = arrayListOf(12, 3, 120)

            var randomId: String = List(16) {
                (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
            }.joinToString("")

            for (i in mondayExercises?.size!! - 1 downTo 0) {
                val exerciseData = hashMapOf(
                    mondayExercises[i] to exerciseDataTemp,
                )

                fireBaseFirestore.collection("Planos de exercícios").document(currentUser)
                    .collection(randomId).document("Segunda-Feira")
                    .set(exerciseData, SetOptions.merge()).addOnCompleteListener { document ->


                        fireBaseFirestore.collection("Planos de exercícios").document(currentUser)
                            .update("subcollectionReference", FieldValue.arrayUnion(randomId))
                        fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                            .update("subcollectionReference", FieldValue.arrayUnion(randomId))

                    }
                fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                    .collection(randomId).document("Segunda-Feira")
                    .set(exerciseData, SetOptions.merge()).addOnSuccessListener {

                        fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                            .collection(randomId).document("Gostos").get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    if(document.exists() == false){
                                        fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                                            .collection(randomId).document("Gostos")
                                            .set(likeCounter, SetOptions.merge())
                                    }
                                    if(document.exists() == true){
                                        Log.d("testeVariable", "continue")
                                    }
                                } else {
                                    Log.d("testeVariable", "No such document")
                                }
                            }
                            .addOnFailureListener { exception ->
                                Log.d("testeVariable", "get failed with ", exception)
                            }
                    }



            }
            for (i in tuesdayExercises?.size!! - 1 downTo 0) {
                val exerciseData = hashMapOf(
                    tuesdayExercises[i] to exerciseDataTemp,
                )

                fireBaseFirestore.collection("Planos de exercícios").document(currentUser)
                    .collection(randomId).document("Terça-Feira")
                    .set(exerciseData, SetOptions.merge())

                fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                    .collection(randomId).document("Terça-Feira")
                    .set(exerciseData, SetOptions.merge())

            }
            for (i in wednesdayExercises?.size!! - 1 downTo 0) {
                val exerciseData = hashMapOf(
                    wednesdayExercises[i] to exerciseDataTemp,
                )

                fireBaseFirestore.collection("Planos de exercícios").document(currentUser)
                    .collection(randomId).document("Quarta-Feira")
                    .set(exerciseData, SetOptions.merge())

                fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                    .collection(randomId).document("Quarta-Feira")
                    .set(exerciseData, SetOptions.merge())

            }
            for (i in thursdayExercises?.size!! - 1 downTo 0) {
                val exerciseData = hashMapOf(
                    thursdayExercises[i] to exerciseDataTemp,
                )

                fireBaseFirestore.collection("Planos de exercícios").document(currentUser)
                    .collection(randomId).document("Quinta-Feira")
                    .set(exerciseData, SetOptions.merge())

                fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                    .collection(randomId).document("Quinta-Feira")
                    .set(exerciseData, SetOptions.merge())

            }
            for (i in fridayExercises?.size!! - 1 downTo 0) {
                val exerciseData = hashMapOf(
                    fridayExercises[i] to exerciseDataTemp,
                )

                fireBaseFirestore.collection("Planos de exercícios").document(currentUser)
                    .collection(randomId).document("Sexta-Feira")
                    .set(exerciseData, SetOptions.merge())

                fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                    .collection(randomId).document("Sexta-Feira")
                    .set(exerciseData, SetOptions.merge())

            }
            for (i in saturdayExercises?.size!! - 1 downTo 0) {
                val exerciseData = hashMapOf(
                    saturdayExercises[i] to exerciseDataTemp,
                )

                fireBaseFirestore.collection("Planos de exercícios").document(currentUser)
                    .collection(randomId).document("Sábado")
                    .set(exerciseData, SetOptions.merge())

                fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                    .collection(randomId).document("Sábado")
                    .set(exerciseData, SetOptions.merge())

            }
            for (i in sundayExercises?.size!! - 1 downTo 0) {
                val exerciseData = hashMapOf(
                    sundayExercises[i] to exerciseDataTemp,
                )

                fireBaseFirestore.collection("Planos de exercícios").document(currentUser)
                    .collection(randomId).document("Domingo")
                    .set(exerciseData, SetOptions.merge())

                fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                    .collection(randomId).document("Domingo")
                    .set(exerciseData, SetOptions.merge())

            }
            dismiss()
        }

        mondayAdapter = mondayRecyclerAdapter(planExercisesMonday)
        mondayAdapter.setHasStableIds(true)
        mondayAdapter.submitList(planExercisesMonday)
        mondayRecyclerView.adapter = mondayAdapter
        tuesdayAdapter = TuesdayRecyclerAdapter(planExercisesTuesday)
        tuesdayAdapter.setHasStableIds(true)
        tuesdayAdapter.submitList(planExercisesTuesday)
        tuesdayRecyclerView.adapter = tuesdayAdapter
        wednesdayAdapter = WednesdayRecyclerAdapter(planExercisesWednesday)
        wednesdayAdapter.setHasStableIds(true)
        wednesdayAdapter.submitList(planExercisesWednesday)
        wednesdayRecyclerView.adapter = wednesdayAdapter
        thursdayAdapter = ThursdayRecyclerAdapter(planExercisesThursday)
        thursdayAdapter.setHasStableIds(true)
        thursdayAdapter.submitList(planExercisesThursday)
        thursdayRecyclerView.adapter = thursdayAdapter
        fridayAdapter = FridayRecyclerAdapter(planExercisesFriday)
        fridayAdapter.setHasStableIds(true)
        fridayAdapter.submitList(planExercisesFriday)
        fridayRecyclerView.adapter = fridayAdapter
        saturdayAdapter = SaturdayRecyclerAdapter(planExercisesSaturday)
        saturdayAdapter.setHasStableIds(true)
        saturdayAdapter.submitList(planExercisesSaturday)
        saturdayRecyclerView.adapter = saturdayAdapter
        sundayAdapter = SundayRecyclerAdapter(planExercisesSunday)
        sundayAdapter.setHasStableIds(true)
        sundayAdapter.submitList(planExercisesSunday)
        sundayRecyclerView.adapter = sundayAdapter
        for (i in mondayExercises?.size!! - 1 downTo  0)
        {
            mondayData.add("8")
            mondayData.add("3")
            mondayData.add("120")

            query = exerciseRef.whereEqualTo("nomeExercicio",mondayExercises[i])
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                    if( error != null){
                        Log.d("error", "error")
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        val tempEquipamento: ArrayList<String> = ArrayList()
                        tempEquipamento.add(dc.document.get("equipamento").toString())
                        val tempExercies = PlanExercisesMonday(dc.document["nomeExercicio"].toString(),dc.document.getLong("dificuldade"),dc.document["seccao"].toString(),tempEquipamento,dc.document.getLong("id"), "Segunda")
                        planExercisesMonday.add(tempExercies)
                        mondayAdapter.notifyDataSetChanged()
                    }

                }

            })
        }
        for (i in tuesdayExercises?.size!! - 1 downTo  0)
        {
            tuesdayData.add(tuesdayExercises[i])
            tuesdayData.add("8")
            tuesdayData.add("3")
            tuesdayData.add("120")
            query = exerciseRef.whereEqualTo("nomeExercicio",tuesdayExercises[i])
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                    if( error != null){
                        Log.d("error", "error")
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        val tempEquipamento: ArrayList<String> = ArrayList()
                        tempEquipamento.add(dc.document.get("equipamento").toString())
                        val tempExercies = PlanExercisesTuesday(dc.document["nomeExercicio"].toString(),dc.document.getLong("dificuldade"),dc.document["seccao"].toString(),tempEquipamento,dc.document.getLong("id"), "Segunda")
                        planExercisesTuesday.add(tempExercies)
                        tuesdayAdapter.notifyDataSetChanged()
                    }

                }

            })
        }
        for (i in wednesdayExercises?.size!! - 1 downTo  0)
        {
            wednesdayData.add(wednesdayExercises[i])
            wednesdayData.add("8")
            wednesdayData.add("3")
            wednesdayData.add("120")
            query = exerciseRef.whereEqualTo("nomeExercicio",wednesdayExercises[i])
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                    if( error != null){
                        Log.d("error", "error")
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        val tempEquipamento: ArrayList<String> = ArrayList()
                        tempEquipamento.add(dc.document.get("equipamento").toString())
                        val tempExercies = PlanExercisesWednesday(dc.document["nomeExercicio"].toString(),dc.document.getLong("dificuldade"),dc.document["seccao"].toString(),tempEquipamento,dc.document.getLong("id"), "Segunda")
                        planExercisesWednesday.add(tempExercies)
                        wednesdayAdapter.notifyDataSetChanged()
                    }

                }

            })
        }

        for (i in thursdayExercises?.size!! - 1 downTo  0)
        {
            thursdayData.add(thursdayExercises[i])
            thursdayData.add("8")
            thursdayData.add("3")
            thursdayData.add("120")
            query = exerciseRef.whereEqualTo("nomeExercicio",thursdayExercises[i])
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                    if( error != null){
                        Log.d("error", "error")
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        val tempEquipamento: ArrayList<String> = ArrayList()
                        tempEquipamento.add(dc.document.get("equipamento").toString())
                        val tempExercies = PlanExercisesThursday(dc.document["nomeExercicio"].toString(),dc.document.getLong("dificuldade"),dc.document["seccao"].toString(),tempEquipamento,dc.document.getLong("id"), "Segunda")
                        planExercisesThursday.add(tempExercies)
                        thursdayAdapter.notifyDataSetChanged()
                    }

                }

            })
        }
        for (i in fridayExercises?.size!! - 1 downTo  0)
        {
            fridayData.add(fridayExercises[i])
            fridayData.add("8")
            fridayData.add("3")
            fridayData.add("120")
            query = exerciseRef.whereEqualTo("nomeExercicio",fridayExercises[i])
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                    if( error != null){
                        Log.d("error", "error")
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        val tempEquipamento: ArrayList<String> = ArrayList()
                        tempEquipamento.add(dc.document.get("equipamento").toString())
                        val tempExercies = PlanExercisesFriday(dc.document["nomeExercicio"].toString(),dc.document.getLong("dificuldade"),dc.document["seccao"].toString(),tempEquipamento,dc.document.getLong("id"), "Segunda")
                        planExercisesFriday.add(tempExercies)
                        fridayAdapter.notifyDataSetChanged()
                    }

                }

            })
        }
        for (i in saturdayExercises?.size!! - 1 downTo  0)
        {
            saturdayData.add(saturdayExercises[i])
            saturdayData.add("8")
            saturdayData.add("3")
            saturdayData.add("120")
            query = exerciseRef.whereEqualTo("nomeExercicio",saturdayExercises[i])
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                    if( error != null){
                        Log.d("error", "error")
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        val tempEquipamento: ArrayList<String> = ArrayList()
                        tempEquipamento.add(dc.document.get("equipamento").toString())
                        val tempExercies = PlanExercisesSaturday(dc.document["nomeExercicio"].toString(),dc.document.getLong("dificuldade"),dc.document["seccao"].toString(),tempEquipamento,dc.document.getLong("id"), "Segunda")
                        planExercisesSaturday.add(tempExercies)
                        saturdayAdapter.notifyDataSetChanged()
                    }

                }

            })
        }
        for (i in sundayExercises?.size!! - 1 downTo  0)
        {
            sundayData.add(sundayExercises[i])
            sundayData.add("8")
            sundayData.add("3")
            sundayData.add("120")
            query = exerciseRef.whereEqualTo("nomeExercicio",sundayExercises[i])
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                    if( error != null){
                        Log.d("error", "error")
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        val tempEquipamento: ArrayList<String> = ArrayList()
                        tempEquipamento.add(dc.document.get("equipamento").toString())
                        val tempExercies = PlanExercisesSunday(dc.document["nomeExercicio"].toString(),dc.document.getLong("dificuldade"),dc.document["seccao"].toString(),tempEquipamento,dc.document.getLong("id"), "Segunda")
                        planExercisesSunday.add(tempExercies)
                        sundayAdapter.notifyDataSetChanged()
                    }

                }

            })
        }
        fireBaseFirestore = FirebaseFirestore.getInstance()
        fireBaseFirestore.clearPersistence()
        val settings = firestoreSettings {
            isPersistenceEnabled = false
        }
        return root
    }
}