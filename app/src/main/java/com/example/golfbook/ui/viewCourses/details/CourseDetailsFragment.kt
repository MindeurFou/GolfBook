package com.example.golfbook.ui.viewCourses.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.golfbook.databinding.FragmentCoursesDetailsBinding

import com.example.golfbook.extensions.ExceptionExtensions.toast
import com.example.golfbook.utils.Resource

class CourseDetailsFragment : Fragment() {

    private lateinit var binding: FragmentCoursesDetailsBinding

    private val args: CourseDetailsFragmentArgs by navArgs()

    private lateinit var viewmodelFactory: CourseDetailsViewModelFactory

    private val viewModel: CourseDetailsViewModel by viewModels(
        factoryProducer = { viewmodelFactory }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCoursesDetailsBinding.inflate(inflater)

        viewmodelFactory = CourseDetailsViewModelFactory(args)

        binding.recyclerViewCourseHoles.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        viewModel.course.observe(viewLifecycleOwner) { resource ->

            when (resource) {

                is Resource.Loading -> binding.progressCircular.visibility = View.VISIBLE

                is Resource.Failure -> {
                    binding.progressCircular.visibility = View.GONE
                    resource.exception.toast(requireContext())
                }

                is Resource.Success -> {
                    binding.progressCircular.visibility = View.GONE

                    val course = resource.data

                    binding.courseTitle.text = course.name
                    binding.numberOfHoles.text = course.numberOfHoles.toString()
                    binding.par.text = course.par.toString()

                    binding.recyclerViewCourseHoles.adapter = CourseDetailsAdapter(requireContext(), course.holes)
                    binding.recyclerViewCourseHoles.visibility = View.VISIBLE
                }
            }

        }

        return binding.root
    }


}