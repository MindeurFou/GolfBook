package com.example.golfbook.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.golfbook.R
import com.example.golfbook.data.model.Player
import com.example.golfbook.ui.adapters.IndividualGameViewPagerAdapter

class GameViewPagerFragment : Fragment() {

    private lateinit var viewPager: ViewPager2

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_view_pager, container, false)

        viewPager = view.findViewById(R.id.viewPager)

        val players = listOf<Player>()

        val pagerAdapter = IndividualGameViewPagerAdapter(
            players,
            requireActivity().supportFragmentManager,
            lifecycle
        )

        viewPager.adapter = pagerAdapter

        return view
    }

}