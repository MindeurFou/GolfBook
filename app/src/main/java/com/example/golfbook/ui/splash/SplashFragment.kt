package com.example.golfbook.ui

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
            findNavController().navigate(R.id.action_splashFragment_to_chooseAvatarFragment)
        }, 3000)

        return view
    }
}