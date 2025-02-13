package com.campus.rent.mainActivity.fragments.publish.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.campus.rent.R
import com.campus.rent.databinding.FragmentExitBinding
import com.campus.rent.mainActivity.fragments.publish.PublishViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ExitFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentExitBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: PublishViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentExitBinding.inflate(inflater, container, false)

        binding.yes.setOnClickListener {
            activity?.finish()
        }

        binding.no.setOnClickListener {

            dismiss()

        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}