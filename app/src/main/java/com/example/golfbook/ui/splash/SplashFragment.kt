package com.example.golfbook.ui.splash

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.golfbook.R
import com.example.golfbook.data.repository.PlayerRepository
import com.example.golfbook.ui.ActivityViewModel
import com.example.golfbook.ui.chooseAvatar.ChooseAvatarViewModel
import com.example.golfbook.utils.Resource
import com.google.android.material.imageview.ShapeableImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {

    private val mainViewModel: ActivityViewModel by activityViewModels()

    private val playerRepo = PlayerRepository

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_splash, container, false)

        val topAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.top_logo_anim)
        val bottomAnim = AnimationUtils.loadAnimation(requireContext(), R.anim.bottom_splash_title_anim)

        view.findViewById<ShapeableImageView>(R.id.appIcon).startAnimation(topAnim)
        view.findViewById<TextView>(R.id.app_name).startAnimation(bottomAnim)


        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)

        val playerId = sharedPref?.getString(requireContext().getString(R.string.player_id_key), null)


        CoroutineScope(Dispatchers.Main).launch {

            delay(2000)

            if (playerId != null) {

                playerRepo.getPlayer(playerId).onEach { resource ->

                    if (resource is Resource.Success) {
                        mainViewModel.currentPlayer = resource.data
                        navigateToChooseAvatarFragment(ChooseAvatarViewModel.Companion.chooseAvatarAction.UPDATE_MAIN_PLAYER)
                    }

                    else if (resource is Resource.Failure) {

                        sharedPref.edit().remove(requireContext().getString(R.string.player_id_key)).apply()

                        navigateToChooseAvatarFragment(ChooseAvatarViewModel.Companion.chooseAvatarAction.CREATE_MAIN_PLAYER)
                    }
                }.launchIn(CoroutineScope(Dispatchers.Main))

            } else {
                navigateToChooseAvatarFragment(ChooseAvatarViewModel.Companion.chooseAvatarAction.CREATE_MAIN_PLAYER)
            }

        }

        return view
    }


    private fun navigateToChooseAvatarFragment(action: ChooseAvatarViewModel.Companion.chooseAvatarAction) {

        val navigationAction = SplashFragmentDirections.actionSplashFragmentToChooseAvatarFragment(
                chooseAvatarAction = action
        )
        findNavController().navigate(navigationAction)
    }

}