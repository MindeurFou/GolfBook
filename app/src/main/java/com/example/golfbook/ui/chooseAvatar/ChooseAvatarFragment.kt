package com.example.golfbook.ui.chooseAvatar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.golfbook.R
import com.example.golfbook.databinding.FragmentChooseAvatarBinding
import com.example.golfbook.extensions.ExceptionExtensions.toast
import com.example.golfbook.extensions.ViewExtensions.textChanges
import com.example.golfbook.ui.ActivityViewModel
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlin.random.Random

class ChooseAvatarFragment : Fragment() {


    private lateinit var binding: FragmentChooseAvatarBinding

    private val args: ChooseAvatarFragmentArgs by navArgs()

    private lateinit var chooseAvatarViewModelFactory: ChooseAvatarViewModelFactory
    private val viewModel: ChooseAvatarViewModel by viewModels(
            factoryProducer =  { chooseAvatarViewModelFactory }
    )

    var backdropShown: Boolean = false

    private val mainViewModel: ActivityViewModel by activityViewModels()

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentChooseAvatarBinding.inflate(layoutInflater)
        chooseAvatarViewModelFactory = ChooseAvatarViewModelFactory(mainViewModel.localPlayer)
        viewModel.processArgs(args)


        subscribeObservers()

        initChooseNameTitle()

        binding.editTextName.textChanges()
            .debounce(300)
            .onEach {
                viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeNameEvent(it.toString()))
            }.launchIn(lifecycleScope)


        binding.imageAvatar.setOnClickListener (
            AvatarImageClickListener(requireContext(), binding.mainLayout, true, setChangeAvatarEvent = null, this)
        )

        binding.btnValidate.setOnClickListener {
            viewModel.setChooseAvatarEvent(ChooseAvatarEvent.SavePlayerEvent)
        }

        bindOnClickListenerImagesAvatar()

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

    private fun bindOnClickListenerImagesAvatar() {

        val container = binding.mainLayout

        binding.man1View.setOnClickListener(AvatarImageClickListener(requireContext(),
            container, setChangeAvatarEvent = { viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeDrawableResourceIdEvent(R.drawable.man1))}, fragment = this))

        binding.man2View.setOnClickListener(AvatarImageClickListener(requireContext(),
                container, setChangeAvatarEvent = { viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeDrawableResourceIdEvent(R.drawable.man2)) }, fragment = this))

        binding.man3View.setOnClickListener(AvatarImageClickListener(requireContext(),
                container, setChangeAvatarEvent = { viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeDrawableResourceIdEvent(R.drawable.man3))}, fragment = this))

        binding.man4View.setOnClickListener(AvatarImageClickListener(requireContext(),
                container, setChangeAvatarEvent = { viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeDrawableResourceIdEvent(R.drawable.man4))}, fragment = this))

        binding.man5View.setOnClickListener(AvatarImageClickListener(requireContext(),
                container, setChangeAvatarEvent = { viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeDrawableResourceIdEvent(R.drawable.man5))}, fragment = this))

        binding.man6View.setOnClickListener(AvatarImageClickListener(requireContext(),
                container, setChangeAvatarEvent = { viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeDrawableResourceIdEvent(R.drawable.man6))}, fragment = this))

        binding.man7View.setOnClickListener(AvatarImageClickListener(requireContext(),
                container, setChangeAvatarEvent = { viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeDrawableResourceIdEvent(R.drawable.man7))}, fragment = this))

        binding.man8View.setOnClickListener(AvatarImageClickListener(requireContext(),
                container, setChangeAvatarEvent = { viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeDrawableResourceIdEvent(R.drawable.man8))}, fragment = this))

        binding.woman1View.setOnClickListener(AvatarImageClickListener(requireContext(),
                container, setChangeAvatarEvent = { viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeDrawableResourceIdEvent(R.drawable.woman1))}, fragment = this))

        binding.woman2View.setOnClickListener(AvatarImageClickListener(requireContext(),
                container, setChangeAvatarEvent = { viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeDrawableResourceIdEvent(R.drawable.woman2))}, fragment = this))

        binding.woman3View.setOnClickListener(AvatarImageClickListener(requireContext(),
                container, setChangeAvatarEvent = { viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeDrawableResourceIdEvent(R.drawable.woman3))}, fragment = this))

        binding.woman4View.setOnClickListener(AvatarImageClickListener(requireContext(),
                container, setChangeAvatarEvent = { viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeDrawableResourceIdEvent(R.drawable.woman4))}, fragment = this))

        binding.woman5View.setOnClickListener(AvatarImageClickListener(requireContext(),
                container, setChangeAvatarEvent = { viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeDrawableResourceIdEvent(R.drawable.woman5))}, fragment = this))

        binding.woman6View.setOnClickListener(AvatarImageClickListener(requireContext(),
                container, setChangeAvatarEvent = { viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeDrawableResourceIdEvent(R.drawable.woman6))}, fragment = this))

        binding.woman7View.setOnClickListener(AvatarImageClickListener(requireContext(),
                container, setChangeAvatarEvent = { viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeDrawableResourceIdEvent(R.drawable.woman7))}, fragment = this))

        binding.woman8View.setOnClickListener(AvatarImageClickListener(requireContext(),
                container, setChangeAvatarEvent = { viewModel.setChooseAvatarEvent(ChooseAvatarEvent.ChangeDrawableResourceIdEvent(R.drawable.woman8))}, fragment = this))


    }

    private fun navigateToHomeFragment(idPlayer: String) {
        val action = ChooseAvatarFragmentDirections.actionChooseAvatarFragmentToHomeFragment(idPlayer)
        findNavController().navigate(action)
    }


}