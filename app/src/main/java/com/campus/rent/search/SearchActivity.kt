package com.campus.rent.search

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.campus.rent.R
import com.campus.rent.databinding.ActivitySearchBinding
import com.campus.rent.search.models.PlacesAdapter
import com.campus.rent.search.models.PlacesModel
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient

class SearchActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivitySearchBinding.inflate(layoutInflater)
    }
    private lateinit var placesClient: PlacesClient
    private lateinit var adapter: PlacesAdapter
    private var predictions = mutableListOf<PlacesModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setUpToolBar()
        setUpSearchEditText()

        // Initialize Places API
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, "AIzaSyAvjNKcsNzP-sYJqYvEVR7l1PWFNw2hDhA")
        }
        placesClient = Places.createClient(this)
        binding.locationRv.layoutManager = LinearLayoutManager(this)
        adapter = PlacesAdapter(predictions) { place ->
            onPlaceSelected(place)
        }
        binding.locationRv.adapter = adapter

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.toString().isNotEmpty()) {
                    fetchPlacePredictions(p0.toString())
                } else {
                    binding.locationRv.visibility = View.GONE
                }
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


    }

    private fun onPlaceSelected(place: PlacesModel) {
        Toast.makeText(this, place.placeName, Toast.LENGTH_SHORT).show()
    }

    private fun fetchPlacePredictions(query: String) {
        val token = AutocompleteSessionToken.newInstance()
        val request =
            FindAutocompletePredictionsRequest.builder().setSessionToken(token).setQuery(query)
                .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            predictions.clear()
            for (prediction in response.autocompletePredictions) {
                predictions.add(
                    PlacesModel(
                        prediction.placeId,
                        prediction.getPrimaryText(null).toString(),
                        prediction.getFullText(null).toString()
                    )
                )
            }
            adapter.updateData(predictions)
            binding.locationRv.isVisible = predictions.isEmpty()
        }.addOnFailureListener {
            Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setUpSearchEditText() {
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                binding.etSearch.clearFocus()
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
                true
            } else {
                false
            }
        }

    }

    private fun setUpToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = ""

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)

            }
        }
    }
}
