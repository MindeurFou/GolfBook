package com.example.golfbook.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.golfbook.data.model.Player
import com.example.golfbook.ui.game.GameIndividualPageFragment

class IndividualGameViewPagerAdapter(
    private val players: List<Player>,
    fm: FragmentManager,
    lifecycle: Lifecycle
): FragmentStateAdapter(fm, lifecycle) {


    override fun getItemCount(): Int = players.size

    override fun createFragment(position: Int): Fragment {
        return GameIndividualPageFragment(players[position])
    }
}