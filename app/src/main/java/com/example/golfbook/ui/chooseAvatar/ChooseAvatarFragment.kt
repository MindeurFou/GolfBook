package com.example.golfbook.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.example.golfbook.GameViewModel
import com.example.golfbook.R

class ChooseAvatarFragment : Fragment() {

    private val viewModel: ChooseAvatarViewModel by viewModels(
            factoryProducer = 
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_choose_avatar, container, false)



        return view
    }
}