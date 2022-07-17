package com.doublej.jjfitnessapp.ui.StartFragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.doublej.jjfitnessapp.R
import com.doublej.jjfitnessapp.databinding.LoadingScreenFragmentBinding
import com.github.ybq.android.spinkit.SpinKitView

class loadingScreenFragment : Fragment() {

    companion object {
        fun newInstance() = loadingScreenFragment()
    }

    private lateinit var viewModel: LoadingScreenViewModel
    private var canStartFirstAnimation : Boolean = true
    private lateinit var logo : ImageView
    private var _binding: LoadingScreenFragmentBinding? = null
    private val binding get() = _binding!!
    private var counter : Float = 0f
    private lateinit var loadingView : SpinKitView
    private lateinit var loadingLayout : FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = LoadingScreenFragmentBinding.inflate(inflater, container, false)
        val root: View = binding.root
        logo = root.findViewById(R.id.logoLoading)
        loadingView = root.findViewById(R.id.spinKitLoading)
        loadingLayout = root.findViewById(R.id.loadingLayout)
        startFirstAnimation()

        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(LoadingScreenViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun startFirstAnimation(){
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {

                if(canStartFirstAnimation == true){
                    val paramsLogoCard = logo.layoutParams as ConstraintLayout.LayoutParams

                    logo.requestLayout()

                    counter += 1f

                    if(counter > 50f){



                        if(logo.alpha < 1f){
                            logo.alpha += 0.125f
                        }
                        if(logo.alpha >=1f){
                            logo.alpha = 1f
                        }
                        if(paramsLogoCard.matchConstraintPercentHeight < 0.4f){
                            paramsLogoCard.matchConstraintPercentHeight += 0.05f
                        }
                        if(paramsLogoCard.matchConstraintPercentHeight >= 0.4f){
                            paramsLogoCard.matchConstraintPercentHeight = 0.4f


                        }
                    }

                    if(counter > 80f){
                        if(loadingView.alpha < 1f){
                            loadingView.alpha += 0.125f
                        }
                        if(loadingView.alpha >=1f){
                            loadingView.alpha = 1f

                        }
                    }

                    if(counter >350f){
                        Log.d("entrou", "entrou")
                        loadingLayout.alpha = 0f
                        canStartFirstAnimation = false
                    }
                    handler.postDelayed(this, 10)



                }
            }

        }, 0)
    }

}