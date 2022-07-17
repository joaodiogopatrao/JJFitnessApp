package com.doublej.jjfitnessapp.ui.recycleradapters.nutrition

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.ui.models.nutrition.SearchPlanSaturdayFoods



class SearchSaturdayNutritionRecyclerAdapter(var plan_foods : ArrayList<SearchPlanSaturdayFoods>) : ListAdapter<SearchPlanSaturdayFoods, SearchSaturdayNutritionRecyclerAdapter.ViewHolder>(
    ExerciseDiffUtil()
) {








    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.plan_nutrition_cards_layout,parent,false)
        Log.d("testValuePlan","ola")



        return ViewHolder(v)
    }





    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        
        val plan_foods : SearchPlanSaturdayFoods = getItem(position)

            Log.d("testValuePlan",plan_foods.nomePrato)


        if(plan_foods.dayOfWeek == "Sábado"){
            holder.planFoodName.text = plan_foods.nomePrato

        }





    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class ViewHolder(itemView : View): RecyclerView.ViewHolder(itemView){
        lateinit var planFoodName: TextView



        init{

            planFoodName = itemView.findViewById(R.id.planFoodName)


        }
    }

    class ExerciseDiffUtil : DiffUtil.ItemCallback<SearchPlanSaturdayFoods>(){
        override fun areItemsTheSame(oldItem: SearchPlanSaturdayFoods, newItem: SearchPlanSaturdayFoods): Boolean {
            return oldItem.nomePrato === newItem.nomePrato
        }

        override fun areContentsTheSame(oldItem: SearchPlanSaturdayFoods, newItem: SearchPlanSaturdayFoods): Boolean {
            return oldItem == newItem
        }

    }




}