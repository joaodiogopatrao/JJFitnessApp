package com.doublej.jjfitnessapp.ui.recycleradapters.nutrition

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.ui.models.nutrition.PlanFoodFriday


class FridayFoodRecyclerAdapter(var plan_foods : ArrayList<PlanFoodFriday>) : ListAdapter<PlanFoodFriday, FridayFoodRecyclerAdapter.ViewHolder>(
    ExerciseDiffUtil()
) {








    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.plan_nutrition_cards_layout,parent,false)



        return ViewHolder(v)
    }





    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        
        val plan_foods: PlanFoodFriday = getItem(position)

            holder.planFoodName.text = plan_foods.nomePrato
            holder.caloriesValue.text = plan_foods.Calorias.toString() +"kcal"
            holder.carbsValue.text = plan_foods.Carbohidratos.toString() +"g"
            holder.proteinValue.text = plan_foods.Proteinas.toString() + "g"
            holder.fatsValue.text = plan_foods.Gorduras.toString() + "g"
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

    class ExerciseDiffUtil : DiffUtil.ItemCallback<PlanFoodFriday>(){
        override fun areItemsTheSame(oldItem: PlanFoodFriday, newItem: PlanFoodFriday): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: PlanFoodFriday, newItem: PlanFoodFriday): Boolean {
            return oldItem == newItem
        }

    }




}