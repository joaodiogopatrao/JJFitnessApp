package com.doublej.jjfitnessapp.ui.recycleradapters.exercises

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.ui.models.exercises.PlanExercisesWednesday


class WednesdayRecyclerAdapter(var plan_exercises : ArrayList<PlanExercisesWednesday>) : ListAdapter<PlanExercisesWednesday, WednesdayRecyclerAdapter.ViewHolder>(
    ExerciseDiffUtil()
) {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.plan_cards_layout,parent,false)



        return ViewHolder(v)
    }





    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val plan_exercises : PlanExercisesWednesday = getItem(position)
            holder.planExerciseName.text = plan_exercises.nomeExercicio
            holder.planRating.rating = 3f







    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        lateinit var planExerciseName: TextView
        lateinit var planRating: RatingBar


        init{

            planExerciseName = itemView.findViewById(R.id.planExerciseName)
            planRating = itemView.findViewById(R.id.planratingBar)
        }
    }

    class ExerciseDiffUtil : DiffUtil.ItemCallback<PlanExercisesWednesday>(){
        override fun areItemsTheSame(oldItem: PlanExercisesWednesday, newItem: PlanExercisesWednesday): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: PlanExercisesWednesday, newItem: PlanExercisesWednesday): Boolean {
            return oldItem == newItem
        }

    }




}