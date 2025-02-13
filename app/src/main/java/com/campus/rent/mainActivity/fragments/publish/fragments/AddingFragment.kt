package com.campus.rent.mainActivity.fragments.publish.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.campus.rent.database.model.property.Property
import com.campus.rent.database.model.property.PropertyRequest
import com.campus.rent.databinding.FragmentAddingBinding
import com.campus.rent.mainActivity.fragments.publish.PublishViewModel
import com.campus.rent.utils.Constants.TAG
import com.campus.rent.utils.NetworkResult
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class AddingFragment : Fragment() {

    private var _binding: FragmentAddingBinding? = null
    val binding get() = _binding!!
    lateinit var auth: FirebaseAuth
    private val sharedViewModel: PublishViewModel by activityViewModels()

    private lateinit var amenities: MutableList<String>
    private lateinit var images: MutableList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddingBinding.inflate(inflater, container, false)

        auth = FirebaseAuth.getInstance()
        amenities = mutableListOf()
        images = mutableListOf()

        getData()
        setUpButton()
        setUi()



        return binding.root
    }

    private fun setUi() {
        sharedViewModel.propertyResponseLiveData.observe(viewLifecycleOwner) {
            binding.loading.isVisible = false
            binding.add.isVisible = true


            when (it) {
                is NetworkResult.Success -> {
                    Log.d(TAG, "property is added")
                    activity?.finish()

                }

                is NetworkResult.Error -> {
                    Toast.makeText(requireActivity(), it.message, Toast.LENGTH_SHORT).show()
                }

                is NetworkResult.Loading -> {
                    binding.loading.isVisible = true
                    binding.add.isVisible = false
                }
            }

        }
    }

    private fun setUpButton() {
        binding.add.setOnClickListener {

            sharedViewModel.addProperty(
                PropertyRequest(
                    amenities.toList(), images.toList(), Property(
                        user_id = auth.currentUser?.uid.toString(),
                        title = sharedViewModel.name.value!!,
                        description = sharedViewModel.desc.value!!,
                        gender = sharedViewModel.gender.value!!,
                        listed_by = sharedViewModel.listedBy.value!!,
                        price = sharedViewModel.price.value?.toDouble() ?: 0.0,
                        security_amount = sharedViewModel.security.value?.toDouble() ?: 0.0,
                        address = sharedViewModel.address.value!!,
                        city = sharedViewModel.city.value!!,
                        state = sharedViewModel.state.value!!,
                        country = sharedViewModel.country.value!!,
                        pincode = sharedViewModel.pinCode.value!!,
                        latitude = sharedViewModel.latitude.value ?: 0.0,
                        longitude = sharedViewModel.longitude.value ?: 0.0,
                        property_type = sharedViewModel.propertyType.value!!,
                        bathroom = sharedViewModel.noOfBathroom.value?.toInt() ?: 0,
                        bedroom = sharedViewModel.noOfBedroom.value?.toInt() ?: 0

                    )
                )
            )
        }
    }

    private fun getData() {
        sharedViewModel.amenities.observe(viewLifecycleOwner) {
            amenities.clear()
            amenities.addAll(it)
        }
        sharedViewModel.photoList.observe(viewLifecycleOwner) {
           it.forEach { t->
               images.add(t.toString())
           }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}