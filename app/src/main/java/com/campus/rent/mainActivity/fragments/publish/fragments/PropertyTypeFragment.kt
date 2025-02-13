package com.campus.rent.mainActivity.fragments.publish.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.campus.rent.R
import com.campus.rent.databinding.FragmentPropertyTypeBinding
import com.campus.rent.mainActivity.fragments.publish.PublishActivity
import com.campus.rent.mainActivity.fragments.publish.PublishViewModel

class PropertyTypeFragment : Fragment() {

    private var _binding: FragmentPropertyTypeBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: PublishViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentPropertyTypeBinding.inflate(inflater, container, false)


        binding.cardPg.setOnClickListener { selectPropertyType(binding.cardPg) }
        binding.cardHouse.setOnClickListener { selectPropertyType(binding.cardHouse) }
        binding.cardRoom.setOnClickListener { selectPropertyType(binding.cardRoom) }
        binding.cardFlat.setOnClickListener { selectPropertyType(binding.cardFlat) }

        validatePropertyType()
        setUpNextButton()

        return binding.root
    }

    private fun validatePropertyType() {
        sharedViewModel.propertyType.observe(requireActivity()) {
            if (it.equals("PG")) {
                binding.check1.visibility = View.VISIBLE
                binding.check2.visibility = View.GONE
                binding.check3.visibility = View.GONE
                binding.check4.visibility = View.GONE
            } else if (it.equals("FLAT")) {
                binding.check1.visibility = View.GONE
                binding.check2.visibility = View.VISIBLE
                binding.check3.visibility = View.GONE
                binding.check4.visibility = View.GONE
            } else if (it.equals("HOUSE")) {
                binding.check1.visibility = View.GONE
                binding.check2.visibility = View.GONE
                binding.check3.visibility = View.VISIBLE
                binding.check4.visibility = View.GONE
            } else if (it.equals("ROOM")) {
                binding.check1.visibility = View.GONE
                binding.check2.visibility = View.GONE
                binding.check3.visibility = View.GONE
                binding.check4.visibility = View.VISIBLE
            } else {
                binding.check1.visibility = View.GONE
                binding.check2.visibility = View.GONE
                binding.check3.visibility = View.GONE
                binding.check4.visibility = View.GONE
            }
        }
    }

    private fun selectPropertyType(selectedCard: CardView) {

        binding.cardPg.isSelected = false
        binding.cardFlat.isSelected = false
        binding.cardRoom.isSelected = false
        binding.cardHouse.isSelected = false

        // Highlight the selected card
        selectedCard.isSelected = true
        selectedCard.setBackgroundResource(R.drawable.property_type_selector)


        // Handle the selection (example: show a toast or save the selection)
        val type = when (selectedCard.id) {
            R.id.cardPg -> "PG"
            R.id.cardHouse -> "HOUSE"
            R.id.cardRoom -> "ROOM"
            R.id.cardFlat -> "FLAT"
            else -> ""
        }
        sharedViewModel.setPropertyType(type)

    }

    private fun setUpNextButton() {
        var ab = ""
        binding.nextToDetail.setOnClickListener {
            sharedViewModel.propertyType.observe(requireActivity()) {
                ab = it
            }
            if (ab.isEmpty()) {
                Toast.makeText(requireActivity(), "Select the property", Toast.LENGTH_SHORT).show()
            } else {
                (activity as PublishActivity).navigateToNext()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}