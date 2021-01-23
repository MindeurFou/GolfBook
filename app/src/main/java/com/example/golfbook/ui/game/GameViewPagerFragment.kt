package com.example.golfbook.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.example.golfbook.R
import com.example.golfbook.data.model.Player
import com.example.golfbook.databinding.FragmentViewPagerBinding
import com.example.golfbook.extensions.ExceptionExtensions.toast
import com.example.golfbook.utils.Resource

class GameViewPagerFragment : Fragment() {

    private lateinit var binding: FragmentViewPagerBinding

    private val args: GameViewPagerFragmentArgs by navArgs()

    private lateinit var viewModelFactory: GameViewModelFactory

    private val viewModel: GameViewModel by viewModels(
            factoryProducer = { viewModelFactory }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentViewPagerBinding.inflate(inflater)

        viewModelFactory = GameViewModelFactory(args)


        viewModel.lounge.observe(viewLifecycleOwner) { resource ->

            when (resource) {

                is Resource.Failure -> {
                    binding.progressCircular.visibility = View.GONE
                    resource.exception.toast(requireContext())
                }

                is Resource.Loading -> binding.progressCircular.visibility = View.VISIBLE

                is Resource.Success -> {
                    //binding.progressCircular.visibility = View.GONE

                    //if () on a la subcollection game ==> s'y abonner
                }
            }
        }

        val players = listOf<Player>()


        val pagerAdapter = IndividualGameViewPagerAdapter(
            players,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        binding.viewPager.adapter = pagerAdapter

        return view
    }

}