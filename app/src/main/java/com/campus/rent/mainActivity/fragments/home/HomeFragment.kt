package com.campus.rent.mainActivity.fragments.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.campus.rent.databinding.FragmentHomeBinding
import com.campus.rent.mainActivity.fragments.home.adapter.PropertyPagerAdapter
import com.campus.rent.search.SearchActivity
import com.campus.rent.utils.Constants.TAG
import com.campus.rent.utils.LocationHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    lateinit var adapter: PropertyPagerAdapter
    private val homeViewModel: HomeViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        binding.search.setOnClickListener {
            startActivity(Intent(requireActivity(), SearchActivity::class.java))
        }

        setUpSearch()

        setUpRv()


        return binding.root

    }

    private fun setUpRv() {
        adapter = PropertyPagerAdapter(requireContext())
        val layoutManager = LinearLayoutManager(requireActivity())

        binding.propertyRv.layoutManager = layoutManager
        /*   binding.propertyRv.setHasFixedSize(false)
           binding.propertyRv.isNestedScrollingEnabled=false
           binding.propertyRv.isVerticalScrollBarEnabled=true*/
        binding.propertyRv.adapter = adapter

        val location = LocationHelper.getSavedLocation(requireContext())

        if (location != null) {
            homeViewModel.updateLocation(location.first,location.second)
        }

        homeViewModel.list.observe(viewLifecycleOwner) { pagingData ->
            if (pagingData != null) {
                adapter.submitData(lifecycle, pagingData)
            } else {
                Log.d(TAG, "Data is null")
            }
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    private fun setUpSearch() {
        binding.search.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val drawableEnd = binding.search.compoundDrawablesRelative[2] // Right drawable

                // Handle end drawable click
                if (drawableEnd != null) {
                    val drawableWidth = drawableEnd.bounds.width()
                    val drawableStartX =
                        binding.search.width - binding.search.paddingEnd - drawableWidth
                    if (event.x >= drawableStartX) {
                        // Drawable end clicked
                        startActivity(Intent(requireActivity(), SearchActivity::class.java))
                        return@setOnTouchListener true
                    }
                }

            }
            false
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}