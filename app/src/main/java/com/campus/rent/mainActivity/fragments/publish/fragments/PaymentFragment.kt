package com.campus.rent.mainActivity.fragments.publish.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.campus.rent.R
import com.campus.rent.databinding.FragmentPaymentBinding
import com.campus.rent.mainActivity.fragments.publish.PublishActivity
import com.campus.rent.mainActivity.fragments.publish.PublishViewModel
import com.campus.rent.utils.Constants.TAG

class PaymentFragment : Fragment() {

    private var _binding: FragmentPaymentBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: PublishViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater, container, false)

        textWatcher()
        setUpButton()

        return binding.root
    }


    private fun textWatcher() {
        binding.etPrice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Nothing for it
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val charCount = p0?.length ?: 0

                if (charCount > 0) {
                    binding.tvPriceError.isVisible = false
                    binding.etPrice.setBackgroundResource(R.drawable.et_back)

                }

                if (charCount > 100) {
                    binding.etPrice.setBackgroundResource(R.drawable.et_back_red)
                } else {
                    binding.etPrice.setBackgroundResource(R.drawable.et_back)
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                sharedViewModel.setPrice(p0.toString())
            }
        })

        binding.etSecurity.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //nothing here
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val charCount = p0?.length ?: 0

                if (charCount > 0) {
                    binding.tvSecurityError.isVisible = false
                    binding.etSecurity.setBackgroundResource(R.drawable.et_back)

                }


                if (charCount > 5000) {
                    binding.etSecurity.setBackgroundResource(R.drawable.et_back_red)
                } else {
                    binding.etSecurity.setBackgroundResource(R.drawable.et_back)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                sharedViewModel.setSecurity(p0.toString())
            }
        })
    }

    private fun validate(): Boolean {

        if (binding.etPrice.text.isNullOrEmpty()) {
            binding.tvPriceError.isVisible = true
            binding.etPrice.setBackgroundResource(R.drawable.et_back_red)
            return false
        }

        if (binding.etSecurity.text.isNullOrEmpty()) {
            binding.tvSecurityError.isVisible = true
            binding.etSecurity.setBackgroundResource(R.drawable.et_back_red)
            return false
        }
        return true
    }

    private fun setUpButton() {
        binding.nextToAdding.setOnClickListener {
            if (validate()){
                (activity as PublishActivity).navigateToNext()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}