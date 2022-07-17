package com.doublej.jjfitnessapp.ui.recycleradapters.nutrition

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.doublej.jjfitnessapp.R

import com.doublej.jjfitnessapp.ui.models.nutrition.PlanFoodTuesday


class TuesdayFoodRecyclerAdapter(var plan_foods : ArrayList<PlanFoodTuesday>) : ListAdapter<PlanFoodTuesday, TuesdayFoodRecyclerAdapter.ViewHolder>(
    ExerciseDiffUtil()
) {








    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.plan_nutrition_cards_layout,parent,false)



        return ViewHolder(v)
    }





    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        
        val plan_exercises : PlanFoodTuesday = getItem(position)
            holder.planFoodName.text = plan_exercises.nomePrato





    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){

        lateinit var planFoodName: TextView
        lateinit var caloriesValue: TextView
        lateinit var carbsValue : TextView
        lateinit var proteinValue : TextView
        lateinit var fatsValue : TextView



        init{

            planFoodName = itemView.findViewById(R.id.planFoodName)
            caloriesValue = itemView.findViewById(R.id.caloriesValuePlan)
            carbsValue = itemView.findViewById(R.id.carbsValuePlan)
            proteinValue = itemView.findViewById(R.id.proteinValuePlan)
            fatsValue = itemView.findViewById(R.id.fatsValuePlan)

        }
    }

    class ExerciseDiffUtil : DiffUtil.ItemCallback<PlanFoodTuesday>(){
        override fun areItemsTheSame(oldItem: PlanFoodTuesday, newItem: PlanFoodTuesday): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: PlanFoodTuesday, newItem: PlanFoodTuesday): Boolean {
            return oldItem == newItem
        }

    }




}