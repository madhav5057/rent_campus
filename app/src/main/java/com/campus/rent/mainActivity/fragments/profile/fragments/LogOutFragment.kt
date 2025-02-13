package com.campus.rent.mainActivity.fragments.profile.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.campus.rent.authentication.AuthActivity
import com.campus.rent.databinding.FragmentLogOutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth


class LogOutFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentLogOutBinding? = null
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogOutBinding.inflate(inflater, container, false)


        auth = FirebaseAuth.getInstance()

        binding.logout.setOnClickListener {
            auth.signOut()
            startActivity(Intent(requireActivity(), AuthActivity::class.java))
        }

        binding.cancel.setOnClickListener {
            dismiss()
        }



        return binding.root

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}