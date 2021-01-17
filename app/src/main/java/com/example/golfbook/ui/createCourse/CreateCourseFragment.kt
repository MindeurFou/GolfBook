package com.example.golfbook.ui.createCourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.golfbook.R
import com.example.golfbook.databinding.FragmentCreateCourseBinding
import com.example.golfbook.extensions.ExceptionExtensions.toast
import com.example.golfbook.ui.chooseAvatar.ChooseAvatarFragmentDirections
import com.example.golfbook.utils.Resource

class CreateCourseFragment : Fragment() {

    private lateinit var binding: FragmentCreateCourseBinding

    private val viewModel: CreateCourseViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCreateCourseBinding.inflate(inflater)

        subscribeObservers()



        binding.btnValidateCourse.setOnClickListener {

            if (checkCourseIsValid())
                viewModel.setCreateCourseEvent(CreateCourseEvent.SaveCourseEvent)
            else
                Toast.makeText(requireContext(), R.string.invalidCourse, Toast.LENGTH_LONG).show()
        }

        return binding.root
    }


    private fun subscribeObservers() {

        viewModel.saveCourseState.observe(viewLifecycleOwner) { resource ->

            when (resource) {

                is Resource.Loading -> {}

                is Resource.Failure -> resource.exception.toast(requireContext())

                is Resource.Success -> {
                    // hide progressBar
                    navigateToHomeFragment("bite")
                }
            }

        }
    }

    private fun checkCourseIsValid() : Boolean {
        // TODO

        return true
    }

    private fun navigateToHomeFragment(idPlayer: String) {
        val action = ChooseAvatarFragmentDirections.actionChooseAvatarFragmentToHomeFragment(idPlayer)
        findNavController().navigate(action)
    }
}