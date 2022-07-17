package com.doublej.jjfitnessapp.ui.fragments

import com.doublej.jjfitnessapp.ui.recycleradapters.nutrition.AllSearchNutritionPlanRecyclerAdapter
import com.doublej.jjfitnessapp.ui.recycleradapters.nutrition.SearchNutritionPlanRecyclerAdapter
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.databinding.SearchNutritionPlanBottomSheetFragmentBinding
import com.doublej.jjfitnessapp.ui.models.nutrition.AllPlanNutritionSearchModel
import com.doublej.jjfitnessapp.ui.models.nutrition.PlanNutritionSearchModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import nl.joery.animatedbottombar.AnimatedBottomBar

class SearchNutritionPlanBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = SearchNutritionPlanBottomSheetFragment()
    }

    private var _binding: SearchNutritionPlanBottomSheetFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchPlanBottomSheetViewModel
    private lateinit var myFoodsRecycler : RecyclerView
    private lateinit var allFoodsRecycler : RecyclerView
    private lateinit var recyclerNutritionAdapter : SearchNutritionPlanRecyclerAdapter
    private lateinit var allrecyclerNutritionAdapter : AllSearchNutritionPlanRecyclerAdapter
    private lateinit var fireBaseFirestore : FirebaseFirestore
    private lateinit var query : Query
    private lateinit var auth: FirebaseAuth
    private lateinit var subcollectionReferences : ArrayList<String>
    private lateinit var planFoodsData : ArrayList<PlanNutritionSearchModel>
    private lateinit var allplanFoodsData : ArrayList<AllPlanNutritionSearchModel>
    private lateinit var animatedbar : AnimatedBottomBar
    private lateinit var myFoodsScroll : NestedScrollView
    private lateinit var allFoodsScroll : NestedScrollView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchNutritionPlanBottomSheetFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        planFoodsData = arrayListOf()
        allplanFoodsData = arrayListOf()
        subcollectionReferences = arrayListOf()
        allFoodsRecycler = root.findViewById(R.id.AllFoodsRecyler)
        myFoodsScroll = root.findViewById(R.id.myFoodsScroll)
        allFoodsScroll = root.findViewById(R.id.allFoodsScroll)
        animatedbar = root.findViewById(R.id.bottom_barnutrition)
        myFoodsRecycler = root.findViewById(R.id.MyFoodsRecyler)
        myFoodsRecycler.layoutManager = LinearLayoutManager(this.context)
        allFoodsRecycler.layoutManager = LinearLayoutManager(this.context)
        fireBaseFirestore = FirebaseFirestore.getInstance()
        recyclerNutritionAdapter = SearchNutritionPlanRecyclerAdapter(planFoodsData)
        recyclerNutritionAdapter.setHasStableIds(true)
        recyclerNutritionAdapter.submitList(planFoodsData)
        allrecyclerNutritionAdapter = AllSearchNutritionPlanRecyclerAdapter(allplanFoodsData)
        allrecyclerNutritionAdapter.setHasStableIds(true)
        allrecyclerNutritionAdapter.submitList(allplanFoodsData)
        auth = Firebase.auth
        myFoodsRecycler.adapter = recyclerNutritionAdapter
        allFoodsRecycler.adapter = allrecyclerNutritionAdapter
        planFoodsData.clear()
        myFoodsScroll.visibility = View.VISIBLE
        allFoodsScroll.visibility = View.GONE
        var currentUser = auth.currentUser!!.uid

        recyclerNutritionAdapter.setOnItemClickListener(object : SearchNutritionPlanRecyclerAdapter.onDeleteClick{
            override fun onDeleteClick(title: CharSequence) {
                val plansRef = fireBaseFirestore.collection("Planos de alimentação").document(currentUser)
                plansRef.get()
                    .addOnSuccessListener { documents ->
                        if(documents != null){
                            val tempPlanData : ArrayList<String> = arrayListOf()
                            val tempRefs = documents["subcollectionReference"] as MutableList <*>
                            tempRefs.remove(title.toString())
                            val tempplansRef = fireBaseFirestore.collection("Planos de alimentação").document(currentUser)
                            tempplansRef.update("subcollectionReference",tempRefs).addOnSuccessListener {
                                recyclerNutritionAdapter.notifyDataSetChanged()
                                planFoodsData.clear()
                                var currentUser = auth.currentUser!!.uid
                                val plansRef = fireBaseFirestore.collection("Planos de alimentação").document(currentUser)
                                plansRef.get()
                                    .addOnSuccessListener { documents ->
                                        if(documents != null){
                                            val tempPlanData : ArrayList<String> = arrayListOf()
                                            val tempRefs = documents["subcollectionReference"] as List <*>
                                            for(i in tempRefs.size -1 downTo 0){
                                                var tempModel = PlanNutritionSearchModel(tempRefs[i].toString(), currentUser)
                                                planFoodsData.add(tempModel)
                                                recyclerNutritionAdapter.notifyDataSetChanged()
                                                plansRef.collection(tempRefs[i].toString()).document("Segunda-Feira").get()
                                                    .addOnSuccessListener { document ->
                                                        if(document !=null){
                                                            val tempArray : ArrayList<String> = arrayListOf()
                                                            document.data?.let { data ->
                                                                data.forEach { (key, _) ->

                                                                }

                                                            }
                                                        }
                                                        if(document == null){
                                                            Log.d("ultimatetest", "fail")
                                                        }

                                                    }
                                            }

                                        }

                                    }
                            }
                            recyclerNutritionAdapter.notifyDataSetChanged()

                        }


                    }
                val allplansRef = fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                allplansRef.get()
                    .addOnSuccessListener { documents ->
                        if(documents != null){
                            val tempPlanData : ArrayList<String> = arrayListOf()
                            val tempRefs = documents["subcollectionReference"] as MutableList <*>
                            tempRefs.remove(title.toString())
                            val tempplansRef = fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                            tempplansRef.update("subcollectionReference",tempRefs).addOnSuccessListener {
                                recyclerNutritionAdapter.notifyDataSetChanged()
                                allplanFoodsData.clear()

                                var currentUser = auth.currentUser!!.uid
                                val plansRef = fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                                plansRef.get()
                                    .addOnSuccessListener { documents ->
                                        if(documents != null){
                                            val tempPlanData : ArrayList<String> = arrayListOf()
                                            val tempRefs = documents["subcollectionReference"] as List <*>
                                            for(i in tempRefs.size -1 downTo 0){
                                                Log.d("testeReferences01", tempRefs[i].toString())
                                                var tempModelAll = AllPlanNutritionSearchModel(tempRefs[i].toString())
                                                allplanFoodsData.add(tempModelAll)
                                                allrecyclerNutritionAdapter.notifyDataSetChanged()
                                            }

                                        }

                                    }

                            }
                            allrecyclerNutritionAdapter.notifyDataSetChanged()

                        }

                    }



            }

        })


        allplanFoodsData.clear()



        val plansRef = fireBaseFirestore.collection("Planos de alimentação").document(currentUser)
        plansRef.get()
            .addOnSuccessListener { documents ->
                if(documents != null){
                    val tempPlanData : ArrayList<String> = arrayListOf()
                    val tempRefs = documents["subcollectionReference"] as List <*>
                    for(i in tempRefs.size -1 downTo 0){
                        var tempModel = PlanNutritionSearchModel(tempRefs[i].toString(), currentUser)
                        planFoodsData.add(tempModel)
                        recyclerNutritionAdapter.notifyDataSetChanged()
                        plansRef.collection(tempRefs[i].toString()).document("Segunda-Feira").get()
                            .addOnSuccessListener { document ->
                                if(document !=null){
                                    val tempArray : ArrayList<String> = arrayListOf()
                                    document.data?.let { data ->
                                        data.forEach { (key, _) ->


                                        }

                                    }
                                }
                                if(document == null){
                                    Log.d("ultimatetest", "fail")
                                }

                            }
                    }

                }

            }
        animatedbar.onTabSelected = {
            if(it.title == "Os meus planos"){
                // recyclerAdapter.notifyDataSetChanged()
                planFoodsData.clear()
                myFoodsScroll.visibility = View.VISIBLE
                allFoodsScroll.visibility = View.GONE
                var currentUser = auth.currentUser!!.uid
                val plansRef = fireBaseFirestore.collection("Planos de alimentação").document(currentUser)
                plansRef.get()
                    .addOnSuccessListener { documents ->
                        if(documents != null){
                            val tempPlanData : ArrayList<String> = arrayListOf()
                            val tempRefs = documents["subcollectionReference"] as List <*>
                            for(i in tempRefs.size -1 downTo 0){
                                var tempModel = PlanNutritionSearchModel(tempRefs[i].toString(), currentUser)
                                planFoodsData.add(tempModel)
                                recyclerNutritionAdapter.notifyDataSetChanged()
                                plansRef.collection(tempRefs[i].toString()).document("Segunda-Feira").get()
                                    .addOnSuccessListener { document ->
                                        if(document !=null){
                                            val tempArray : ArrayList<String> = arrayListOf()
                                            document.data?.let { data ->
                                                data.forEach { (key, _) ->


                                                }

                                            }
                                        }
                                        if(document == null){
                                            Log.d("ultimatetest", "fail")
                                        }

                                    }
                            }

                        }

                    }
            }
            if(it.title == "Procurar planos"){
                //allrecyclerAdapter.notifyDataSetChanged()
                allplanFoodsData.clear()
                myFoodsScroll.visibility = View.GONE
                allFoodsScroll.visibility = View.VISIBLE
                var currentUser = auth.currentUser!!.uid
                val plansRef = fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                plansRef.get()
                    .addOnSuccessListener { documents ->
                        if(documents != null){
                            val tempPlanData : ArrayList<String> = arrayListOf()
                            val tempRefs = documents["subcollectionReference"] as List <*>
                            for(i in tempRefs.size -1 downTo 0){
                                Log.d("testeReferences01", tempRefs[i].toString())
                                var tempModelAll = AllPlanNutritionSearchModel(tempRefs[i].toString())
                                allplanFoodsData.add(tempModelAll)
                                allrecyclerNutritionAdapter.notifyDataSetChanged()
                            }

                        }

                    }

            }
        }






        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SearchPlanBottomSheetViewModel::class.java)
        // TODO: Use the ViewModel
    }



}