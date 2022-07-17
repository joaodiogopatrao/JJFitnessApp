package com.doublej.jjfitnessapp.ui.recycleradapters.nutrition

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.ui.models.nutrition.Foods

import com.google.firebase.storage.FirebaseStorage
import java.io.File


class NutritionRecyclerAdapter(var foods: ArrayList<Foods>) : ListAdapter<Foods, NutritionRecyclerAdapter.ViewHolder>(
    ExerciseDiffUtil()
) {

    private lateinit var mListener : onExerciseClick


    interface onExerciseClick{
        fun onItemClick(title : CharSequence)
    }

    fun setOnItemClickListener(listener: onExerciseClick){
        mListener = listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val v = LayoutInflater.from(parent.context).inflate(R.layout.nutrition_cards_layout,parent,false)



        return ViewHolder(v,mListener)
    }





    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val foods : Foods = getItem(position)
        val storageRef = FirebaseStorage.getInstance().reference.child(foods.ReferenciaImagem!!)
        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            Glide.with(holder.itemImage.context)
                .load(bitmap)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .dontAnimate()
                .let { request ->
                    if(holder.itemImage.drawable != null) {
                        request.placeholder(holder.itemImage.drawable.constantState?.newDrawable()?.mutate())
                    } else {
                        request
                    }
                }
                .into(holder.itemImage)

        }
        holder.itemTitle.text = foods.nomePrato
        holder.caloriesValue.text = foods.Calorias.toString() + "kcal"
        holder.proteinValue.text = foods.Proteinas.toString() + "g"
        holder.carbsValue.text = foods.Carbohidratos.toString() + "g"
        holder.fatsValue.text = foods.Gorduras.toString() + "g"

    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class ViewHolder(itemView : View,listener : onExerciseClick): RecyclerView.ViewHolder(itemView){
        lateinit var itemTitle: TextView
        lateinit var caloriesValue: TextView
        lateinit var proteinValue: TextView
        lateinit var carbsValue: TextView
        lateinit var fatsValue: TextView
        lateinit var itemImage : ImageView
        lateinit var addFoodButton : CardView

        init{
           itemTitle = itemView.findViewById(R.id.foodName)
            itemImage = itemView.findViewById(R.id.foodImage)
            caloriesValue = itemView.findViewById(R.id.caloriesValue)
            proteinValue = itemView.findViewById(R.id.proteinValue)
            carbsValue = itemView.findViewById(R.id.carbsValue)
            fatsValue = itemView.findViewById(R.id.fatsValue)
            addFoodButton = itemView.findViewById(R.id.addFoodButton)
            addFoodButton.setOnClickListener {
                var pos = adapterPosition
                listener.onItemClick(itemTitle.text)
            }

        }
    }

    class ExerciseDiffUtil : DiffUtil.ItemCallback<Foods>(){
        override fun areItemsTheSame(oldItem: Foods, newItem: Foods): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: Foods, newItem: Foods): Boolean {
            return oldItem == newItem
        }

    }




}