package com.example.golfbook.ui.chooseAvatar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.navArgument
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.golfbook.R
import com.example.golfbook.databinding.FragmentChooseAvatarBinding
import com.example.golfbook.data.model.Player
import com.example.golfbook.extensions.ExceptionExtensions.toast
import com.example.golfbook.extensions.ViewExtensions.textChanges
import com.example.golfbook.ui.home.HomeEvent
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.random.Random

class ChooseAvatarFragment : Fragment() {


    private lateinit var binding: FragmentChooseAvatarBinding

    private val args: ChooseAvatarFragmentArgs by navArgs()

    private lateinit var viewModelFactory: ChooseAvatarViewModelFactory

    private val viewModel: ChooseAvatarViewModel by viewModels(
            factoryProducer = { viewModelFactory }
    )

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentChooseAvatarBinding.inflate(layoutInflater)
        viewModelFactory = ChooseAvatarViewModelFactory()
        viewModel.processArgs(args)


        subscribeObservers()

        initChooseNameTitle()

        binding.editTextName.textChanges()
            .debounce(300)
            .onEach {
                viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeNameEvent(it.toString()))
            }.launchIn(lifecycleScope)

        // TODO faire le setEvent(ChangeResourceId)

        binding.btnValidate.setOnClickListener {
            viewModel.setChooseAvatarEvent(ChooseAvatarEvent.SavePlayerEvent)
        }

        return binding.root
    }

    private fun initChooseNameTitle() {

        val text: String?

        if (viewModel.player.value!!.name == null ) {

            val textArray: Array<String> = requireContext().resources.getStringArray(R.array.emptyNameHeader)
            val index = Random.nextInt(textArray.size)
            text = textArray[index]

        } else {

            val textArray: Array<String> = requireContext().resources.getStringArray(R.array.knownNameHeader)
            val index = Random.nextInt(textArray.size)
            text = textArray[index]
        }

        binding.chooseNameTitle.text = text
    }

    private fun subscribeObservers() {

        viewModel.player.observe(viewLifecycleOwner) { player ->


            val drawable = ContextCompat.getDrawable(requireContext(), player.drawableResourceId)
            binding.imageAvatar.setImageDrawable(drawable)


            //Log.d("mdebug", player.toString())

            if (player.name != null)
                binding.name.setText(player.name)

        }


        viewModel.savePlayerState.observe(viewLifecycleOwner) { resource ->

            when (resource) {

                is Resource.Failure -> {
                    binding.progressBar.visibility = View.GONE
                    resource.exception.toast(requireContext())
                }

                is Resource.Loading -> binding.progressBar.visibility = View.VISIBLE

                is Resource.Success -> {
                    binding.progressBar.visibility = View.GONE
                    navigateToHomeFragment(resource.data)
                }
            }

        }

    }

    private fun navigateToHomeFragment(idPlayer: String) {
        val action = ChooseAvatarFragmentDirections.actionChooseAvatarFragmentToHomeFragment(idPlayer)
        findNavController().navigate(action)
    }





}