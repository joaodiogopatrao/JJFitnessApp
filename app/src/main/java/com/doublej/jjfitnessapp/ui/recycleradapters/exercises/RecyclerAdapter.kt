package com.doublej.jjfitnessapp.ui.recycleradapters.exercises

import android.graphics.BitmapFactory
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import android.widget.RatingBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.ui.models.exercises.Exercises
import com.google.firebase.storage.FirebaseStorage
import java.io.File


class RecyclerAdapter(var exercises: ArrayList<Exercises>, var mondayExercises: ArrayList<String>) : ListAdapter<Exercises, RecyclerAdapter.ViewHolder>(
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

        val v = LayoutInflater.from(parent.context).inflate(R.layout.cards_layout,parent,false)



        return ViewHolder(v,mListener)
    }





    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val exercise : Exercises = getItem(position)
        Log.d("testReferencias", exercise.referenciaImagem.toString())
        val storageRef = FirebaseStorage.getInstance().reference.child(exercise.referenciaImagem!!)
        val localFile = File.createTempFile("tempImage", "jpg")
        storageRef.getFile(localFile).addOnSuccessListener {
            val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            //holder.itemImage.setImageBitmap(bitmap)
           // Glide.with(holder.itemImage.context).clear(holder.itemImage);
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
        holder.itemTitle.text = exercise.nomeExercicio
        holder.itemGroup.text = exercise.descricaoPrimaria
       // holder.itemSecundaryGroup.text = exercise.grupoMuscularSecundario
        Log.d("ratingDifTest", exercise.nomeExercicio + exercise.dificuldade?.toFloat().toString())
       holder.itemRating.rating = exercise.dificuldade?.toFloat()!!





    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    inner class ViewHolder(itemView : View, listener: onExerciseClick): RecyclerView.ViewHolder(itemView){
        lateinit var itemTitle: TextView
        lateinit var itemGroup: TextView
        //var itemSecundaryGroup: TextView
        lateinit var itemImage : ImageView
        lateinit var itemRating : RatingBar
        lateinit var addExerciseButton : CardView

        init{
            itemTitle = itemView.findViewById(R.id.exerciseName)
            itemGroup = itemView.findViewById(R.id.descricaoPrimaria)
            //itemSecundaryGroup = itemView.findViewById(R.id.secundaryMuscleGroup)
            itemImage = itemView.findViewById(R.id.exerciseImage)
            itemRating = itemView.findViewById(R.id.ratingBar)
            addExerciseButton = itemView.findViewById(R.id.addExerciseButton)
            addExerciseButton.setOnClickListener {
                var pos = adapterPosition
                listener.onItemClick(itemTitle.text)
            }

        }
    }

    class ExerciseDiffUtil : DiffUtil.ItemCallback<Exercises>(){
        override fun areItemsTheSame(oldItem: Exercises, newItem: Exercises): Boolean {
            return oldItem.id === newItem.id
        }

        override fun areContentsTheSame(oldItem: Exercises, newItem: Exercises): Boolean {
            return oldItem == newItem
        }

    }




}