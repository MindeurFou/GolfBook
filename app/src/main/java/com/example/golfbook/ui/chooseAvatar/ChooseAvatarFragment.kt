package com.example.golfbook.ui.chooseAvatar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.golfbook.R
import com.example.golfbook.databinding.FragmentChooseAvatarBinding
import com.example.golfbook.data.model.Player
import kotlin.random.Random

class ChooseAvatarFragment : Fragment() {


    private lateinit var binding: FragmentChooseAvatarBinding

    private val args: ChooseAvatarFragmentArgs by navArgs()

    private lateinit var viewModelFactory: ChooseAvatarViewModelFactory

    private val viewModel: ChooseAvatarViewModel by viewModels(
            factoryProducer = { viewModelFactory }
    )



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentChooseAvatarBinding.inflate(layoutInflater)

        viewModelFactory = ChooseAvatarViewModelFactory(args)

        initChooseNameTitle()

        val viewStateObserver = Observer<Player> { player ->

            if (player.avatarResourceId != -1){
                val drawable = ContextCompat.getDrawable(requireContext(), player.avatarResourceId)
                binding.imageAvatar.setImageDrawable(drawable)
            }


            if (player.name == null)
                binding.editTextName.text?.clear()
            else
                binding.editTextName.setText(player.name)

        }

        viewModel.viewState.observe(viewLifecycleOwner, viewStateObserver)

        binding.btnValidate.setOnClickListener {

            val player = viewModel.viewState.value!!

            val name = binding.editTextName.text?.toString()

            if (name == null || player.avatarResourceId == -1) {
                Toast.makeText(requireContext(), R.string.choosePlayerError, Toast.LENGTH_LONG).show()
            } else{
                navigateToHomeFragment(player)
            }

        }


        return binding.root
    }

    private fun initChooseNameTitle() {

        val text: String?

        if (args.player?.name == null ) {

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

    private fun navigateToHomeFragment( player: Player) {
        val action = ChooseAvatarFragmentDirections.actionChooseAvatarFragmentToHomeFragment(player)
        findNavController().navigate(action)
    }

    fun updateAvatar(view: View) {

        val id: Int = view.id

        val resourceId = when (id) {

            R.id.man1View -> R.drawable.man1
            R.id.man2View -> R.drawable.man2
            R.id.man3View -> R.drawable.man3
            R.id.man4View -> R.drawable.man4
            R.id.man5View -> R.drawable.man5
            R.id.man6View -> R.drawable.man6
            R.id.man7View -> R.drawable.man7
            R.id.man8View -> R.drawable.man8
            R.id.woman1View -> R.drawable.woman1
            R.id.woman2View -> R.drawable.woman2
            R.id.woman3View -> R.drawable.woman3
            R.id.woman4View -> R.drawable.woman4
            R.id.woman5View -> R.drawable.woman5
            R.id.woman6View -> R.drawable.woman6
            R.id.woman7View -> R.drawable.woman7
            R.id.woman8View -> R.drawable.woman8
            else -> R.drawable.man1
        }

        viewModel.updateAvatar(resourceId)

    }


}