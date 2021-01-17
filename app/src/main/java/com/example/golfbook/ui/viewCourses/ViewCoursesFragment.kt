package com.example.golfbook.ui.viewCourses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.golfbook.R
import com.example.golfbook.databinding.FragmentViewCoursesBinding
import com.example.golfbook.extensions.ExceptionExtensions.toast
import com.example.golfbook.utils.Resource

class ViewCoursesFragment : Fragment() {

    private lateinit var binding: FragmentViewCoursesBinding

    private val viewModel: ViewCoursesViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        binding = FragmentViewCoursesBinding.inflate(inflater)

        viewModel.coursesName.observe(viewLifecycleOwner) { resource ->

            when (resource) {

                is Resource.Loading -> binding.progressCircular.visibility = View.VISIBLE

                is Resource.Failure -> {

                    binding.progressCircular.visibility = View.GONE

                    resource.exception.toast(requireContext())
                }

                is Resource.Success -> {

                    binding.progressCircular.visibility = View.GONE
                    binding.recyclerViewCourses.visibility = View.VISIBLE

                    binding.recyclerViewCourses.adapter = AllCoursesAdapter(resource.data) { courseName ->
                        navigateToCoursesDetailsFragment(courseName)
                    }
                }
            }
        }

        return binding.root
    }

    private fun navigateToCoursesDetailsFragment(courseName: String) {
        val action = ViewCoursesFragmentDirections.actionViewCoursesFragmentToCourseDetails(courseName)
        findNavController().navigate(action)
    }


}