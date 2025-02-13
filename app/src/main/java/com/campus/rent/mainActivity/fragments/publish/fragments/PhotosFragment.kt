package com.campus.rent.mainActivity.fragments.publish.fragments

import PhotosAdapter
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.campus.rent.databinding.FragmentPhotosBinding
import com.campus.rent.mainActivity.fragments.publish.PublishActivity
import com.campus.rent.mainActivity.fragments.publish.PublishViewModel
import com.campus.rent.utils.Constants.TAG
import kotlin.math.log


class PhotosFragment : Fragment() {

    private lateinit var list: MutableList<Uri>
    private var _binding: FragmentPhotosBinding? = null
    private val binding get() = _binding!!
    private val sharedViewModel: PublishViewModel by activityViewModels()
    private lateinit var adapter: PhotosAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPhotosBinding.inflate(inflater, container, false)

        list = mutableListOf()

        sharedViewModel.photoList.observe(requireActivity()) {
            Log.d(TAG, "Shared list $it")
        }


        settUpButton()



        adapter = PhotosAdapter(requireContext(), list)
        binding.rvPhotos.layoutManager =
            LinearLayoutManager(requireActivity(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvPhotos.adapter = adapter






        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private val pickImage =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            if (uri != null) {
                list.add(uri)
                adapter . notifyDataSetChanged ()
                Log.d(TAG, "$list: ")
            } else {

                Log.d(TAG, "No media selected")

            }

        }

    private fun openImagePicker() {
        if (list.size >= 5) {
            Toast.makeText(requireActivity(), "only 5 images can be selected", Toast.LENGTH_SHORT)
                .show()
        } else {
            pickImage.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
        }
    }

    private fun validate(): Boolean {

        if (list.isEmpty()) {
            Toast.makeText(requireActivity(), "Add images", Toast.LENGTH_SHORT).show()
            return false
        }

        if (list.size < 5) {
            Toast.makeText(requireActivity(), "At least 5 images are needed", Toast.LENGTH_SHORT)
                .show()
            return false
        }

        return true
    }

    private fun settUpButton() {
        binding.addImage.setOnClickListener {
            openImagePicker()
        }

        binding.btnNextToPayment.setOnClickListener {
            if (validate()) {
                sharedViewModel.addPhoto(list)
                (activity as PublishActivity).navigateToNext()
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}