package com.doublej.jjfitnessapp.ui.fragments


import android.animation.LayoutTransition
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.ViewPager
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.databinding.FragmentNutricaoBinding
import com.google.android.material.slider.Slider
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.marvel999.acr.ArcProgress
import com.roughike.swipeselector.SwipeItem
import com.roughike.swipeselector.SwipeSelector
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator
import eightbitlab.com.blurview.BlurView
import io.ghyeok.stickyswitch.widget.StickySwitch
import me.itangqi.waveloadingview.WaveLoadingView
import www.sanju.motiontoast.MotionToast
import www.sanju.motiontoast.MotionToastStyle
import java.io.File
import java.time.LocalDate
import java.time.Period


class DashboardFragment : Fragment() {

    //DECLARAÇÃO DAS VARIAVIES
    private lateinit var dashboardViewModel: DashboardViewModel
    private var _binding: FragmentNutricaoBinding? = null
    private lateinit var storage: FirebaseStorage
    private lateinit var storageReference: StorageReference
    private lateinit var database: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var adapter : MyViewPagerAdapterNutricao
    private lateinit var sliderHeight : Slider
    private lateinit var sliderWeight : Slider
    private lateinit var calculateParamButton : CardView
    private lateinit var waveLoadingView : WaveLoadingView
    private lateinit var waveLoadingView2 : WaveLoadingView
    private lateinit var calculatorsViewPager : ViewPager
    private lateinit var dotsIndicator : WormDotsIndicator
    private lateinit var genderPickerCalculator : StickySwitch
    private lateinit var ageEditTextCalculator : TextInputEditText
    private lateinit var slidingMenuImc : ConstraintLayout
    private lateinit var slidingMenuMacro : ConstraintLayout
    private lateinit var slidingMenuMet : ConstraintLayout
    private lateinit var documentSnapshot: DocumentSnapshot
    private lateinit var arcProgressBar : ArcProgress
    private lateinit var bottomDrawerHeightTextValue : TextView
    private lateinit var bottomDrawerWeightTextValue : TextView
    private lateinit var maleButtonImc : CardView
    private lateinit var femaleButtonImc : CardView
    private lateinit var bluefemalesymbolimc : ImageView
    private lateinit var bluemalesymbolimc : ImageView
    private lateinit var maleButtonImcBack : ImageView
    private lateinit var femaleButtonImcBack : ImageView
    private lateinit var localFile: File
    private lateinit var bitmap: Bitmap
    private lateinit var imcCard: CardView
    private lateinit var generatePlanNutrition : CardView
    private lateinit var ImcDrawerButton : CardView
    private lateinit var ImcDrawerValue : TextView
    private lateinit var ImcDrawerValueText : TextView
    private lateinit var biometricCard : LinearLayout
    private lateinit var biometricCardContent : ConstraintLayout
    private lateinit var goalCard : LinearLayout
    private lateinit var goalCardContent : ConstraintLayout
    private lateinit var ageCard : LinearLayout
    private lateinit var ageCardContent : ConstraintLayout
    private lateinit var cardsParentLayout : LinearLayout
    private lateinit var heightValueText : TextView
    private lateinit var weightValueText : TextView
    private lateinit var updateButton : CardView
    private lateinit var calculateButton : CardView
    private lateinit var swipeSelectorGoal : SwipeSelector
    private lateinit var swipeSelectorLevelFitness : SwipeSelector
    private lateinit var dayInput : TextInputEditText
    private lateinit var monthInput : TextInputEditText
    private lateinit var yearInput : TextInputEditText
    private var age : Int = 0
    private lateinit var blurView : BlurView
    private var userHeight: Int = 0
    private var userWeight: Float = 0f
    private var imc: Float = 0f
    private var imcIncreasing : Float = 0f
    private var canIncreaseImc : Boolean = true
    private lateinit var imcTextValue : TextView
    private var canOpenImcMenu : Boolean = true
    private var canOpenMacroMenu : Boolean = true
    private var canOpenMetMenu : Boolean = true
    private var heightValue : Int = 100
    private lateinit var maleBody : ImageView
    private lateinit var femaleBody : ImageView
    private var isMale : Boolean = true
    private var canCalculateDrawerImc : Boolean = true
    private var basalValue : Int = 0
    private var metValue : Int = 0
    private var recommendedCalories : Int = 0
    private var recommendedCarbs : Int = 0
    private var recommendedFats : Int = 0
    private var recommendedProtein : Int = 0
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentNutricaoBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)

        //INICIALIZAÇÃO DAS VARIAVIES
        auth = Firebase.auth
        biometricCard = root.findViewById(R.id.biometricCard)
        biometricCardContent = root.findViewById(R.id.biometricCardContent)
        ageCard = root.findViewById(R.id.ageCard)
        ageCardContent = root.findViewById(R.id.ageCardContent)
        cardsParentLayout = root.findViewById(R.id.cardParentLayout)
        sliderHeight = root.findViewById(R.id.heightSlider)
        sliderWeight = root.findViewById(R.id.weightSlider)
        updateButton = root.findViewById(R.id.updateButton)
        calculateButton = root.findViewById(R.id.calculateButton)
        heightValueText = root.findViewById(R.id.HeightValueText)
        weightValueText = root.findViewById(R.id.WeightValueText)
        genderPickerCalculator = root.findViewById(R.id.genderPickerCalculator)
        dayInput = root.findViewById(R.id.dayCalculatorEditText)
        monthInput = root.findViewById(R.id.monthCalculatorEditText)
        yearInput = root.findViewById(R.id.yearCalculatorEditText)
        biometricCard.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        ageCard.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)

        updateButton.setOnClickListener {
            updateData()
        }

        calculateButton.setOnClickListener {
            calculateValue()
        }

        sliderHeight.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {

            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {
                heightValueText.text = slider.value.toInt().toString() + "cm"
            }
        })
        sliderWeight.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {

            override fun onStartTrackingTouch(slider: Slider) {

            }

            override fun onStopTrackingTouch(slider: Slider) {
                weightValueText.text = slider.value.toInt().toString() + "kg"


            }
        })


        var biometricCardHeader = root.findViewById<ConstraintLayout>(R.id.biometricCardHeader)
        var ageCardHeader = root.findViewById<ConstraintLayout>(R.id.ageHeader)
        var goalsCardHeader = root.findViewById<ConstraintLayout>(R.id.goalsCardHeader)
        biometricCardHeader.setOnClickListener {
            expandBiometric(biometricCard)
        }
        ageCardHeader.setOnClickListener {
            expandAge(ageCard)
        }
        goalCard = root.findViewById(R.id.goalsCard)
        goalCardContent = root.findViewById(R.id.goalsCardContent)
        goalCard.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)


        swipeSelectorGoal = root.findViewById(R.id.goalSelectorGenerate)
        swipeSelectorGoal.setItems( // The first argument is the value for that item, and should in most cases be unique for the
            // current SwipeSelector, just as you would assign values to radio buttons.
            // You can use the value later on to check what the selected item was.
            // The value can be any Object, here we're using ints.
            SwipeItem(0, "Perder peso", "Pretende perder massa gorda, sem abdicar da massa magra já existente"),
            SwipeItem(1, "Manter peso", "Ciclo de manutenção, de forma a manter o peso e forma atual."),
            SwipeItem(2, "Ganhar peso", "Ganhar peso, maioritariamente proveniente do ganho de massa magra.")
        )


        swipeSelectorLevelFitness = root.findViewById(R.id.fitnessLevelSelectorGenerate)
        swipeSelectorLevelFitness.setItems( // The first argument is the value for that item, and should in most cases be unique for the
            // current SwipeSelector, just as you would assign values to radio buttons.
            // You can use the value later on to check what the selected item was.
            // The value can be any Object, here we're using ints.
            SwipeItem(0, "Sedentário", "Não treina, passando a maior parte do tempo deitado ou sentado."),
            SwipeItem(1, "Moderado", "Pratica algum tipo de atividade fisica de 1 a 2 vezes por semana."),
            SwipeItem(2, "Ativo", "Pratica atividade física de 3 a 5 vezes por semana."),
            SwipeItem(3, "Muito ativo", "Pratica atividade física pelo menos 6 vezes por semana.")
        )
        swipeSelectorGoal.selectItemWithValue(1)
        swipeSelectorLevelFitness.selectItemWithValue(1)
        goalsCardHeader.setOnClickListener {
            Log.d("testclick", " HASCLICKED")
            expandGoals(goalCard)
        }

        database = FirebaseFirestore.getInstance()
        val userId = auth.currentUser!!.uid
        adapter = MyViewPagerAdapterNutricao(childFragmentManager)
        calculatorsViewPager = root.findViewById(R.id.calculatorsViewPager)
        dotsIndicator = root.findViewById(R.id.worm_dots_indicatornutricao)
        adapter.addFragment(ImcFragment())
        adapter.addFragment(MacroFragment())
        adapter.addFragment(MetabolicFragment())
        calculatorsViewPager.adapter = adapter
        dotsIndicator.setViewPager(calculatorsViewPager)
        calculatorsViewPager.setOffscreenPageLimit(2);

        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference("images/massabolonhesa.png")
        localFile = File.createTempFile("image", ".png")
        storageReference.getFile(localFile).addOnSuccessListener {
            bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
            //imageTest.setImageBitmap(bitmap)

        }.addOnFailureListener {
            Log.d("ok", "Ok")
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    class MyViewPagerAdapterNutricao(manager: FragmentManager) : FragmentPagerAdapter(manager) {
        private val fragmentList : MutableList<Fragment> = ArrayList()
        private val titleList : MutableList<String> = ArrayList()
        override fun getItem(position: Int): Fragment {
            Log.d("testposition", position.toString())

            return fragmentList[position]
        }

        override fun getCount(): Int {
            Log.d("ViewPagerTest", fragmentList.size.toString())
            return fragmentList.size
        }

        fun addFragment(fragment : Fragment){
            fragmentList.add(fragment)
        }
    }

    fun expandAge(view : View){

        if(ageCardContent.visibility == View.GONE){
            ageCardContent.visibility = View.VISIBLE
        }else{
            ageCardContent.visibility = View.GONE
        }
        TransitionManager.beginDelayedTransition(ageCard,AutoTransition())
        beginAuto(cardsParentLayout)
    }

    fun expandBiometric(view : View){

        if(biometricCardContent.visibility == View.GONE){
            biometricCardContent.visibility = View.VISIBLE
        }else{
            biometricCardContent.visibility = View.GONE
        }
        TransitionManager.beginDelayedTransition(biometricCard,AutoTransition())
        beginAuto(cardsParentLayout)
    }
    fun expandGoals(view : View){

        if(goalCardContent.visibility == View.GONE){
            goalCardContent.visibility = View.VISIBLE
        }else{
            goalCardContent.visibility = View.GONE
        }
        TransitionManager.beginDelayedTransition(goalCard,AutoTransition())
        beginAuto(cardsParentLayout)
    }

    fun beginAuto(viewGroup: ViewGroup?) {
        val transition = AutoTransition()
        transition.duration = 250
        transition.ordering = TransitionSet.ORDERING_TOGETHER
        TransitionManager.beginDelayedTransition(viewGroup, transition)
    }

    fun updateData(){
        database = FirebaseFirestore.getInstance()
        auth = Firebase.auth
        var currentUser = auth.currentUser!!.uid
        Log.d("testValueCalculator", genderPickerCalculator.getText() +"|" + dayInput.text.toString() + monthInput.text.toString()+ yearInput.text.toString())
        val newData = hashMapOf(
            "height" to sliderHeight.value.toInt(),
            "weight" to sliderWeight.value.toFloat(),
            "goal" to swipeSelectorGoal.selectedItem.title,
            "levelOfActivity" to swipeSelectorLevelFitness.selectedItem.title,
            "gender" to genderPickerCalculator.getText(),
            "dayOfBirth" to  dayInput.text.toString().toInt(),
            "monthOfBirth" to monthInput.text.toString().toInt(),
            "yearOfBirth" to yearInput.text.toString().toInt()
        )
        database.collection("Users").document(currentUser).set(newData, SetOptions.merge()).addOnCompleteListener {
            MotionToast.createColorToast(this.requireActivity(),
                "Dados adicionados!",
                "Os seus dados foram submetidos com sucesso",
                MotionToastStyle.INFO,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this.requireActivity(),R.font.montserrat))

        }
            .addOnFailureListener{
                MotionToast.createColorToast(this.requireActivity(),
                    "Erro ao adicionar dados!",
                    "Verifique a sua ligação à Internet",
                    MotionToastStyle.ERROR,
                    MotionToast.GRAVITY_BOTTOM,
                    MotionToast.LONG_DURATION,
                    ResourcesCompat.getFont(this.requireActivity(),R.font.montserrat))
            }

    }

    fun calculateValue(){
        var gender = genderPickerCalculator.getText().toString()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
             age = Period.between(
                LocalDate.of(yearInput.text.toString().toInt(), monthInput.text.toString().toInt(), dayInput.text.toString().toInt()),
                LocalDate.now()
            ).years.toInt()

        }

        var height = sliderHeight.value.toInt()
        var weight = sliderWeight.value.toInt()
        var ageValue = age
        var calculateimc = ((weight / ((height / 100f) * (height / 100f)) * 100.00f.toInt()) / 100.00f).toInt()
        var imcString : String = ""
        if(calculateimc < 18.5){
            imcString = "abaixo do peso"
        }
        if(calculateimc >= 18.5 && calculateimc < 25){
            imcString = "dentro do peso normal"
        }
        if(calculateimc >= 25 && calculateimc < 30){
            imcString = "acima do peso"
        }
        if(calculateimc >= 30 && calculateimc < 35){
            imcString = "obeso"
        }
        if(calculateimc >= 35){
            imcString = "obeso mórbido"
        }

        if(gender == "Homem"){
            basalValue = ((9.99f * weight) + (6.25f * height) - (4.92f * age) + 5).toInt()
        }
        if(gender == "Mulher"){
            basalValue =((9.99f * weight) + (6.25f * height) - (4.92f * age) - 161).toInt()
        }
        if(swipeSelectorLevelFitness.selectedItem.title == "Sedentário"){
            metValue = (basalValue * 1.2f).toInt()
        }
        if(swipeSelectorLevelFitness.selectedItem.title == "Moderado"){
            metValue = (basalValue * 1.300f).toInt()
        }
        if(swipeSelectorLevelFitness.selectedItem.title == "Ativo"){
            metValue = (basalValue * 1.375f).toInt()
        }
        if(swipeSelectorLevelFitness.selectedItem.title == "Muito ativo"){
            metValue = (basalValue * 1.6f).toInt()
        }

        if(swipeSelectorGoal.selectedItem.title == "Perder peso"){
            recommendedCalories = metValue - 200
            recommendedCarbs = (recommendedCalories * 0.65 / 4f).toInt()
            recommendedProtein = (recommendedCalories * 0.3 / 4f).toInt()
            recommendedFats = (recommendedCalories * 0.15 / 9f).toInt()
        }
        if(swipeSelectorGoal.selectedItem.title == "Manter peso"){
            recommendedCalories = metValue
            recommendedCarbs = (recommendedCalories * 0.5 / 4f).toInt()
            recommendedProtein = (recommendedCalories * 0.28 / 4f).toInt()
            recommendedFats = (recommendedCalories * 0.22 / 9f).toInt()
        }

        if(swipeSelectorGoal.selectedItem.title == "Ganhar peso"){
            recommendedCalories = metValue + 200
            recommendedCarbs = (recommendedCalories * 0.38 / 4f).toInt()
            recommendedProtein = (recommendedCalories * 0.28 / 4f).toInt()
            recommendedFats = (recommendedCalories * 0.32 / 9f).toInt()
        }

        Log.d("testValuesCalculator", ageValue.toString() + "|" + metValue.toString() + "|" +  recommendedCalories.toString() + "|"  +recommendedProtein.toString() )


        if(calculatorsViewPager.currentItem == 0){
            MotionToast.createColorToast(this.requireActivity(),
                "IMC",
                "O seu IMC é de " + calculateimc.toString() + "\n" + "Encontra-se " + imcString,
                MotionToastStyle.INFO,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this.requireActivity(),R.font.montserrat))
        }

        if(calculatorsViewPager.currentItem == 1){
            MotionToast.createColorToast(this.requireActivity(),
                "Macronutrientes",
                recommendedProtein.toString() + "|" + recommendedCarbs.toString()+
                        "|"  + recommendedFats.toString(),
                MotionToastStyle.INFO,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this.requireActivity(),R.font.montserrat))
        }

        if(calculatorsViewPager.currentItem == 2){
            MotionToast.createColorToast(this.requireActivity(),
                "Metabolismo",
                 basalValue.toString() + "|" + metValue.toString()
                + "|" + recommendedCalories.toString(),
                MotionToastStyle.INFO,
                MotionToast.GRAVITY_BOTTOM,
                MotionToast.LONG_DURATION,
                ResourcesCompat.getFont(this.requireActivity(),R.font.montserrat))
        }
    }


}