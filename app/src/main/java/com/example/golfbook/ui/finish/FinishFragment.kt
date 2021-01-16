package com.example.golfbook.ui.finish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.golfbook.databinding.FragmentFinishBinding
import com.example.golfbook.data.model.Player


class FinishFragment : Fragment() {

    private lateinit var binding: FragmentFinishBinding


    private lateinit var viewModelFactory: FinishViewModelFactory

    private val viewModel: FinishViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentFinishBinding.inflate(inflater)

        viewModelFactory = FinishViewModelFactory()





        return binding.root
    }

    private fun navigateToHomeFragment(player: Player, isMainPlayer: Boolean) {
        val action = FinishFragmentDirections.actionFinishFragmentToHomeFragment("patrick")
        findNavController().navigate(action)
    }
}