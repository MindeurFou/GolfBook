package com.example.golfbook.ui.loungeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.golfbook.R
import com.example.golfbook.databinding.FragmentLoungeDetailsBinding
import com.example.golfbook.extensions.ExceptionExtensions.toast
import com.example.golfbook.ui.ActivityViewModel
import com.example.golfbook.ui.compoundedComponents.PlayerPreviewItem
import com.example.golfbook.utils.Resource
import java.lang.StringBuilder

class LoungeDetailsFragment : Fragment() {

    private lateinit var binding: FragmentLoungeDetailsBinding

    private val args: LoungeDetailsFragmentArgs by navArgs()

    private val mainViewModel: ActivityViewModel by activityViewModels()

    private lateinit var viewModelFactory: LoungeDetailsViewModelFactory

    private val viewModel: LoungeDetailsViewModel by viewModels(
            factoryProducer =  { viewModelFactory }
    )



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentLoungeDetailsBinding.inflate(inflater)
        viewModelFactory = LoungeDetailsViewModelFactory(args, mainViewModel.currentPlayer!!)

        val adapter = ArrayAdapter(requireContext(), R.layout.list_courses, listOf<String>())
        binding.editTextCourse.setAdapter(adapter)


        subscribeObservers()


        binding.btnLeave.setOnClickListener { viewModel.leaveLounge() }

        binding.btnStart.setOnClickListener { view ->

        }


        return binding.root
    }

    private fun subscribeObservers() {

        viewModel.listCoursesName.observe(viewLifecycleOwner) { resource ->

            if (resource is Resource.Failure)
                resource.exception.toast(requireContext())
            else if (resource is Resource.Success) {

                val listCourseName = resource.data

                val adapter = ArrayAdapter(requireContext(), R.layout.list_courses, listCourseName)
                binding.editTextCourse.setAdapter(adapter)
            }

        }

        viewModel.lounge.observe(viewLifecycleOwner) { resource ->

            if (resource is Resource.Failure)
                resource.exception.toast(requireContext())
            else if (resource is Resource.Success) {

                val lounge = resource.data

                binding.loungeTitle.text = requireContext().getString(R.string.loungeName, lounge.name)

                binding.playersContainer.removeAllViews()

                for (player in lounge.playersInLounge!!) {

                    val item = PlayerPreviewItem(requireContext(), player, PlayerPreviewItem.Companion.PlayerPreviewItemSize.BIG)
                    binding.playersContainer.addView(item)

                }

                binding.playersContainer.invalidate()
                binding.playersContainer.requestLayout()

                val sb = StringBuilder()
                // TODO afficher un textview des playersReady une fois que le backend est fait

            }

        }

        viewModel.leaveLoungeState.observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.Failure)
                resource.exception.toast(requireContext())
            else if (resource is Resource.Success) {
                navigateToHomeFragment()
            }
        }


    }

    private fun navigateToHomeFragment() {
        val action = LoungeDetailsFragmentDirections.actionLoungeDetailsFragmentToHomeFragment(null)
        findNavController().navigate(action)
    }

    private fun navigateToGameFragment() {
        val action = LoungeDetailsFragmentDirections.actionLoungeDetailsFragmentToGameViewPagerFragment("updateHere")
        findNavController().navigate(action)
    }

}