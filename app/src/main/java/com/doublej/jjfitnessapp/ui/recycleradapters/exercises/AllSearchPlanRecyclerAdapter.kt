package com.doublej.jjfitnessapp.ui.recycleradapters.exercises

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.ui.models.exercises.AllPlanSearchModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue


import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class AllSearchPlanRecyclerAdapter(var plans: ArrayList<AllPlanSearchModel>) : ListAdapter<AllPlanSearchModel, AllSearchPlanRecyclerAdapter.ViewHolder>(
    ExerciseDiffUtil()
) {

    private lateinit var mListener : onExerciseClick
    private lateinit var fireBaseFirestore : FirebaseFirestore
    private var creatorUsername : String = ""
    private lateinit var auth: FirebaseAuth
    private lateinit var currentUser : String


    interface onExerciseClick{
        fun onItemClick(title : CharSequence)
    }

    fun setOnItemClickListener(listener: onExerciseClick){
        mListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.all_search_plan_card,parent,false)
        fireBaseFirestore = FirebaseFirestore.getInstance()
        auth = Firebase.auth
        currentUser = auth.currentUser!!.uid


        return ViewHolder(v)
    }





    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plan_search: AllPlanSearchModel = getItem(position)

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


        holder.planId.text = plan_search.planId
        holder.planCreator.text = "Criado por: username"

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


    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        lateinit var planId : TextView
        lateinit var planCreator : TextView
        lateinit var likeExercise : CardView
        lateinit var likeExerciseText : TextView
        lateinit var filledHeart : ImageView

        init{
                planId = itemView.findViewById(R.id.searchplanExerciseName)
                planCreator = itemView.findViewById(R.id.planCreatorUsername)
            likeExercise = itemView.findViewById(R.id.planLikeExercise)
            likeExerciseText = itemView.findViewById(R.id.exerciseLikes)
            filledHeart = itemView.findViewById(R.id.heartFill)
            }

    }

    class ExerciseDiffUtil : DiffUtil.ItemCallback<AllPlanSearchModel>(){
        override fun areItemsTheSame(oldItem: AllPlanSearchModel, newItem: AllPlanSearchModel): Boolean {
            return oldItem.planId === newItem.planId
        }

        override fun areContentsTheSame(oldItem: AllPlanSearchModel, newItem: AllPlanSearchModel): Boolean {
            return oldItem == newItem
        }

    }
}




