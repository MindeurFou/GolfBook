package com.example.golfbook.ui.finish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.golfbook.R
import com.example.golfbook.databinding.FragmentFinishBinding
import com.example.golfbook.databinding.FragmentHomeBinding
import com.example.golfbook.model.Player
import com.example.golfbook.ui.compoundedComponents.LoungeItem


class FinishFragment : Fragment() {

    private lateinit var binding: FragmentFinishBinding

    private val args:  by navArgs()

    private lateinit var viewModelFactory: FinishViewModelFactory

    private val viewModel: FinishViewModel by viewModels(
        factoryProducer = { viewModelFactory }
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentFinishBinding.inflate(inflater)

        viewModelFactory = FinishViewModelFactory(args)


        val observer = Observer<HomeViewState> { viewState ->

            val drawable = ContextCompat.getDrawable(requireContext(), viewState.player.avatarResourceId)
            binding.imageAvatarHome.setImageDrawable(drawable)

            binding.avatarName.text = viewState.player.name

        }

        viewModel.viewState.observe(viewLifecycleOwner, observer)

        binding.btnUpdatePlayer.setOnClickListener {
            navigateToChooseAvatarFragment(viewModel.viewState.value!!.player, true)
        }

        binding.btnManageOtherPlayer.setOnClickListener {
            navigateToChooseAvatarFragment(viewModel.viewState.value!!.player, false)
        }


        return binding.root
    }

    private fun navigateToChooseAvatarFragment(player: Player, isMainPlayer: Boolean) {
        val action = HomeFragmentDirections.actionHomeFragmentToChooseAvatarFragment(player.name, player.avatarResourceId, isMainPlayer)
        findNavController().navigate(action)
    }
}