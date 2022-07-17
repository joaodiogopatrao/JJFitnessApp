package com.doublej.jjfitnessapp.ui.fragments

import com.doublej.jjfitnessapp.ui.recycleradapters.exercises.AllSearchPlanRecyclerAdapter
import com.doublej.jjfitnessapp.ui.models.exercises.SearchPlanRecyclerAdapter
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
import com.doublej.jjfitnessapp.databinding.SearchPlanBottomSheetFragmentBinding
import com.doublej.jjfitnessapp.ui.models.exercises.AllPlanSearchModel
import com.doublej.jjfitnessapp.ui.models.exercises.PlanSearchModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import nl.joery.animatedbottombar.AnimatedBottomBar

class SearchPlanBottomSheetFragment : BottomSheetDialogFragment() {

    companion object {
        fun newInstance() = SearchPlanBottomSheetFragment()
    }

    private var _binding: SearchPlanBottomSheetFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: SearchPlanBottomSheetViewModel
    private lateinit var myExercisesRecycler : RecyclerView
    private lateinit var allExercisesRecycler : RecyclerView
    private lateinit var recyclerAdapter : SearchPlanRecyclerAdapter
    private lateinit var allrecyclerAdapter : AllSearchPlanRecyclerAdapter
    private lateinit var fireBaseFirestore : FirebaseFirestore
    private lateinit var query : Query
    private lateinit var auth: FirebaseAuth
    private lateinit var subcollectionReferences : ArrayList<String>
    private lateinit var planExercisesData : ArrayList<PlanSearchModel>
    private lateinit var allplanExercisesData : ArrayList<AllPlanSearchModel>
    private lateinit var animatedbar : AnimatedBottomBar
    private lateinit var myExercisesScroll : NestedScrollView
    private lateinit var allExercisesScroll : NestedScrollView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = SearchPlanBottomSheetFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        planExercisesData = arrayListOf()
        allplanExercisesData = arrayListOf()
        subcollectionReferences = arrayListOf()
        allExercisesRecycler = root.findViewById(R.id.AllExercisesRecyler)
        myExercisesScroll = root.findViewById(R.id.myExercisesScroll)
        allExercisesScroll = root.findViewById(R.id.allExercisesScroll)
        animatedbar = root.findViewById(R.id.bottom_bar)
        myExercisesRecycler = root.findViewById(R.id.MyExercisesRecyler)
        myExercisesRecycler.layoutManager = LinearLayoutManager(this.context)
        allExercisesRecycler.layoutManager = LinearLayoutManager(this.context)
        fireBaseFirestore = FirebaseFirestore.getInstance()
        recyclerAdapter = SearchPlanRecyclerAdapter(planExercisesData)
        recyclerAdapter.setHasStableIds(true)
        recyclerAdapter.submitList(planExercisesData)
        allrecyclerAdapter = AllSearchPlanRecyclerAdapter(allplanExercisesData)
        allrecyclerAdapter.setHasStableIds(true)
        allrecyclerAdapter.submitList(allplanExercisesData)
        auth = Firebase.auth
        myExercisesRecycler.adapter = recyclerAdapter
        allExercisesRecycler.adapter = allrecyclerAdapter
        planExercisesData.clear()
        myExercisesScroll.visibility = View.VISIBLE
        allExercisesScroll.visibility = View.GONE
        var currentUser = auth.currentUser!!.uid

        recyclerAdapter.setOnItemClickListener(object : SearchPlanRecyclerAdapter.onDeleteClick{
            override fun onDeleteClick(title: CharSequence) {
                val plansRef = fireBaseFirestore.collection("Planos de exercícios").document(currentUser)
                plansRef.get()
                    .addOnSuccessListener { documents ->
                        if(documents != null){
                            val tempPlanData : ArrayList<String> = arrayListOf()
                            val tempRefs = documents["subcollectionReference"] as MutableList <*>
                            tempRefs.remove(title.toString())
                            val tempplansRef = fireBaseFirestore.collection("Planos de exercícios").document(currentUser)
                            tempplansRef.update("subcollectionReference",tempRefs).addOnSuccessListener {
                                recyclerAdapter.notifyDataSetChanged()
                                planExercisesData.clear()
                                var currentUser = auth.currentUser!!.uid
                                val plansRef = fireBaseFirestore.collection("Planos de exercícios").document(currentUser)
                                plansRef.get()
                                    .addOnSuccessListener { documents ->
                                        if(documents != null){
                                            val tempPlanData : ArrayList<String> = arrayListOf()
                                            val tempRefs = documents["subcollectionReference"] as List <*>
                                            for(i in tempRefs.size -1 downTo 0){
                                                var tempModel = PlanSearchModel(tempRefs[i].toString(), currentUser)
                                                planExercisesData.add(tempModel)
                                                recyclerAdapter.notifyDataSetChanged()
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
                            recyclerAdapter.notifyDataSetChanged()

                        }


                    }
                val allplansRef = fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                allplansRef.get()
                    .addOnSuccessListener { documents ->
                        if(documents != null){
                            val tempPlanData : ArrayList<String> = arrayListOf()
                            val tempRefs = documents["subcollectionReference"] as MutableList <*>
                            tempRefs.remove(title.toString())
                            val tempplansRef = fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                            tempplansRef.update("subcollectionReference",tempRefs).addOnSuccessListener {
                                allrecyclerAdapter.notifyDataSetChanged()
                                allplanExercisesData.clear()

                                var currentUser = auth.currentUser!!.uid
                                val plansRef = fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                                plansRef.get()
                                    .addOnSuccessListener { documents ->
                                        if(documents != null){
                                            val tempPlanData : ArrayList<String> = arrayListOf()
                                            val tempRefs = documents["subcollectionReference"] as List <*>
                                            for(i in tempRefs.size -1 downTo 0){
                                                Log.d("testeReferences01", tempRefs[i].toString())
                                                var tempModelAll = AllPlanSearchModel(tempRefs[i].toString())
                                                allplanExercisesData.add(tempModelAll)
                                                allrecyclerAdapter.notifyDataSetChanged()
                                            }

                                        }

                                    }

                            }
                            allrecyclerAdapter.notifyDataSetChanged()

                        }

                    }



            }

        })


        allplanExercisesData.clear()



        val plansRef = fireBaseFirestore.collection("Planos de exercícios").document(currentUser)
        plansRef.get()
            .addOnSuccessListener { documents ->
                if(documents != null){
                    val tempPlanData : ArrayList<String> = arrayListOf()
                    val tempRefs = documents["subcollectionReference"] as List <*>
                    for(i in tempRefs.size -1 downTo 0){
                        var tempModel = PlanSearchModel(tempRefs[i].toString(), currentUser)
                        planExercisesData.add(tempModel)
                        recyclerAdapter.notifyDataSetChanged()
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
               planExercisesData.clear()
               myExercisesScroll.visibility = View.VISIBLE
               allExercisesScroll.visibility = View.GONE
               var currentUser = auth.currentUser!!.uid
               val plansRef = fireBaseFirestore.collection("Planos de exercícios").document(currentUser)
               plansRef.get()
                   .addOnSuccessListener { documents ->
                       if(documents != null){
                           val tempPlanData : ArrayList<String> = arrayListOf()
                           val tempRefs = documents["subcollectionReference"] as List <*>
                           for(i in tempRefs.size -1 downTo 0){
                               var tempModel = PlanSearchModel(tempRefs[i].toString(), currentUser)
                               planExercisesData.add(tempModel)
                               recyclerAdapter.notifyDataSetChanged()
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
                allplanExercisesData.clear()
                myExercisesScroll.visibility = View.GONE
                allExercisesScroll.visibility = View.VISIBLE
                var currentUser = auth.currentUser!!.uid
                val plansRef = fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                plansRef.get()
                    .addOnSuccessListener { documents ->
                        if(documents != null){
                            val tempPlanData : ArrayList<String> = arrayListOf()
                            val tempRefs = documents["subcollectionReference"] as List <*>
                            for(i in tempRefs.size -1 downTo 0){
                                Log.d("testeReferences01", tempRefs[i].toString())
                                var tempModelAll = AllPlanSearchModel(tempRefs[i].toString())
                                allplanExercisesData.add(tempModelAll)
                                allrecyclerAdapter.notifyDataSetChanged()
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