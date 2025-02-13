package com.campus.rent.mainActivity.fragments.publish.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.campus.rent.databinding.FragmentAddressDetailBinding
import com.campus.rent.mainActivity.fragments.publish.PublishActivity
import com.campus.rent.mainActivity.fragments.publish.PublishViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddressDetailFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentAddressDetailBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: PublishViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAddressDetailBinding.inflate(inflater, container, false)


        binding.confirm.setOnClickListener {
            (activity as PublishActivity).navigateToNext()
            dismiss()

        }
        getDataEditText()
        return binding.root
    }

    private fun getDataEditText() {
        sharedViewModel.address.observe(requireActivity()) {
            binding.etAddress.setText(it)
        }

        sharedViewModel.city.observe(requireActivity()) {
            binding.etCity.setText(it)
        }

        sharedViewModel.state.observe(requireActivity()) {
            binding.etState.setText(it)
        }

        sharedViewModel.country.observe(requireActivity()) {
            binding.etCountry.setText(it)
        }
        sharedViewModel.pinCode.observe(requireActivity()) {
            binding.etPinCode.setText(it)
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}