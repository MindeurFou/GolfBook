package com.example.golfbook.ui.loungeDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
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
import com.example.golfbook.ui.compoundedComponents.PlayerPreviewItemSize
import com.example.golfbook.utils.Resource
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.random.Random
import kotlin.text.StringBuilder

class LoungeDetailsFragment : Fragment() {

    private lateinit var binding: FragmentLoungeDetailsBinding

    private val args: LoungeDetailsFragmentArgs by navArgs()

    private val mainViewModel: ActivityViewModel by activityViewModels()

    private lateinit var viewModelFactory: LoungeDetailsViewModelFactory

    private val viewModel: LoungeDetailsViewModel by viewModels(
            factoryProducer =  { viewModelFactory }
    )

    private val listPlayersReady: MutableList<Pair<String,String>> = mutableListOf()
    private var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {

                val state = (viewModel.lounge.value!! as Resource.Success).data.state

                state?.let {
                    if (it == "available")
                        viewModel.leaveLounge()
                }

            }
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        binding = FragmentLoungeDetailsBinding.inflate(inflater)
        viewModelFactory = LoungeDetailsViewModelFactory(args, mainViewModel.localPlayer!!)

        val adapter = ArrayAdapter(requireContext(), R.layout.list_courses, listOf<String>())
        binding.editTextCourse.setAdapter(adapter)

        subscribeObservers()

        binding.editTextCourse.setOnItemClickListener { parent, view, position, id ->

            val courseName = parent.getItemAtPosition(position).toString()

            viewModel.updateCourseName(courseName)
        }


        binding.btnLeave.setOnClickListener { viewModel.leaveLounge() }

        binding.btnStart.setOnClickListener {
            try {
                viewModel.startGame()
            } catch (e: Exception) {
                e.toast(requireContext())
            }
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

                    val item = PlayerPreviewItem(requireContext(), player, PlayerPreviewItemSize.BIG)
                    binding.playersContainer.addView(item)

                }

                binding.playersContainer.invalidate()
                binding.playersContainer.requestLayout()


                if (!lounge.courseName.isNullOrBlank()){
                    binding.editTextCourse.setText(lounge.courseName, false)
                }

                lounge.state?.let { state ->


                    when(state) {

                        "starting" -> {

                            binding.btnStart.visibility = View.GONE
                            binding.btnLeave.visibility = View.GONE

                            if (!viewModel.localPlayerIsAdmin) {

                                dialog = MaterialAlertDialogBuilder(requireContext())
                                        .setTitle(resources.getString(R.string.starting_dialog_title))
                                        .setMessage(resources.getString(R.string.starting_dialog_message))
                                        .setNegativeButton(resources.getString(R.string.starting_dialog_decline)) { _, _ ->
                                            viewModel.refuseStart()
                                        }
                                        .setPositiveButton(resources.getString(R.string.starting_dialog_accept)) { _, _ ->
                                            viewModel.acceptStart()
                                        }
                                        .setCancelable(false)
                                        .show()
                            }




                        }

                        "available" -> {

                            dialog?.dismiss()
                            dialog = null

                            binding.btnStart.visibility = View.VISIBLE
                            binding.btnLeave.visibility = View.VISIBLE
                        }

                        "busy" -> {}

                        else -> Toast.makeText(requireContext(), "État du salon inconnu : $state", Toast.LENGTH_LONG).show()
                    }


                }
            }

        }

        viewModel.loungeDetails.observe(viewLifecycleOwner) { resource ->

            if (resource is Resource.Failure)
                resource.exception.toast(requireContext())
            else if (resource is Resource.Success) {

                val playerReady = resource.data.playersReady
                playerReady?.let {
                    updatePlayersReadyText(it)

                    if (viewModel.localPlayerIsAdmin)
                        viewModel.maybeLaunchGame()
                }



            }
        }

        viewModel.leaveLoungeState.observe(viewLifecycleOwner) { resource ->
            if (resource is Resource.Failure)
                resource.exception.toast(requireContext())
            else if (resource is Resource.Success) {
                navigateToHomeFragment()
            }
        }

        viewModel.launchGameState.observe(viewLifecycleOwner) {
            navigateToGameFragment(viewModel.localPlayerIsAdmin, it) //loungeId
        }


    }

    private fun updatePlayersReadyText(playersReady: List<String>) {

        if (playersReady.isNotEmpty()) {

            playersReady.forEach { name ->

                if (name.isNotBlank()) {
                    if (listPlayersReady.all { it.first != name }) {

                        val textArray: Array<String> = resources.getStringArray(R.array.playerReadyArray)
                        val index = Random.nextInt(textArray.size)
                        val text = textArray[index]

                        listPlayersReady.add(Pair(name, text))
                    }
                }
            }

            val sb = StringBuilder()

            listPlayersReady.forEachIndexed { index, pair ->
                sb.append(pair.first).append(" ${pair.second}")
                if (index != listPlayersReady.size -1)
                    sb.append("\n")
            }

            binding.playersReadyTextView.text = sb.toString()
        } else {
            binding.playersReadyTextView.text = null
        }


    }

    private fun navigateToHomeFragment() {
        val action = LoungeDetailsFragmentDirections.actionLoungeDetailsFragmentToHomeFragment(null)
        findNavController().navigate(action)
    }

    private fun navigateToGameFragment(localPlayerIsAdmin: Boolean, loungeId: String) {
        val action = LoungeDetailsFragmentDirections.actionLoungeDetailsFragmentToGameViewPagerFragment(loungeId, localPlayerIsAdmin)
        findNavController().navigate(action)
    }

}