package com.doublej.jjfitnessapp.ui.fragments

import android.animation.ObjectAnimator
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.doublej.jjfitnessapp.databinding.ImcFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.marvel999.acr.ArcProgress
import kotlin.math.roundToInt
import com.doublej.jjfitnessapp.R
import com.google.android.material.slider.Slider
import android.view.View.*

import android.widget.*
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.children
import com.doublej.jjfitnessapp.ui.dashboard.Score
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter



class ImcFragment : Fragment() {

    companion object {
        fun newInstance() = ImcFragment()
    }

    private lateinit var viewModel: ImcViewModel
    private var _binding: ImcFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var userHeight : Int = 0
    private var userWeight : Float = 0f
    private var userAge : Int = 0
    private var imc : Float = 0f
    private var canIncreaseImc : Boolean = true
    private var imcIncreasing : Float = 0f
    private lateinit var arcProgressBar : ArcProgress
    private lateinit var imcTextValue : TextView
    private var dataRetrieved : Boolean = false
    private var isViewShown : Boolean = false
    private lateinit var editHeightImc :CardView
    private lateinit var editWeightImc : CardView
    private lateinit var addHeightButtonImc : CardView
    private lateinit var addWeightButtonImc : CardView
    private lateinit var heightImcSlider : Slider
    private lateinit var heightImcValue : TextView
    private lateinit var heightImcPicker : CardView
    private lateinit var imcHeightText : TextView
    private lateinit var imcWeightText : TextView
    private var isMale : Boolean = true
    private var bmr : Float = 0f
    private var mr : Float = 0f
    private var activityLevel : String = ""
    private lateinit var testButton : Button
    private lateinit var maleBody : ImageView
    private lateinit var heightSlider : Slider
    private var canOpenImcHeightPicker : Boolean = false
    private var canCloseImcHeightPicker : Boolean = false
    private var isHeightImcPickerOpen : Boolean = false
    private var isWeightImcPickerOpen : Boolean = false
    private lateinit var imcScrollView : ScrollView
    private lateinit var lineChart: LineChart
    private var scoreList = ArrayList<Score>()

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ImcFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root


        arcProgressBar = root.findViewById(R.id.arcProgressView)
        imcTextValue = root.findViewById(R.id.bmiTextValue)
        lineChart = root.findViewById(R.id.lineChart)
        initLineChart()


        setDataToLineChart()





        //maleBody = root.findViewById(R.id.malebody)
        //testButton = root.findViewById(R.id.testButton)
        imcTextValue.alpha = 0f
        database = FirebaseFirestore.getInstance()
        auth = Firebase.auth
        val userId = auth.currentUser!!.uid
        imcTextValue.alpha = 0f
        if (!isViewShown) {
            updateData()
        }

      /*  testButton.setOnClickListener{
            SweetAlertDialog(activity, SweetAlertDialog.SUCCESS_TYPE)
                .setContentText("Está dentro do peso normal!")
                .setCustomView(inflater.inflate(R.layout.first_slide_fragment,null))
                .show()

        }*/




        return root
    }



    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("Destroyed", "hasBeenDestroyed")
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putFloat("Imc", imc)
            putBoolean("Flag", isViewShown)
            apply()
        }
    }

    fun savePref(newImc : Float){
         val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putFloat("Imc", newImc)
            putBoolean("Flag", isViewShown)
            apply()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if(isVisibleToUser == true){
            MacroFragment().removeMacroData()
            MetabolicFragment().removeMetData()
        }
        if(isVisibleToUser == true && dataRetrieved == true && view != null){
            removeImcData()
            updateData()


        }else{
            if(view != null) {
                isViewShown = true
               removeImcData()
            }

        }
    }
    fun View.isUserInteractionEnabled(enabled: Boolean) {
        isEnabled = enabled
        if (this is ViewGroup && this.childCount > 0) {
            this.children.forEach {
                it.isUserInteractionEnabled(enabled)
            }
        }
    }

    fun applyImcData(){
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val imc = sharedPref.getFloat("Imc", imc)
        animateProgressBar(imc)
    }

    fun removeImcData(){
        if(view != null) {
            deAnimateProgressBar()
        }
    }

    fun updateData(){
        database = FirebaseFirestore.getInstance()
        auth = Firebase.auth
        val userId = auth.currentUser!!.uid
        database.collection("Users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    userHeight = document.getLong("height")?.toInt()!!
                    userWeight = document.getLong("weight")?.toFloat()!!
                    imc = userWeight / ((userHeight / 100f) * (userHeight / 100f)) * 100.00f.toInt() / 100.00f
                    savePref(imc)
                    applyImcData()
                    dataRetrieved = true

                } else {
                    Log.d("firestore", "Documento não encontrado")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("firestore", "Falha na ligação ", exception)
            }

    }





    override fun onResume() {
        super.onResume()

    }

    fun openImcHeightPicker(){
        heightImcPicker.visibility = VISIBLE
        imcScrollView.isUserInteractionEnabled(false)
        heightImcPicker.isUserInteractionEnabled(true)
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {

                if(canOpenImcHeightPicker == true) {
                    if (heightImcPicker.alpha < 1f) {
                        imcIncreasing += 0.08f
                    }
                    if (heightImcPicker.alpha >= 1f) {
                        imcIncreasing = 1f
                        canOpenImcHeightPicker = false
                    }
                }

                handler.postDelayed(this, 10)
            }

        }, 0)
    }
    fun closeImcHeightPicker(){
        imcScrollView.isUserInteractionEnabled(true)
        heightImcPicker.isUserInteractionEnabled(false)
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {

                if(canCloseImcHeightPicker == true) {
                    if (heightImcPicker.alpha > 0f) {
                        imcIncreasing -= 0.08f
                    }
                    if (heightImcPicker.alpha <= 0f) {
                        imcIncreasing = 0f
                        canCloseImcHeightPicker = false
                    }
                }

                handler.postDelayed(this, 10)
            }

        }, 0)
    }

    fun deAnimateProgressBar(){
        arcProgressBar.setProgress(0f)
        imcTextValue.alpha = 0f
    }

    fun animateProgressBar(imc : Float){

        if(imcTextValue.alpha != 1f) {
            arcProgressBar.setProgressWithAnimation((imc * 180f) / 40f, 1000)
            imcTextValue.text = ((imc * 100.0).roundToInt() / 100.0).toString()
            var animation: ObjectAnimator = ObjectAnimator.ofFloat(imcTextValue, "alpha", 1f)
            animation.setDuration(1000)
            animation.start()
        }

        /*val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {

                if(canIncreaseImc == true) {
                    if (imcIncreasing < imc) {
                        imcIncreasing += 1f
                    }
                    if (imcIncreasing >= imc) {
                        imcIncreasing = imc
                        canIncreaseImc = false
                    }
                    val progressValue = (imcIncreasing * 180f) / 40f

                    imcTextValue.text = ((imcIncreasing * 100.0).roundToInt() / 100.0).toString()
                }

                handler.postDelayed(this, 10)
            }

        }, 0)
*/
    }

    fun changeHeightValue(height : Int){
        imcHeightText.text = height.toString()

    }
    fun changeWeightValue(weight : Int){

    }

    private fun initLineChart() {

//        hide grid lines
        lineChart.axisLeft.setDrawGridLines(false)
        lineChart.axisRight.setDrawGridLines(false)
        lineChart.axisRight.setDrawLabels(false)
        lineChart.axisLeft.setDrawLabels(false)
        lineChart.axisRight.setDrawAxisLine(false)
        lineChart.xAxis.setDrawLabels(true)
        lineChart.xAxis.axisLineColor =resources.getColor(R.color.white)
        lineChart.xAxis.axisLineWidth = 1f
        lineChart.xAxis.setDrawAxisLine(false)
        lineChart.axisLeft.setDrawAxisLine(false)
        lineChart.xAxis.textColor = resources.getColor(R.color.white)
        lineChart.xAxis.typeface = ResourcesCompat.getFont(activity?.applicationContext!!, R.font.montserrat);


        val xAxis: XAxis = lineChart.xAxis
        xAxis.setDrawGridLines(false)

        //remove right y-axis
        lineChart.axisRight.isEnabled = true

        //remove legend
        lineChart.legend.isEnabled = false


        //remove description label
        lineChart.description.isEnabled = false


        //add animation
        lineChart.animateX(1000, Easing.EaseInBounce)

        // to draw label on xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.valueFormatter = MyAxisFormatter()

        xAxis.granularity = 1f
        xAxis.labelRotationAngle = 0F

    }


    inner class MyAxisFormatter : IndexAxisValueFormatter() {

        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < scoreList.size) {
                scoreList[index].name
            } else {
                ""
            }
        }
    }

    private fun setDataToLineChart() {
            //now draw bar chart with dynamic data
            val entries: ArrayList<Entry> = ArrayList()

            scoreList = getScoreList()

            //you can replace this data object with  your custom object
            for (i in scoreList.indices) {
                val score = scoreList[i]
                entries.add(Entry(i.toFloat(), score.score.toFloat()))
            }

            val lineDataSet = LineDataSet(entries, "")
            lineDataSet.setDrawValues(false);
            lineDataSet.setDrawCircles(true);
            lineDataSet.setCircleColor(resources.getColor(R.color.white))
            lineDataSet.circleRadius = 4f

            lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
            lineDataSet.setCubicIntensity(0.2f)
            lineDataSet.lineWidth = 3f
            lineDataSet.fillDrawable = resources.getDrawable(R.drawable.gradient)
            lineDataSet.color = resources.getColor(R.color.white)
            lineDataSet.setDrawFilled(true)

            lineDataSet.valueTextColor = resources.getColor(R.color.white)
            lineDataSet.valueTextSize = 10f
            lineDataSet.valueTypeface = ResourcesCompat.getFont(activity?.applicationContext!!, R.font.montserrat);
            val data = LineData(lineDataSet)
            lineChart.data = data

            lineChart.invalidate()
    }

    private fun getScoreList(): ArrayList<Score> {
        scoreList.add(Score("23/03", 23.1f))
        scoreList.add(Score("25/03", 22.9f))
        scoreList.add(Score("26/03", 22.1f))
        scoreList.add(Score("28/03", 22.2f))
        scoreList.add(Score("29/03", 22.5f))

        return scoreList
    }


}