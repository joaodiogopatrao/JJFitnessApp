package com.doublej.jjfitnessapp.ui.models.exercises

import android.transition.AutoTransition
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.ui.recycleradapters.exercises.*

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class SearchPlanRecyclerAdapter(var plans: ArrayList<PlanSearchModel>) : ListAdapter<PlanSearchModel, SearchPlanRecyclerAdapter.ViewHolder>(
    ExerciseDiffUtil()
) {

    private lateinit var mListener : onDeleteClick
    private lateinit var fireBaseFirestore : FirebaseFirestore
    private var creatorUsername : String = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser : String
    private lateinit var planExercisesMonday: ArrayList<SearchPlanMondayExercises>
    private lateinit var planExercisesTuesday: ArrayList<SearchPlanTuesdayExercises>
    private lateinit var planExercisesWednesday: ArrayList<SearchPlanWednesdayExercises>
    private lateinit var planExercisesThursday: ArrayList<SearchPlanThursdayExercises>
    private lateinit var planExercisesFriday: ArrayList<SearchPlanFridayExercises>
    private lateinit var planExercisesSaturday: ArrayList<SearchPlanSaturdayExercises>
    private lateinit var planExercisesSunday: ArrayList<SearchPlanSundayExercises>


    interface onDeleteClick{
        fun onDeleteClick(title : CharSequence)
    }

    fun setOnItemClickListener(listener: onDeleteClick){
        mListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.search_plan_card,parent,false)
        fireBaseFirestore = FirebaseFirestore.getInstance()
        auth = Firebase.auth
        currentUser = auth.currentUser!!.uid
        planExercisesMonday = arrayListOf()
        planExercisesTuesday = arrayListOf()
        planExercisesWednesday = arrayListOf()
        planExercisesThursday = arrayListOf()
        planExercisesFriday = arrayListOf()
        planExercisesSaturday = arrayListOf()
        planExercisesSunday = arrayListOf()
        Log.d("testValuePlan", "Ok")



        return ViewHolder(v,mListener)
    }





    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plan_search: PlanSearchModel = getItem(position)

        holder.mondayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
        holder.mondayAdapter = SearchMondayRecyclerAdapter(planExercisesMonday)
        holder.mondayAdapter.setHasStableIds(true)
        holder.mondayAdapter.submitList(planExercisesMonday)
        holder.mondayRecycler.adapter = holder.mondayAdapter
        holder.tuesdayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
        holder.tuesdayAdapter = SearchTuesdayRecyclerAdapter(planExercisesTuesday)
        holder.tuesdayAdapter.setHasStableIds(true)
        holder.tuesdayAdapter.submitList(planExercisesTuesday)
        holder.tuesdayRecycler.adapter = holder.tuesdayAdapter
        holder.wednesdayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
        holder.wednesdayAdapter = SearchWednesdayRecyclerAdapter(planExercisesWednesday)
        holder.wednesdayAdapter.setHasStableIds(true)
        holder.wednesdayAdapter.submitList(planExercisesWednesday)
        holder.wednesdayRecycler.adapter = holder.wednesdayAdapter
        holder.thursdayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
        holder.thursdayAdapter = SearchThursdayRecyclerAdapter(planExercisesThursday)
        holder.thursdayAdapter.setHasStableIds(true)
        holder.thursdayAdapter.submitList(planExercisesThursday)
        holder.thursdayRecycler.adapter = holder.thursdayAdapter
        holder.fridayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
        holder.fridayAdapter = SearchFridayRecyclerAdapter(planExercisesFriday)
        holder.fridayAdapter.setHasStableIds(true)
        holder.fridayAdapter.submitList(planExercisesFriday)
        holder.fridayRecycler.adapter = holder.fridayAdapter
        holder.saturdayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
        holder.saturdayAdapter = SearchSaturdayRecyclerAdapter(planExercisesSaturday)
        holder.saturdayAdapter.setHasStableIds(true)
        holder.saturdayAdapter.submitList(planExercisesSaturday)
        holder.saturdayRecycler.adapter = holder.saturdayAdapter
        holder.sundayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
        holder.sundayAdapter = SearchSundayRecyclerAdapter(planExercisesSunday)
        holder.sundayAdapter.setHasStableIds(true)
        holder.sundayAdapter.submitList(planExercisesSunday)
        holder.sundayRecycler.adapter = holder.sundayAdapter

        holder.cardHeader.setOnClickListener {
            planExercisesMonday.clear()
            planExercisesTuesday.clear()
            planExercisesWednesday.clear()
            planExercisesThursday.clear()
            planExercisesFriday.clear()
            planExercisesSaturday.clear()
            planExercisesSunday.clear()
            holder.mondayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
            holder.mondayAdapter = SearchMondayRecyclerAdapter(planExercisesMonday)
            holder.mondayAdapter.setHasStableIds(true)
            holder.mondayAdapter.submitList(planExercisesMonday)
            holder.mondayRecycler.adapter = holder.mondayAdapter
            holder.tuesdayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
            holder.tuesdayAdapter = SearchTuesdayRecyclerAdapter(planExercisesTuesday)
            holder.tuesdayAdapter.setHasStableIds(true)
            holder.tuesdayAdapter.submitList(planExercisesTuesday)
            holder.tuesdayRecycler.adapter = holder.tuesdayAdapter
            holder.wednesdayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
            holder.wednesdayAdapter = SearchWednesdayRecyclerAdapter(planExercisesWednesday)
            holder.wednesdayAdapter.setHasStableIds(true)
            holder.wednesdayAdapter.submitList(planExercisesWednesday)
            holder.wednesdayRecycler.adapter = holder.wednesdayAdapter
            holder.thursdayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
            holder.thursdayAdapter = SearchThursdayRecyclerAdapter(planExercisesThursday)
            holder.thursdayAdapter.setHasStableIds(true)
            holder.thursdayAdapter.submitList(planExercisesThursday)
            holder.thursdayRecycler.adapter = holder.thursdayAdapter
            holder.fridayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
            holder.fridayAdapter = SearchFridayRecyclerAdapter(planExercisesFriday)
            holder.fridayAdapter.setHasStableIds(true)
            holder.fridayAdapter.submitList(planExercisesFriday)
            holder.fridayRecycler.adapter = holder.fridayAdapter
            holder.saturdayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
            holder.saturdayAdapter = SearchSaturdayRecyclerAdapter(planExercisesSaturday)
            holder.saturdayAdapter.setHasStableIds(true)
            holder.saturdayAdapter.submitList(planExercisesSaturday)
            holder.saturdayRecycler.adapter = holder.saturdayAdapter
            holder.sundayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
            holder.sundayAdapter = SearchSundayRecyclerAdapter(planExercisesSunday)
            holder.sundayAdapter.setHasStableIds(true)
            holder.sundayAdapter.submitList(planExercisesSunday)
            holder.sundayRecycler.adapter = holder.sundayAdapter
            holder.mondayAdapter.notifyDataSetChanged()
            holder.tuesdayAdapter.notifyDataSetChanged()
            holder.wednesdayAdapter.notifyDataSetChanged()
            holder.thursdayAdapter.notifyDataSetChanged()
            holder.fridayAdapter.notifyDataSetChanged()
            holder.saturdayAdapter.notifyDataSetChanged()
            holder.sundayAdapter.notifyDataSetChanged()
            if(holder.cardContent.visibility == View.GONE){
                holder.cardContent.visibility = View.VISIBLE
                fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                    .collection(plan_search.planId).document("Segunda-Feira").get().addOnSuccessListener { document ->
                        if(document !=null){
                            val tempArray : ArrayList<String> = arrayListOf()
                            document.data?.let { data ->
                                data.forEach { (key, _) ->
                                    Log.d("testkey", key.toString())
                                    val tempExercies = SearchPlanMondayExercises(key,"Segunda")
                                    planExercisesMonday.add(tempExercies)
                                    holder.mondayAdapter.notifyDataSetChanged()


                                }

                            }
                        }

                    }
                fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                    .collection(plan_search.planId).document("Terça-Feira").get().addOnSuccessListener { document ->
                        if(document !=null){
                            val tempArray : ArrayList<String> = arrayListOf()
                            document.data?.let { data ->
                                data.forEach { (key, _) ->
                                    Log.d("testkey", key.toString())
                                    val tempExercies = SearchPlanTuesdayExercises(key,"Terça")
                                    planExercisesTuesday.add(tempExercies)
                                    holder.tuesdayAdapter.notifyDataSetChanged()


                                }

                            }
                        }

                    }
                fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                    .collection(plan_search.planId).document("Quarta-Feira").get().addOnSuccessListener { document ->
                        if(document !=null){
                            val tempArray : ArrayList<String> = arrayListOf()
                            document.data?.let { data ->
                                data.forEach { (key, _) ->
                                    Log.d("testkey", key.toString())
                                    val tempExercies = SearchPlanWednesdayExercises(key,"Quarta")
                                    planExercisesWednesday.add(tempExercies)
                                    holder.wednesdayAdapter.notifyDataSetChanged()


                                }

                            }
                        }

                    }
                fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                    .collection(plan_search.planId).document("Quinta-Feira").get().addOnSuccessListener { document ->
                        if(document !=null){
                            val tempArray : ArrayList<String> = arrayListOf()
                            document.data?.let { data ->
                                data.forEach { (key, _) ->
                                    Log.d("testkey", key.toString())
                                    val tempExercies = SearchPlanThursdayExercises(key,"Quinta")
                                    planExercisesThursday.add(tempExercies)
                                    holder.thursdayAdapter.notifyDataSetChanged()


                                }

                            }
                        }

                    }
                fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                    .collection(plan_search.planId).document("Sexta-Feira").get().addOnSuccessListener { document ->
                        if(document !=null){
                            val tempArray : ArrayList<String> = arrayListOf()
                            document.data?.let { data ->
                                data.forEach { (key, _) ->
                                    Log.d("testkey", key.toString())
                                    val tempExercies = SearchPlanFridayExercises(key,"Sexta")
                                    planExercisesFriday.add(tempExercies)
                                    holder.fridayAdapter.notifyDataSetChanged()


                                }

                            }
                        }

                    }
                fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                    .collection(plan_search.planId).document("Sábado").get().addOnSuccessListener { document ->
                        if(document !=null){
                            val tempArray : ArrayList<String> = arrayListOf()
                            document.data?.let { data ->
                                data.forEach { (key, _) ->
                                    Log.d("testkey", key.toString())
                                    val tempExercies = SearchPlanSaturdayExercises(key,"Sábado")
                                    planExercisesSaturday.add(tempExercies)
                                    holder.saturdayAdapter.notifyDataSetChanged()


                                }

                            }
                        }

                    }
                fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                    .collection(plan_search.planId).document("Domingo").get().addOnSuccessListener { document ->
                        if(document !=null){
                            val tempArray : ArrayList<String> = arrayListOf()
                            document.data?.let { data ->
                                data.forEach { (key, _) ->
                                    Log.d("testkey", key.toString())
                                    val tempExercies = SearchPlanSundayExercises(key,"Domingo")
                                    planExercisesSunday.add(tempExercies)
                                    holder.sundayAdapter.notifyDataSetChanged()


                                }

                            }
                        }

                    }





            }else{
                holder.cardContent.visibility = View.GONE
            }
            TransitionManager.beginDelayedTransition(holder.searchCard, AutoTransition())


        }

        fireBaseFirestore.collection("Users").document(currentUser)
            .get().addOnSuccessListener { documents ->
                if (documents != null) {
                    var likeArray = documents["likedPlans"] as MutableList<*>
                    if(likeArray.contains(plan_search.planId)){
                        holder.filledHeart.visibility = View.VISIBLE
                    }
                    if(likeArray.contains(plan_search.planId) == false){
                        holder.filledHeart.visibility = View.INVISIBLE


                    }
                }

            }

            fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
            .collection(plan_search.planId).document("Gostos").get().addOnSuccessListener { documents ->
                if (documents != null) {
                    var exerciseNumberLikes = documents["Gostos"].toString()
                    holder.likeExerciseText.text = exerciseNumberLikes

                }

            }
        val plansRef = fireBaseFirestore.collection("Users").document(plan_search.creator)
        plansRef.get()
            .addOnSuccessListener { documents ->
                if (documents != null) {
                   creatorUsername = documents["username"].toString()
                    holder.planCreator.text = "Criado por: " + creatorUsername

                }
            }
        holder.planId.text = plan_search.planId

        holder.likeExercise.setOnClickListener {
            fireBaseFirestore.collection("Users").document(currentUser)
                .get().addOnSuccessListener { documents ->
                    if (documents != null) {
                        var likeArray = documents["likedPlans"] as MutableList<*>
                        if(likeArray.contains(plan_search.planId)){
                            likeArray.remove(plan_search.planId)
                            fireBaseFirestore.collection("Users").document(currentUser)
                                .update("likedPlans", likeArray)
                            holder.filledHeart.visibility = View.INVISIBLE
                            fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                                .collection(plan_search.planId).document("Gostos")
                                .update("Gostos", FieldValue.increment(-1))

                            fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                                .collection(plan_search.planId).document("Gostos").get().addOnSuccessListener { documents ->
                                    if (documents != null) {
                                        var exerciseNumberLikes = documents["Gostos"].toString()
                                        holder.likeExerciseText.text = exerciseNumberLikes

                                    }

                                }


                        }else{
                            fireBaseFirestore.collection("Users").document(currentUser)
                                .update("likedPlans", FieldValue.arrayUnion(plan_search.planId))
                               holder.filledHeart.visibility = View.VISIBLE
                            fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                                .collection(plan_search.planId).document("Gostos")
                                .update("Gostos", FieldValue.increment(1))
                            fireBaseFirestore.collection("Planos de exercícios").document("Banco de Id")
                                .collection(plan_search.planId).document("Gostos").get().addOnSuccessListener { documents ->
                                    if (documents != null) {
                                        var exerciseNumberLikes = documents["Gostos"].toString()
                                        holder.likeExerciseText.text = exerciseNumberLikes

                                    }

                                }

                        }
                    }

                }

        }

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class ViewHolder(itemView : View, listener: onDeleteClick): RecyclerView.ViewHolder(itemView){
        lateinit var planId : TextView
        lateinit var planCreator : TextView
        lateinit var deleteExercise : CardView
        lateinit var likeExercise : CardView
        lateinit var likeExerciseText : TextView
        lateinit var filledHeart : ImageView
        lateinit var cardHeader : ConstraintLayout
        lateinit var cardContent : ConstraintLayout
        lateinit var searchCard : LinearLayout
        lateinit var mondayRecycler : RecyclerView
        lateinit var mondayAdapter : SearchMondayRecyclerAdapter
        lateinit var tuesdayRecycler : RecyclerView
        lateinit var tuesdayAdapter : SearchTuesdayRecyclerAdapter
        lateinit var wednesdayRecycler : RecyclerView
        lateinit var wednesdayAdapter : SearchWednesdayRecyclerAdapter
        lateinit var thursdayRecycler : RecyclerView
        lateinit var thursdayAdapter : SearchThursdayRecyclerAdapter
        lateinit var fridayRecycler :RecyclerView
        lateinit var fridayAdapter : SearchFridayRecyclerAdapter
        lateinit var saturdayRecycler : RecyclerView
        lateinit var saturdayAdapter : SearchSaturdayRecyclerAdapter
        lateinit var sundayRecycler :RecyclerView
        lateinit var sundayAdapter : SearchSundayRecyclerAdapter

        init{
                planId = itemView.findViewById(R.id.searchplanExerciseName)
                planCreator = itemView.findViewById(R.id.planCreatorUsername)
                deleteExercise = itemView.findViewById(R.id.planDeleteExercise)
                likeExercise = itemView.findViewById(R.id.planLikeExercise)
                likeExerciseText = itemView.findViewById(R.id.exerciseLikes)
                filledHeart = itemView.findViewById(R.id.heartFill)
                cardHeader = itemView.findViewById(R.id.searchPlanExerciseHeader)
                cardContent = itemView.findViewById(R.id.searchPlanExerciseContent)
                searchCard = itemView.findViewById(R.id.searchPlanCard)
                mondayRecycler = itemView.findViewById(R.id.SearchMondayRecycler)
                tuesdayRecycler = itemView.findViewById(R.id.tuesdayRecycler)
            wednesdayRecycler = itemView.findViewById(R.id.wednesdayRecycler)
            thursdayRecycler = itemView.findViewById(R.id.thursdayRecycler)
            fridayRecycler = itemView.findViewById(R.id.fridayRecycler)
            saturdayRecycler = itemView.findViewById(R.id.saturdayRecycler)
            sundayRecycler = itemView.findViewById(R.id.sundayRecycler)
                deleteExercise.setOnClickListener {
                    listener.onDeleteClick(planId.text)
                }


            }

    }

    class ExerciseDiffUtil : DiffUtil.ItemCallback<PlanSearchModel>(){
        override fun areItemsTheSame(oldItem: PlanSearchModel, newItem: PlanSearchModel): Boolean {
            return oldItem.planId === newItem.planId
        }

        override fun areContentsTheSame(oldItem: PlanSearchModel, newItem: PlanSearchModel): Boolean {
            return oldItem == newItem
        }

    }
}




