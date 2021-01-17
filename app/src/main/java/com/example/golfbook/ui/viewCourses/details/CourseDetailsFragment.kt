package com.example.golfbook.ui.viewCourses.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.example.golfbook.R
import com.example.golfbook.extensions.ExceptionExtensions.toast
import com.example.golfbook.ui.viewCourses.CourseDetailsFragmentArgs
import com.example.golfbook.utils.Resource

class CourseDetailsFragment : Fragment() {

    private val args: CourseDetailsFragmentArgs by navArgs()

    private lateinit var viewmodelFactory: CourseDetailsViewModelFactory

    private val viewModel: CourseDetailsViewModel by viewModels(
        factoryProducer = { viewmodelFactory }
    )

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view =  inflater.inflate(R.layout.fragment_courses_details, container, false)

        viewmodelFactory = CourseDetailsViewModelFactory(args)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewCourseHoles)

        viewModel.course.observe(viewLifecycleOwner) { resource ->

            when (resource) {

                is Resource.Loading -> {}

                is Resource.Failure -> resource.exception.toast(requireContext())

                is Resource.Success -> {

                    recyclerView.adapter = CourseDetailsAdapter(requireContext(), resource.data.holes)
                }
            }

        }

        return view
    }


}