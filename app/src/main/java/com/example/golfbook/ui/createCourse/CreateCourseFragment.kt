package com.example.golfbook.ui.createCourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.golfbook.R
import com.example.golfbook.databinding.FragmentCreateCourseBinding
import com.example.golfbook.extensions.ExceptionExtensions.toast
import com.example.golfbook.extensions.ViewExtensions.textChanges
import com.example.golfbook.ui.chooseAvatar.ChooseAvatarFragmentDirections
import com.example.golfbook.ui.compoundedComponents.HoleInputItem
import com.example.golfbook.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class CreateCourseFragment : Fragment() {

    private lateinit var binding: FragmentCreateCourseBinding

    private val viewModel: CreateCourseViewModel by viewModels()

    @ExperimentalCoroutinesApi
    @FlowPreview
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCreateCourseBinding.inflate(inflater)

        subscribeObservers()

        // save ui accross configuration changes
        if (!viewModel.name.value.isNullOrBlank()) {
            binding.courseNameEditText.setText(viewModel.name.value)
        }

        binding.courseNameEditText.textChanges()
                .debounce(300)
                .onEach {
                    viewModel.setCreateCourseEvent(CreateCourseEvent.SetNameEvent(it.toString()))
                }.launchIn(lifecycleScope)

        binding.toggleButtonNbHoles.addOnButtonCheckedListener { _, checkedId, isChecked ->

            if (isChecked){
                if (checkedId == R.id.btn18Holes) {
                    if (viewModel.numberOfHole.value!! != 18) {
                        viewModel.setCreateCourseEvent(CreateCourseEvent.SetNumberOfHoleEvent(18))
                    }
                } else {
                    if (viewModel.numberOfHole.value!! != 9) {
                        viewModel.setCreateCourseEvent(CreateCourseEvent.SetNumberOfHoleEvent(9))
                    }
                }
            }
        }

        binding.btnValidateCourse.setOnClickListener { viewModel.setCreateCourseEvent(CreateCourseEvent.SaveCourseEvent) }

        return binding.root
    }


    @FlowPreview
    @ExperimentalCoroutinesApi
    private fun subscribeObservers() {

        viewModel.saveCourseState.observe(viewLifecycleOwner) { resource ->

            when (resource) {

                is Resource.Loading -> binding.progressCircular.visibility = View.VISIBLE

                is Resource.Failure -> {
                    binding.progressCircular.visibility = View.GONE
                    resource.exception.toast(requireContext())
                }

                is Resource.Success -> {
                    binding.progressCircular.visibility = View.GONE
                    navigateToHomeFragment()
                }
            }

        }

        viewModel.numberOfHole.observe(viewLifecycleOwner) { bindAllHoles() }


    }

    @ExperimentalCoroutinesApi
    @FlowPreview
    private fun bindAllHoles() {

        binding.createHolesLayout.removeAllViews()

        for (hole in viewModel.holes) {

            val item = HoleInputItem(requireContext(), hole, lifecycleScope) { holeNumber, par ->
                viewModel.setCreateCourseEvent(CreateCourseEvent.SetHoleEvent(holeNumber, par))
            }

            binding.createHolesLayout.addView(item)
        }

        binding.createHolesLayout.invalidate()
        binding.createHolesLayout.requestLayout()
    }


    private fun navigateToHomeFragment() {
        val action = CreateCourseFragmentDirections.actionCreateCourseFragmentToHomeFragment(null)
        findNavController().navigate(action)
    }
}