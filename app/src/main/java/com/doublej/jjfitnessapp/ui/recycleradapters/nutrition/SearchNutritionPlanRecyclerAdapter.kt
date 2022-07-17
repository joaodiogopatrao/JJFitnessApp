package com.doublej.jjfitnessapp.ui.recycleradapters.nutrition

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
import com.doublej.jjfitnessapp.ui.models.nutrition.*

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class SearchNutritionPlanRecyclerAdapter(var plansFood: ArrayList<PlanNutritionSearchModel>) : ListAdapter<PlanNutritionSearchModel, SearchNutritionPlanRecyclerAdapter.ViewHolder>(
    ExerciseDiffUtil()
) {

    private lateinit var mListener : onDeleteClick
    private lateinit var fireBaseFirestore : FirebaseFirestore
    private var creatorUsername : String = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser : String
    private lateinit var planFoodMonday: ArrayList<SearchPlanMondayFoods>
    private lateinit var planFoodTuesday: ArrayList<SearchPlanTuesdayFoods>
    private lateinit var planFoodWednesday: ArrayList<SearchPlanWednesdayFoods>
    private lateinit var planFoodThursday: ArrayList<SearchPlanThursdayFoods>
    private lateinit var planFoodFriday: ArrayList<SearchPlanFridayFoods>
    private lateinit var planFoodSaturday: ArrayList<SearchPlanSaturdayFoods>
    private lateinit var planFoodSunday: ArrayList<SearchPlanSundayFoods>


    interface onDeleteClick{
        fun onDeleteClick(title : CharSequence)
    }

    fun setOnItemClickListener(listener: onDeleteClick){
        mListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.search_nutrition_plan_card,parent,false)
        fireBaseFirestore = FirebaseFirestore.getInstance()
        auth = Firebase.auth
        currentUser = auth.currentUser!!.uid
        planFoodMonday = arrayListOf()
        planFoodTuesday = arrayListOf()
        planFoodWednesday = arrayListOf()
        planFoodThursday = arrayListOf()
        planFoodFriday = arrayListOf()
        planFoodSaturday = arrayListOf()
        planFoodSunday = arrayListOf()



        return ViewHolder(v,mListener)
    }





    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plan_search: PlanNutritionSearchModel = getItem(position)

        holder.mondayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
        holder.mondayAdapter = SearchMondayNutritionRecyclerAdapter(planFoodMonday)
        holder.mondayAdapter.setHasStableIds(true)
        holder.mondayAdapter.submitList(planFoodMonday)
        holder.mondayRecycler.adapter = holder.mondayAdapter
        holder.tuesdayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
        holder.tuesdayAdapter = SearchTuesdayNutritionRecyclerAdapter(planFoodTuesday)
        holder.tuesdayAdapter.setHasStableIds(true)
        holder.tuesdayAdapter.submitList(planFoodTuesday)
        holder.tuesdayRecycler.adapter = holder.tuesdayAdapter
        holder.wednesdayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
        holder.wednesdayAdapter = SearchWednesdayNutritionRecyclerAdapter(planFoodWednesday)
        holder.wednesdayAdapter.setHasStableIds(true)
        holder.wednesdayAdapter.submitList(planFoodWednesday)
        holder.wednesdayRecycler.adapter = holder.wednesdayAdapter
        holder.thursdayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
        holder.thursdayAdapter = SearchThursdayNutritionRecyclerAdapter(planFoodThursday)
        holder.thursdayAdapter.setHasStableIds(true)
        holder.thursdayAdapter.submitList(planFoodThursday)
        holder.thursdayRecycler.adapter = holder.thursdayAdapter
        holder.fridayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
        holder.fridayAdapter = SearchFridayNutritionRecyclerAdapter(planFoodFriday)
        holder.fridayAdapter.setHasStableIds(true)
        holder.fridayAdapter.submitList(planFoodFriday)
        holder.fridayRecycler.adapter = holder.fridayAdapter
        holder.saturdayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
        holder.saturdayAdapter = SearchSaturdayNutritionRecyclerAdapter(planFoodSaturday)
        holder.saturdayAdapter.setHasStableIds(true)
        holder.saturdayAdapter.submitList(planFoodSaturday)
        holder.saturdayRecycler.adapter = holder.saturdayAdapter
        holder.sundayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
        holder.sundayAdapter = SearchSundayNutritionRecyclerAdapter(planFoodSunday)
        holder.sundayAdapter.setHasStableIds(true)
        holder.sundayAdapter.submitList(planFoodSunday)
        holder.sundayRecycler.adapter = holder.sundayAdapter

        holder.cardHeader.setOnClickListener {
            planFoodMonday.clear()
            planFoodTuesday.clear()
            planFoodWednesday.clear()
            planFoodThursday.clear()
            planFoodFriday.clear()
            planFoodSaturday.clear()
            planFoodSunday.clear()
            holder.mondayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
            holder.mondayAdapter = SearchMondayNutritionRecyclerAdapter(planFoodMonday)
            holder.mondayAdapter.setHasStableIds(true)
            holder.mondayAdapter.submitList(planFoodMonday)
            holder.mondayRecycler.adapter = holder.mondayAdapter
            holder.tuesdayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
            holder.tuesdayAdapter = SearchTuesdayNutritionRecyclerAdapter(planFoodTuesday)
            holder.tuesdayAdapter.setHasStableIds(true)
            holder.tuesdayAdapter.submitList(planFoodTuesday)
            holder.tuesdayRecycler.adapter = holder.tuesdayAdapter
            holder.wednesdayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
            holder.wednesdayAdapter = SearchWednesdayNutritionRecyclerAdapter(planFoodWednesday)
            holder.wednesdayAdapter.setHasStableIds(true)
            holder.wednesdayAdapter.submitList(planFoodWednesday)
            holder.wednesdayRecycler.adapter = holder.wednesdayAdapter
            holder.thursdayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
            holder.thursdayAdapter = SearchThursdayNutritionRecyclerAdapter(planFoodThursday)
            holder.thursdayAdapter.setHasStableIds(true)
            holder.thursdayAdapter.submitList(planFoodThursday)
            holder.thursdayRecycler.adapter = holder.thursdayAdapter
            holder.fridayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
            holder.fridayAdapter = SearchFridayNutritionRecyclerAdapter(planFoodFriday)
            holder.fridayAdapter.setHasStableIds(true)
            holder.fridayAdapter.submitList(planFoodFriday)
            holder.fridayRecycler.adapter = holder.fridayAdapter
            holder.saturdayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
            holder.saturdayAdapter = SearchSaturdayNutritionRecyclerAdapter(planFoodSaturday)
            holder.saturdayAdapter.setHasStableIds(true)
            holder.saturdayAdapter.submitList(planFoodSaturday)
            holder.saturdayRecycler.adapter = holder.saturdayAdapter
            holder.sundayRecycler.layoutManager = LinearLayoutManager(holder.cardContent.context)
            holder.sundayAdapter = SearchSundayNutritionRecyclerAdapter(planFoodSunday)
            holder.sundayAdapter.setHasStableIds(true)
            holder.sundayAdapter.submitList(planFoodSunday)
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
                fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                    .collection(plan_search.planId).document("Segunda-Feira").get().addOnSuccessListener { document ->
                        if(document !=null){
                            val tempArray : ArrayList<String> = arrayListOf()
                            document.data?.let { data ->
                                data.forEach { (key, _) ->
                                    Log.d("testkey", key.toString())
                                    val tempFoods = SearchPlanMondayFoods(key,"Segunda")
                                    planFoodMonday.add(tempFoods)
                                    holder.mondayAdapter.notifyDataSetChanged()


                                }

                            }
                        }

                    }
                fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                    .collection(plan_search.planId).document("Terça-Feira").get().addOnSuccessListener { document ->
                        if(document !=null){
                            val tempArray : ArrayList<String> = arrayListOf()
                            document.data?.let { data ->
                                data.forEach { (key, _) ->
                                    Log.d("testkey", key.toString())
                                    val tempFood = SearchPlanTuesdayFoods(key,"Terça")
                                    planFoodTuesday.add(tempFood)
                                    holder.tuesdayAdapter.notifyDataSetChanged()


                                }

                            }
                        }

                    }
                fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                    .collection(plan_search.planId).document("Quarta-Feira").get().addOnSuccessListener { document ->
                        if(document !=null){
                            val tempArray : ArrayList<String> = arrayListOf()
                            document.data?.let { data ->
                                data.forEach { (key, _) ->
                                    Log.d("testkey", key.toString())
                                    val tempFood = SearchPlanWednesdayFoods(key,"Quarta")
                                    planFoodWednesday.add(tempFood)
                                    holder.wednesdayAdapter.notifyDataSetChanged()


                                }

                            }
                        }

                    }
                fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                    .collection(plan_search.planId).document("Quinta-Feira").get().addOnSuccessListener { document ->
                        if(document !=null){
                            val tempArray : ArrayList<String> = arrayListOf()
                            document.data?.let { data ->
                                data.forEach { (key, _) ->
                                    Log.d("testkey", key.toString())
                                    val tempFood = SearchPlanThursdayFoods(key,"Quinta")
                                    planFoodThursday.add(tempFood)
                                    holder.thursdayAdapter.notifyDataSetChanged()


                                }

                            }
                        }

                    }
                fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                    .collection(plan_search.planId).document("Sexta-Feira").get().addOnSuccessListener { document ->
                        if(document !=null){
                            val tempArray : ArrayList<String> = arrayListOf()
                            document.data?.let { data ->
                                data.forEach { (key, _) ->
                                    Log.d("testkey", key.toString())
                                    val tempFood = SearchPlanFridayFoods(key,"Sexta")
                                    planFoodFriday.add(tempFood)
                                    holder.fridayAdapter.notifyDataSetChanged()


                                }

                            }
                        }

                    }
                fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                    .collection(plan_search.planId).document("Sábado").get().addOnSuccessListener { document ->
                        if(document !=null){
                            val tempArray : ArrayList<String> = arrayListOf()
                            document.data?.let { data ->
                                data.forEach { (key, _) ->
                                    Log.d("testkey", key.toString())
                                    val tempFood = SearchPlanSaturdayFoods(key,"Sábado")
                                    planFoodSaturday.add(tempFood)
                                    holder.saturdayAdapter.notifyDataSetChanged()


                                }

                            }
                        }

                    }
                fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                    .collection(plan_search.planId).document("Domingo").get().addOnSuccessListener { document ->
                        if(document !=null){
                            val tempArray : ArrayList<String> = arrayListOf()
                            document.data?.let { data ->
                                data.forEach { (key, _) ->
                                    Log.d("testkey", key.toString())
                                    val tempFood = SearchPlanSundayFoods(key,"Domingo")
                                    planFoodSunday.add(tempFood)
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

            fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
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
                            fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                                .collection(plan_search.planId).document("Gostos")
                                .update("Gostos", FieldValue.increment(-1))

                            fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
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
                            fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
                                .collection(plan_search.planId).document("Gostos")
                                .update("Gostos", FieldValue.increment(1))
                            fireBaseFirestore.collection("Planos de alimentação").document("Banco de Id")
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
        lateinit var mondayAdapter : SearchMondayNutritionRecyclerAdapter
        lateinit var tuesdayRecycler : RecyclerView
        lateinit var tuesdayAdapter : SearchTuesdayNutritionRecyclerAdapter
        lateinit var wednesdayRecycler : RecyclerView
        lateinit var wednesdayAdapter : SearchWednesdayNutritionRecyclerAdapter
        lateinit var thursdayRecycler : RecyclerView
        lateinit var thursdayAdapter : SearchThursdayNutritionRecyclerAdapter
        lateinit var fridayRecycler :RecyclerView
        lateinit var fridayAdapter : SearchFridayNutritionRecyclerAdapter
        lateinit var saturdayRecycler : RecyclerView
        lateinit var saturdayAdapter : SearchSaturdayNutritionRecyclerAdapter
        lateinit var sundayRecycler :RecyclerView
        lateinit var sundayAdapter : SearchSundayNutritionRecyclerAdapter

        init{
                planId = itemView.findViewById(R.id.searchplanFoodName)
                planCreator = itemView.findViewById(R.id.planCreatorUsernameNutrition)
                deleteExercise = itemView.findViewById(R.id.planDeleteFood)
                likeExercise = itemView.findViewById(R.id.planLikeFood)
                likeExerciseText = itemView.findViewById(R.id.foodLikes)
                filledHeart = itemView.findViewById(R.id.heartFillNutrition)
                cardHeader = itemView.findViewById(R.id.searchPlanNutritionHeader)
                cardContent = itemView.findViewById(R.id.searchPlanFoodContent)
                searchCard = itemView.findViewById(R.id.searchPlanCardNutrition)
                mondayRecycler = itemView.findViewById(R.id.MondayRecyclerNutrition)
                tuesdayRecycler = itemView.findViewById(R.id.tuesdayRecyclerNutrition)
                wednesdayRecycler = itemView.findViewById(R.id.wednesdayRecyclerNutrition)
                thursdayRecycler = itemView.findViewById(R.id.thursdayRecyclerNutrition)
                fridayRecycler = itemView.findViewById(R.id.fridayRecyclerNutrition)
                saturdayRecycler = itemView.findViewById(R.id.saturdayRecyclerNutrition)
                sundayRecycler = itemView.findViewById(R.id.sundayRecyclerNutrition)
                deleteExercise.setOnClickListener {
                    listener.onDeleteClick(planId.text)
                }


            }

    }

    class ExerciseDiffUtil : DiffUtil.ItemCallback<PlanNutritionSearchModel>(){
        override fun areItemsTheSame(oldItem: PlanNutritionSearchModel, newItem: PlanNutritionSearchModel): Boolean {
            return oldItem.planId === newItem.planId
        }

        override fun areContentsTheSame(oldItem: PlanNutritionSearchModel, newItem: PlanNutritionSearchModel): Boolean {
            return oldItem == newItem
        }

    }
}




