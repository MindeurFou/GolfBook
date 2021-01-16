package com.example.golfbook.ui.splash

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.golfbook.R

class SplashFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        Handler().postDelayed({

            val action = SplashFragmentDirections.actionSplashFragmentToChooseAvatarFragment(
                    name = null,
                    drawableResourceId = -1,
                    managerId = null,
                    playerId = null
            )

            findNavController().navigate(action)
        }, 3000)

        return view
    }




}