package com.campus.rent.mainActivity.fragments.publish.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.campus.rent.R
import com.campus.rent.databinding.FragmentAmenitiesBinding
import com.campus.rent.mainActivity.fragments.publish.PublishActivity
import com.campus.rent.mainActivity.fragments.publish.PublishViewModel
import com.campus.rent.mainActivity.fragments.publish.adapter.GridAdapter
import com.campus.rent.utils.Constants.TAG
import kotlin.math.ceil

class AmenitiesFragment : Fragment() {

    private var _binding: FragmentAmenitiesBinding? = null
    private val binding get() = _binding!!
    private var items = listOf<Pair<String, Int>>()
    private val sharedViewModel: PublishViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAmenitiesBinding.inflate(inflater, container, false)

        sharedViewModel.amenities.observe(viewLifecycleOwner) { amenities ->
            Log.d(TAG, "Updated amenities: $amenities")
        }

        setUpButton()
        setUpGridView()
        setUpSwitch()


        return binding.root
    }

    private fun setUpSwitch() {


        binding.food.setOnClickListener {
            binding.switchFood.isChecked = !binding.switchFood.isChecked
        }

        binding.switchFood.setOnCheckedChangeListener { _, b ->
            if (b) {
                sharedViewModel.addAmenity("Food")
            } else {
                sharedViewModel.removeAmenity("Food")
            }
        }

        binding.wifi.setOnClickListener {
            binding.switchWifi.isChecked = !binding.switchWifi.isChecked
        }

        binding.switchWifi.setOnCheckedChangeListener { _, b ->
            if (b) {
                sharedViewModel.addAmenity("Wifi")
            } else {
                sharedViewModel.removeAmenity("Wifi")
            }
        }

        binding.laundry.setOnClickListener {
            binding.switchLaundry.isChecked = !binding.switchLaundry.isChecked
        }

        binding.switchLaundry.setOnCheckedChangeListener { _, b ->
            if (b) {
                sharedViewModel.addAmenity("LAUNDRY")
            } else {
                sharedViewModel.removeAmenity("LAUNDRY")
            }
        }

        binding.houseKeeping.setOnClickListener {
            binding.switchHouseKeeping.isChecked = !binding.switchHouseKeeping.isChecked
        }

        binding.switchHouseKeeping.setOnCheckedChangeListener { _, b ->
            if (b) {
                sharedViewModel.addAmenity("HouseKeeping")
            } else {
                sharedViewModel.removeAmenity("HouseKeeping")
            }
        }

        binding.hotWater.setOnClickListener {
            binding.switchHotWater.isChecked = !binding.switchHotWater.isChecked
        }

        binding.switchHotWater.setOnCheckedChangeListener { _, b ->
            if (b) {
                sharedViewModel.addAmenity("HotWater")
            } else {
                sharedViewModel.removeAmenity("HotWater")
            }
        }

    }

    private fun setUpGridView() {
        items = listOf(
            Pair("Bed", R.drawable.bed_single),
            Pair("Table", R.drawable.study_table),
            Pair("Mattress", R.drawable.mattress),
            Pair("Bedsheet", R.drawable.bedsheet),
            Pair("Pillow", R.drawable.pillow),
            Pair("Wardrobe", R.drawable.wardrobe),
            Pair("Curtain", R.drawable.curtains),
            Pair("Fridge", R.drawable.fridge),
            Pair("Ac", R.drawable.ac)
        )
        val adapter = GridAdapter(requireActivity(), items)
        binding.grid.adapter = adapter


        binding.grid.setOnItemClickListener { parent, view, position, id ->
            val item = items[position].first.uppercase()

            sharedViewModel.amenities.value?.let { amenities ->

                if (amenities.contains(item)) {
                    sharedViewModel.removeAmenity(item)
                    // id.isSelected = false // Deselect the view
                } else {
                    sharedViewModel.addAmenity(item)
                    // view.isSelected = true // Select the view
                }
            }


        }

        // Calculate the required height for the GridView based on the number of items
        val totalItems = items.size
        val columns = 3 // Number of columns
        val rows =
            ceil(totalItems.toDouble() / columns.toDouble()).toInt() // Calculate the number of rows

        val itemHeight =
            resources.getDimensionPixelSize(R.dimen.icon) // Set the height of each grid item

        val totalHeight = itemHeight * rows

        val layoutParams = binding.grid.layoutParams
        layoutParams.height = totalHeight
        binding.grid.layoutParams = layoutParams


    }

    private fun validate(): Boolean {
        sharedViewModel.amenities.observe(requireActivity()) { amenities ->
            if (amenities.isNotEmpty()) {
                binding.tvAmenitiesError.isVisible = false
            }
        }
        if (sharedViewModel.amenities.value!!.isEmpty()) {
            binding.tvAmenitiesError.isVisible = true
            return false
        }
        return true
    }

    private fun setUpButton() {
        binding.btnNextToPhotos.setOnClickListener {
            if (validate()) {
                (activity as PublishActivity).navigateToNext()
            } else {
                binding.scrollView2.smoothScrollTo(R.id.tv_amenities_error, R.id.tv_amenities_error)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}