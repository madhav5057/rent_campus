package com.campus.rent.mainActivity.fragments.profile

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.campus.rent.R
import com.campus.rent.databinding.FragmentProfileBinding
import com.campus.rent.mainActivity.fragments.profile.fragments.LogOutFragment

class ProfileFragment : Fragment() {

    private val viewModel: ProfileViewModel by viewModels()
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)


        setUpButton()



        return binding.root
    }

    private fun setUpButton() {
        binding.logout.setOnClickListener {

            val bottomSheetFragment = LogOutFragment()
            bottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                bottomSheetFragment.tag
            )
        }

        binding.myListings.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_listingsFragment)
        }

        binding.saved.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_savedFragment)
        }

        binding.notification.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_notificationFragment)
        }

        binding.language.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_languageFragment)
        }

        binding.saved.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_savedFragment)
        }

        binding.profile.setOnClickListener {
            findNavController().navigate(R.id.action_profileFragment_to_myProfileFragment)
        }

        binding.more.setOnClickListener {
            setUpMode()
        }
    }

    private fun setUpMode() {
        val sharedPreferences = requireActivity().getSharedPreferences("ThemePrefs", MODE_PRIVATE)
        val isDarkMode = sharedPreferences.getBoolean("isDarkMode", false)

        if (isDarkMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.darkMode.isChecked = isDarkMode
        binding.darkMode.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

            // Save the theme preference
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
            editor.putBoolean("isDarkMode", isChecked)
            editor.apply()

        }


    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onStart() {
        setUpMode()
        super.onStart()
    }
}