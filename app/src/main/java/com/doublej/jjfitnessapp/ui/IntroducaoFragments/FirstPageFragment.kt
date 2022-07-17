package com.doublej.jjfitnessapp.ui.IntroducaoFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.databinding.FirstPageFragmentBinding
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.sackcentury.shinebuttonlib.ShineButton


import android.util.Log
import com.aigestudio.wheelpicker.widgets.WheelDatePicker
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import java.lang.ClassCastException


class FirstPageFragment : Fragment() {


    private lateinit var viewModel: FirstPageViewModel
    private lateinit var firstPageButton : ShineButton
    public lateinit var firstName : TextInputEditText
    public lateinit var lastName : TextInputEditText
    private lateinit var firstNameInput : TextInputLayout
    private lateinit var lastNameInput : TextInputLayout
    private lateinit var day : TextInputEditText
    private lateinit var month : TextInputEditText
    private lateinit var year : TextInputEditText
    private lateinit var checkImage : ImageView
    private lateinit var xImage : ImageView
    private lateinit var actionText : TextView
    private lateinit var root : View
    private var _binding: FirstPageFragmentBinding? = null
    private val binding get() = _binding!!
    private var isLocked : Boolean = false
    private var canShowButton : Boolean = false
    private var buttonChecker : Boolean = false
    private lateinit var database : FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var usernameText : TextView
    private lateinit var datePicker : WheelDatePicker



    interface onSomeEventListener {
        fun someEvent(firstName : String, lastName : String,day : Int,month : Int,year : Int)
    }

    lateinit var someEventListener : onSomeEventListener


    override fun onAttach(context: Context) {
        super.onAttach(context)
        try{
            someEventListener = context as onSomeEventListener
        }catch (e : ClassCastException) {
            throw ClassCastException(context.toString() + "error")
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FirstPageFragmentBinding.inflate(inflater, container, false)
        root = binding.root
        auth = Firebase.auth

        firstPageButton = root.findViewById(R.id.firstPageButton)
        firstName = root.findViewById(R.id.firstNameEditText)
        lastName = root.findViewById(R.id.lastNameEditText)
        day = root.findViewById(R.id.dayEditText)
        month = root.findViewById(R.id.monthEditText)
        year = root.findViewById(R.id.yearEditText)
        usernameText = root.findViewById(R.id.usernameText)
        database = FirebaseFirestore.getInstance()


        var userId = auth.currentUser!!.uid
        database.collection("Users").document(userId).get()
            .addOnSuccessListener { document ->
            if (document != null) {
               usernameText.text =  document["username"].toString()
            } else {
                Log.d("firestore", "No such document")
            }
        }
            .addOnFailureListener { exception ->
                Log.d("firestore", "get failed with ", exception)
            }





        firstPageButton.setOnClickListener {
            callNames()
        }


        return root
    }





    fun callNames(){
        
        someEventListener.someEvent(firstName.text.toString(),lastName.text.toString(),day.text.toString().toInt(),month.text.toString().toInt(),year.text.toString().toInt())
    }


}