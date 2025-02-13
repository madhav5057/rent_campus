package com.campus.rent.mainActivity.fragments.publish.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.RadioButton
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.campus.rent.R
import com.campus.rent.databinding.FragmentDetailBinding
import com.campus.rent.mainActivity.fragments.publish.PublishActivity
import com.campus.rent.mainActivity.fragments.publish.PublishViewModel

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: PublishViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        textWatcher()
        setUpNoOfRooms()
        validateBath()
        setUpAction()
        setUpButtonGroup()
        setUpButton()

        binding.cardMale.setOnClickListener { selectGender(binding.cardMale) }
        binding.cardFemale.setOnClickListener { selectGender(binding.cardFemale) }
        binding.cardOther.setOnClickListener { selectGender(binding.cardOther) }

        return binding.root

    }


    private fun setUpButtonGroup() {
        binding.buttonGroup.setOnCheckedChangeListener { _, checkedId ->
            val selectedButton = view?.findViewById<RadioButton>(checkedId)
            val selectedText = selectedButton?.text.toString()
            sharedViewModel.setListedBy(selectedText)

        }
    }


    private fun validateBath() {
        sharedViewModel.propertyType.observe(requireActivity()) {
            when (it) {
                "PG" -> binding.badBathView.visibility = View.GONE
                "ROOM" -> binding.badBathView.visibility = View.GONE
                "FLAT" -> binding.badBathView.visibility = View.VISIBLE
                else -> binding.badBathView.visibility = View.VISIBLE

            }
        }
    }

    private fun selectGender(selectedCard: CardView) {

        binding.cardMale.isSelected = false
        binding.cardFemale.isSelected = false
        binding.cardOther.isSelected = false

        // Highlight the selected card
        selectedCard.isSelected = true
        selectedCard.setBackgroundResource(R.drawable.card_selector)


        // Handle the selection (example: show a toast or save the selection)
        val gender = when (selectedCard.id) {
            R.id.cardMale -> "BOYS"
            R.id.cardFemale -> "GIRLS"
            R.id.cardOther -> "BOTH"
            else -> ""
        }
        sharedViewModel.setGender(gender)
    }

    @SuppressLint("SetTextI18n")
    private fun setUpNoOfRooms() {
        binding.addBedroom.setOnClickListener {
            sharedViewModel.incrementBedroom()
        }
        binding.addBathroom.setOnClickListener {
            sharedViewModel.incrementBathroom()
        }

        binding.removeBedroom.setOnClickListener {
            sharedViewModel.decrementBedroom()
        }
        binding.removeBathroom.setOnClickListener {
            sharedViewModel.decrementBathroom()
        }

        sharedViewModel.noOfBedroom.observe(requireActivity()) {
            binding.bedroom.text = it.toString()
        }
        sharedViewModel.noOfBathroom.observe(requireActivity()) {
            binding.bathroom.text = it.toString()
        }
    }


    private fun textWatcher() {
        binding.etName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //Nothing for it
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val charCount = p0?.length ?: 0

                if (charCount > 0) {
                    binding.tvNameError.isVisible = false
                    binding.etName.setBackgroundResource(R.drawable.et_back)

                }

                binding.tvTitleCount.text = "$charCount/100"

                if (charCount > 100) {
                    binding.etName.setBackgroundResource(R.drawable.et_back_red)
                } else {
                    binding.etName.setBackgroundResource(R.drawable.et_back)
                }

            }

            override fun afterTextChanged(p0: Editable?) {
                sharedViewModel.setName(p0.toString())
            }
        })

        binding.etDesc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                //nothing here
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val charCount = p0?.length ?: 0

                if (charCount > 0) {
                    binding.tvDescError.isVisible = false
                    binding.etDesc.setBackgroundResource(R.drawable.et_back)

                }

                binding.tvDescCount.text = "$charCount/5000"

                if (charCount > 5000) {
                    binding.etName.setBackgroundResource(R.drawable.et_back_red)
                } else {
                    binding.etName.setBackgroundResource(R.drawable.et_back)
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                sharedViewModel.setDesc(p0.toString())
            }
        })
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setUpAction() {
        binding.etName.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                // Move focus to the next EditText
                binding.etDesc.requestFocus()
                true
            } else {
                false
            }
        }
    }

    private fun validate(): Boolean {

        if (binding.etName.text.isEmpty()) {
            binding.tvNameError.visibility = View.VISIBLE
            binding.etName.setBackgroundResource(R.drawable.et_back_red)
            return false
        }

        if (binding.etDesc.text.isEmpty()) {
            binding.tvDescError.visibility = View.VISIBLE
            binding.etDesc.setBackgroundResource(R.drawable.et_back_red)
            return false
        }
        if (sharedViewModel.gender.value.isNullOrEmpty()) {
            Toast.makeText(requireActivity(), "Select gender", Toast.LENGTH_SHORT).show()
            return false
        }
        if (sharedViewModel.listedBy.value.isNullOrEmpty()) {
            Toast.makeText(requireActivity(), "Select listed by", Toast.LENGTH_SHORT).show()
            return false
        }
        if (sharedViewModel.propertyType.value == "FLAT" || sharedViewModel.propertyType.value == "HOUSE") {
            if (sharedViewModel.noOfBedroom.value == 0 || sharedViewModel.noOfBathroom.value == 0) {
                Toast.makeText(requireActivity(), "Add no of bathroom", Toast.LENGTH_SHORT).show()
                return false
            }
        }
        return true
    }

    private fun setUpButton() {
        binding.btnNextToAddress.setOnClickListener {
            if (validate()) {
                (activity as PublishActivity).navigateToNext()
            }
        }

    }


}