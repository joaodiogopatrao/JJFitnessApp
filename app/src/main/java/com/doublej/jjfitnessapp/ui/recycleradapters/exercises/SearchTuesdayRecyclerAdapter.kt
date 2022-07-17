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
import com.doublej.jjfitnessapp.ui.models.exercises.SearchPlanTuesdayExercises


class SearchTuesdayRecyclerAdapter(var plan_exercises : ArrayList<SearchPlanTuesdayExercises>) : ListAdapter<SearchPlanTuesdayExercises, SearchTuesdayRecyclerAdapter.ViewHolder>(
    ExerciseDiffUtil()
) {








    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.plan_cards_layout,parent,false)



        return ViewHolder(v)
    }





    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        
        val plan_exercises : SearchPlanTuesdayExercises = getItem(position)

        if(plan_exercises.dayOfWeek == "Terça"){
            holder.planExerciseName.text = plan_exercises.nomeExercicio
            holder.planRating.rating = 3f
        }




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
        lateinit var repsValue : TextView
        lateinit var setsValue : TextView
        lateinit var restValue : TextView



        init{

            planExerciseName = itemView.findViewById(R.id.planExerciseName)
            planRating = itemView.findViewById(R.id.planratingBar)

        }
    }

    class ExerciseDiffUtil : DiffUtil.ItemCallback<SearchPlanTuesdayExercises>(){
        override fun areItemsTheSame(oldItem: SearchPlanTuesdayExercises, newItem: SearchPlanTuesdayExercises): Boolean {
            return oldItem.nomeExercicio === newItem.nomeExercicio
        }

        override fun areContentsTheSame(oldItem: SearchPlanTuesdayExercises, newItem: SearchPlanTuesdayExercises): Boolean {
            return oldItem == newItem
        }

    }




}