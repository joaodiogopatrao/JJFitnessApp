package com.doublej.jjfitnessapp.ui.fragments

import com.doublej.jjfitnessapp.ui.recycleradapters.nutrition.FridayFoodRecyclerAdapter
import com.doublej.jjfitnessapp.ui.recycleradapters.nutrition.MondayFoodRecyclerAdapter
import com.doublej.jjfitnessapp.ui.recycleradapters.nutrition.SaturdayFoodRecyclerAdapter
import com.doublej.jjfitnessapp.ui.recycleradapters.nutrition.SundayFoodRecyclerAdapter
import com.doublej.jjfitnessapp.ui.recycleradapters.nutrition.ThursdayFoodRecyclerAdapter
import com.doublej.jjfitnessapp.ui.recycleradapters.nutrition.TuesdayFoodRecyclerAdapter
import com.doublej.jjfitnessapp.ui.recycleradapters.nutrition.WednesdayFoodRecyclerAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.doublej.jjfitnessapp.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doublej.jjfitnessapp.databinding.BottomSheetNutritionFragmentBinding
import com.doublej.jjfitnessapp.ui.models.exercises.ExerciseSchedule
import com.doublej.jjfitnessapp.ui.models.nutrition.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.*
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.ktx.firestoreSettings
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList


class BottomSheetNutritionFragment: BottomSheetDialogFragment(){

    private var _binding: BottomSheetNutritionFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var query : Query
    private lateinit var db : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var fireBaseFirestore : FirebaseFirestore
    private lateinit var mondayData : ArrayList<String>
    private lateinit var tuesdayData : ArrayList<String>
    private lateinit var wednesdayData : ArrayList<String>
    private lateinit var thursdayData : ArrayList<String>
    private lateinit var fridayData : ArrayList<String>
    private lateinit var saturdayData : ArrayList<String>
    private lateinit var sundayData : ArrayList<String>
    private lateinit var mondayDataExercises : Map<String,String>
    private lateinit var planFoodMonday: ArrayList<PlanFoodMonday>
    private lateinit var planFoodTuesday: ArrayList<PlanFoodTuesday>
    private lateinit var planFoodWednesday: ArrayList<PlanFoodWednesday>
    private lateinit var planFoodThursday: ArrayList<PlanFoodThursday>
    private lateinit var planFoodFriday: ArrayList<PlanFoodFriday>
    private lateinit var planFoodSaturday: ArrayList<PlanFoodSaturday>
    private lateinit var planFoodSunday: ArrayList<PlanFoodSunday>
    private lateinit var mondayFoodRecyclerView : RecyclerView
    private lateinit var mondayFoodAdapter : MondayFoodRecyclerAdapter
    private lateinit var tuesdayFoodRecyclerView : RecyclerView
    private lateinit var tuesdayFoodAdapter : TuesdayFoodRecyclerAdapter
    private lateinit var wednesdayFoodRecyclerView : RecyclerView
    private lateinit var wednesdayFoodAdapter : WednesdayFoodRecyclerAdapter
    private lateinit var thursdayFoodRecyclerView : RecyclerView
    private lateinit var thursdayFoodAdapter : ThursdayFoodRecyclerAdapter
    private lateinit var fridayFoodRecyclerView : RecyclerView
    private lateinit var fridayFoodAdapter : FridayFoodRecyclerAdapter
    private lateinit var saturdayFoodRecyclerView : RecyclerView
    private lateinit var saturdayFoodAdapter : SaturdayFoodRecyclerAdapter
    private lateinit var sundayFoodRecyclerView : RecyclerView
    private lateinit var sundayFoodAdapter : SundayFoodRecyclerAdapter
    private lateinit var createPlanButton : CardView
    private lateinit var repsArrayTest : ArrayList<Int>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = BottomSheetNutritionFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        fireBaseFirestore = FirebaseFirestore.getInstance()
        db = FirebaseFirestore.getInstance()
        val foodRef = fireBaseFirestore.collection("Pratos")
        val mondayFood = this.arguments!!.getStringArrayList("mondayFoods")
        val tuesdayFood = this.arguments!!.getStringArrayList("tuesdayFoods")
        val wednesdayFood = this.arguments!!.getStringArrayList("wednesdayFoods")
        val thursdayFood = this.arguments!!.getStringArrayList("thursdayFoods")
        val fridayFood = this.arguments!!.getStringArrayList("fridayFoods")
        val saturdayFood = this.arguments!!.getStringArrayList("saturdayFoods")
        val sundayFood = this.arguments!!.getStringArrayList("sundayFoods")
        mondayData = arrayListOf()
        tuesdayData = arrayListOf()
        wednesdayData = arrayListOf()
        thursdayData = arrayListOf()
        fridayData = arrayListOf()
        saturdayData = arrayListOf()
        sundayData = arrayListOf()
        mondayDataExercises = mapOf()
        planFoodMonday = arrayListOf()
        planFoodTuesday = arrayListOf()
        planFoodWednesday = arrayListOf()
        planFoodThursday = arrayListOf()
        planFoodFriday = arrayListOf()
        planFoodSaturday = arrayListOf()
        planFoodSunday = arrayListOf()
        repsArrayTest = arrayListOf()
        createPlanButton = root.findViewById(R.id.createPlanButton2nutrition)
        mondayFoodRecyclerView = root.findViewById(R.id.mondayRecyclernutrition)
        tuesdayFoodRecyclerView = root.findViewById(R.id.tuesdayRecyclernutrition)
        wednesdayFoodRecyclerView = root.findViewById(R.id.wednesdayRecyclernutrition)
        thursdayFoodRecyclerView = root.findViewById(R.id.thursdayRecyclernutrition)
        fridayFoodRecyclerView = root.findViewById(R.id.fridayRecyclernutrition)
        saturdayFoodRecyclerView = root.findViewById(R.id.saturdayRecyclernutrition)
        sundayFoodRecyclerView = root.findViewById(R.id.sundayRecyclernutrition)
        mondayFoodRecyclerView.layoutManager = LinearLayoutManager(this.context)
        tuesdayFoodRecyclerView.layoutManager = LinearLayoutManager(this.context)
        wednesdayFoodRecyclerView.layoutManager = LinearLayoutManager(this.context)
        thursdayFoodRecyclerView.layoutManager = LinearLayoutManager(this.context)
        fridayFoodRecyclerView.layoutManager = LinearLayoutManager(this.context)
        saturdayFoodRecyclerView.layoutManager = LinearLayoutManager(this.context)
        sundayFoodRecyclerView.layoutManager = LinearLayoutManager(this.context)
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

            fireBaseFirestore.collection("Planos de alimentação").document(currentUser).get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        if(document.exists() == false){
                            Log.d("testnutrition", "entrou1")
                            fireBaseFirestore.collection("Planos de alimentação").document(currentUser)
                                .set(subcollectionReference, SetOptions.merge())
                        }
                        if(document.exists() == true){
                            Log.d("testnutrition", "entrou2")
                            Log.d("testeVariable", "continue")
                        }
                    } else {
                        Log.d("testeVariable", "No such document")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d("testeVariable", "get failed with ", exception)
                }

            var randomId: String = List(16) {
                (('a'..'z') + ('A'..'Z') + ('0'..'9')).random()
            }.joinToString("")

            for (i in mondayFood?.size!! - 1 downTo 0) {
                val foodData = hashMapOf(
                    mondayFood[i] to "",
                )

                fireBaseFirestore.collection("Planos de alimentação").document(currentUser)
                    .collection(randomId).document("Segunda-Feira")
                    .set(foodData, SetOptions.merge()).addOnCompleteListener { document ->


                        fireBaseFirestore.collection("Planos de alimentação").document(currentUser)
                            .update("subcollectionReference", FieldValue.arrayUnion(randomId))
                        fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                            .update("subcollectionReference", FieldValue.arrayUnion(randomId))

                    }
                fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                    .collection(randomId).document("Segunda-Feira")
                    .set(foodData, SetOptions.merge()).addOnSuccessListener {

                        fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                            .collection(randomId).document("Gostos").get()
                            .addOnSuccessListener { document ->
                                if (document != null) {
                                    if(document.exists() == false){
                                        fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
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
            for (i in tuesdayFood?.size!! - 1 downTo 0) {
                val foodData = hashMapOf(
                    tuesdayFood[i] to "",
                )

                fireBaseFirestore.collection("Planos de alimentação").document(currentUser)
                    .collection(randomId).document("Terça-Feira")
                    .set(foodData, SetOptions.merge())

                fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                    .collection(randomId).document("Terça-Feira")
                    .set(foodData, SetOptions.merge())

            }
            for (i in wednesdayFood?.size!! - 1 downTo 0) {
                val foodData = hashMapOf(
                    wednesdayFood[i] to "",
                )

                fireBaseFirestore.collection("Planos de alimentação").document(currentUser)
                    .collection(randomId).document("Quarta-Feira")
                    .set(foodData, SetOptions.merge())

                fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                    .collection(randomId).document("Quarta-Feira")
                    .set(foodData, SetOptions.merge())

            }
            for (i in thursdayFood?.size!! - 1 downTo 0) {
                val foodData = hashMapOf(
                    thursdayFood[i] to "",
                )

                fireBaseFirestore.collection("Planos de alimentação").document(currentUser)
                    .collection(randomId).document("Quinta-Feira")
                    .set(foodData, SetOptions.merge())

                fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                    .collection(randomId).document("Quinta-Feira")
                    .set(foodData, SetOptions.merge())

            }
            for (i in fridayFood?.size!! - 1 downTo 0) {
                val foodData = hashMapOf(
                    fridayFood[i] to "",
                )

                fireBaseFirestore.collection("Planos de alimentação").document(currentUser)
                    .collection(randomId).document("Sexta-Feira")
                    .set(foodData, SetOptions.merge())

                fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                    .collection(randomId).document("Sexta-Feira")
                    .set(foodData, SetOptions.merge())

            }
            for (i in saturdayFood?.size!! - 1 downTo 0) {
                val foodData = hashMapOf(
                    saturdayFood[i] to "",
                )

                fireBaseFirestore.collection("Planos de alimentação").document(currentUser)
                    .collection(randomId).document("Sábado")
                    .set(foodData, SetOptions.merge())

                fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                    .collection(randomId).document("Sábado")
                    .set(foodData, SetOptions.merge())

            }
            for (i in sundayFood?.size!! - 1 downTo 0) {
                val foodData = hashMapOf(
                    sundayFood[i] to "",
                )

                fireBaseFirestore.collection("Planos de alimentação").document(currentUser)
                    .collection(randomId).document("Domingo")
                    .set(foodData, SetOptions.merge())

                fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                    .collection(randomId).document("Domingo")
                    .set(foodData, SetOptions.merge())

            }
            dismiss()
        }
        mondayFoodAdapter = MondayFoodRecyclerAdapter(planFoodMonday)
        mondayFoodAdapter.setHasStableIds(true)
        mondayFoodAdapter.submitList(planFoodMonday)
        mondayFoodRecyclerView.adapter = mondayFoodAdapter
        tuesdayFoodAdapter = TuesdayFoodRecyclerAdapter(planFoodTuesday)
        tuesdayFoodAdapter.setHasStableIds(true)
        tuesdayFoodAdapter.submitList(planFoodTuesday)
        tuesdayFoodRecyclerView.adapter = tuesdayFoodAdapter
        wednesdayFoodAdapter = WednesdayFoodRecyclerAdapter(planFoodWednesday)
        wednesdayFoodAdapter.setHasStableIds(true)
        wednesdayFoodAdapter.submitList(planFoodWednesday)
        wednesdayFoodRecyclerView.adapter = wednesdayFoodAdapter
        thursdayFoodAdapter = ThursdayFoodRecyclerAdapter(planFoodThursday)
        thursdayFoodAdapter.setHasStableIds(true)
        thursdayFoodAdapter.submitList(planFoodThursday)
        thursdayFoodRecyclerView.adapter = thursdayFoodAdapter
        fridayFoodAdapter = FridayFoodRecyclerAdapter(planFoodFriday)
        fridayFoodAdapter.setHasStableIds(true)
        fridayFoodAdapter.submitList(planFoodFriday)
        fridayFoodRecyclerView.adapter = fridayFoodAdapter
        saturdayFoodAdapter = SaturdayFoodRecyclerAdapter(planFoodSaturday)
        saturdayFoodAdapter.setHasStableIds(true)
        saturdayFoodAdapter.submitList(planFoodSaturday)
        saturdayFoodRecyclerView.adapter = saturdayFoodAdapter
        sundayFoodAdapter = SundayFoodRecyclerAdapter(planFoodSunday)
        sundayFoodAdapter.setHasStableIds(true)
        sundayFoodAdapter.submitList(planFoodSunday)
        sundayFoodRecyclerView.adapter = sundayFoodAdapter
        for (i in mondayFood?.size!! - 1 downTo  0)
        {

            query = foodRef.whereEqualTo("nomePrato",mondayFood[i])
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                    if( error != null){
                        Log.d("error", "error")
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        val tempFood = PlanFoodMonday(dc.document["nomePrato"].toString(),dc.document.getLong("Calorias"),dc.document.getLong("Carbohidratos"),dc.document.getLong("Proteinas"),dc.document.getLong("Gorduras"),dc.document.getLong("id"), "Segunda")
                        planFoodMonday.add(tempFood)
                        mondayFoodAdapter.notifyDataSetChanged()
                    }

                }

            })
        }
        for (i in tuesdayFood?.size!! - 1 downTo  0)
        {
            tuesdayData.add(tuesdayFood[i])
            tuesdayData.add("8")
            tuesdayData.add("3")
            tuesdayData.add("120")
            query = foodRef.whereEqualTo("nomeExercicio",tuesdayFood[i])
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                    if( error != null){
                        Log.d("error", "error")
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        val tempEquipamento: ArrayList<String> = ArrayList()
                        tempEquipamento.add(dc.document.get("equipamento").toString())
                        val tempFood = PlanFoodTuesday(dc.document["nomePrato"].toString(),dc.document.getLong("Calorias"),dc.document.getLong("Carbohidratos"),dc.document.getLong("Proteinas"),dc.document.getLong("Gorduras"),dc.document.getLong("id"), "Terça")
                        planFoodTuesday.add(tempFood)
                        tuesdayFoodAdapter.notifyDataSetChanged()
                    }

                }

            })
        }
        for (i in wednesdayFood?.size!! - 1 downTo  0)
        {
            wednesdayData.add(wednesdayFood[i])
            wednesdayData.add("8")
            wednesdayData.add("3")
            wednesdayData.add("120")
            query = foodRef.whereEqualTo("nomePrato",wednesdayFood[i])
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                    if( error != null){
                        Log.d("error", "error")
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        val tempEquipamento: ArrayList<String> = ArrayList()
                        tempEquipamento.add(dc.document.get("equipamento").toString())
                        val tempFood = PlanFoodWednesday(dc.document["nomePrato"].toString(),dc.document.getLong("Calorias"),dc.document.getLong("Carbohidratos"),dc.document.getLong("Proteinas"),dc.document.getLong("Gorduras"),dc.document.getLong("id"), "Quarta")
                        planFoodWednesday.add(tempFood)
                        wednesdayFoodAdapter.notifyDataSetChanged()
                    }

                }

            })
        }

        for (i in thursdayFood?.size!! - 1 downTo  0)
        {
            thursdayData.add(thursdayFood[i])
            thursdayData.add("8")
            thursdayData.add("3")
            thursdayData.add("120")
            query = foodRef.whereEqualTo("nomePrato",thursdayFood[i])
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                    if( error != null){
                        Log.d("error", "error")
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        val tempEquipamento: ArrayList<String> = ArrayList()
                        tempEquipamento.add(dc.document.get("equipamento").toString())
                        val tempFood = PlanFoodThursday(dc.document["nomePrato"].toString(),dc.document.getLong("Calorias"),dc.document.getLong("Carbohidratos"),dc.document.getLong("Proteinas"),dc.document.getLong("Gorduras"),dc.document.getLong("id"), "Quinta")
                        planFoodThursday.add(tempFood)
                        thursdayFoodAdapter.notifyDataSetChanged()
                    }

                }

            })
        }
        for (i in fridayFood?.size!! - 1 downTo  0)
        {
            fridayData.add(fridayFood[i])
            fridayData.add("8")
            fridayData.add("3")
            fridayData.add("120")
            query = foodRef.whereEqualTo("nomePrato",fridayFood[i])
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                    if( error != null){
                        Log.d("error", "error")
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        val tempEquipamento: ArrayList<String> = ArrayList()
                        tempEquipamento.add(dc.document.get("equipamento").toString())
                        val tempFood = PlanFoodFriday(dc.document["nomePrato"].toString(),dc.document.getLong("Calorias"),dc.document.getLong("Carbohidratos"),dc.document.getLong("Proteinas"),dc.document.getLong("Gorduras"),dc.document.getLong("id"), "Sexta")
                        planFoodFriday.add(tempFood)
                        fridayFoodAdapter.notifyDataSetChanged()
                    }

                }

            })
        }
        for (i in saturdayFood?.size!! - 1 downTo  0)
        {

            query = foodRef.whereEqualTo("nomePrato",saturdayFood[i])
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                    if( error != null){
                        Log.d("error", "error")
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        val tempEquipamento: ArrayList<String> = ArrayList()
                        tempEquipamento.add(dc.document.get("equipamento").toString())
                        val tempFood = PlanFoodSaturday(dc.document["nomePrato"].toString(),dc.document.getLong("Calorias"),dc.document.getLong("Carbohidratos"),dc.document.getLong("Proteinas"),dc.document.getLong("Gorduras"),dc.document.getLong("id"), "Sabado")
                        planFoodSaturday.add(tempFood)
                        saturdayFoodAdapter.notifyDataSetChanged()
                    }

                }

            })
        }
        for (i in sundayFood?.size!! - 1 downTo  0)
        {
            sundayData.add("8")
            sundayData.add("3")
            sundayData.add("120")
            query = foodRef.whereEqualTo("nomePrato",sundayFood[i])
            query.addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {


                    if( error != null){
                        Log.d("error", "error")
                        return
                    }
                    for(dc : DocumentChange in value?.documentChanges!!){
                        val tempEquipamento: ArrayList<String> = ArrayList()
                        tempEquipamento.add(dc.document.get("equipamento").toString())
                        val tempFood = PlanFoodSunday(dc.document["nomePrato"].toString(),dc.document.getLong("Calorias"),dc.document.getLong("Carbohidratos"),dc.document.getLong("Proteinas"),dc.document.getLong("Gorduras"),dc.document.getLong("id"), "Domingo")
                        planFoodSunday.add(tempFood)
                        sundayFoodAdapter.notifyDataSetChanged()
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