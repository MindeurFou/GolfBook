package com.example.golfbook.ui.createCourse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.golfbook.databinding.FragmentCreateCourseBinding

class CreateCourseFragment : Fragment() {

    private lateinit var binding: FragmentCreateCourseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentCreateCourseBinding.inflate(inflater)





        return binding.root
    }
}