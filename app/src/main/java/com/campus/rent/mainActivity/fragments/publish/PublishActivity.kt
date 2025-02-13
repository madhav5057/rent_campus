package com.campus.rent.mainActivity.fragments.publish

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.campus.rent.R
import com.campus.rent.databinding.ActivityPublishBinding
import com.campus.rent.mainActivity.fragments.publish.adapter.ViewPagerAdapter
import com.campus.rent.mainActivity.fragments.publish.fragments.AddingFragment
import com.campus.rent.mainActivity.fragments.publish.fragments.AddressFragment
import com.campus.rent.mainActivity.fragments.publish.fragments.AmenitiesFragment
import com.campus.rent.mainActivity.fragments.publish.fragments.DetailFragment
import com.campus.rent.mainActivity.fragments.publish.fragments.ExitFragment
import com.campus.rent.mainActivity.fragments.publish.fragments.PaymentFragment
import com.campus.rent.mainActivity.fragments.publish.fragments.PhotosFragment
import com.campus.rent.mainActivity.fragments.publish.fragments.PropertyTypeFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PublishActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityPublishBinding.inflate(layoutInflater)
    }
    private lateinit var publishViewModel: PublishViewModel
    val fragments = listOf(
        PropertyTypeFragment(),
        DetailFragment(),
        AddressFragment(),
        AmenitiesFragment(),
        PhotosFragment(),
        PaymentFragment(),
        AddingFragment()
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        publishViewModel = ViewModelProvider(this)[PublishViewModel::class.java]

        setUpToolBar()

        setUpViewPager()

    }


    private fun setUpViewPager() {
        val segments = listOf(
            findViewById(R.id.segment1),
            findViewById(R.id.segment2),
            findViewById(R.id.segment3),
            findViewById(R.id.segment4),
            findViewById(R.id.segment5),
            findViewById(R.id.segment6),
            findViewById<View>(R.id.segment7)
        )

        val adapter = ViewPagerAdapter(this, fragments)
        binding.viewPager.adapter = adapter

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                for (i in segments.indices) {
                    if (i <= position) {
                        segments[i].setBackgroundResource(R.drawable.blue_select)// Active color
                    } else {
                        segments[i].setBackgroundResource(R.drawable.gray_select) // Inactive color
                    }
                }

                binding.ca.visibility =
                    if (position == fragments.size - 1) View.GONE else View.VISIBLE

            }
        })
        binding.viewPager.isUserInputEnabled = false


    }


    fun navigateToNext() {
        val currentItem = binding.viewPager.currentItem
        if (currentItem < fragments.size - 1) {
            binding.viewPager.currentItem = currentItem + 1

        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.publish_toolbar_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }

            R.id.exit -> {
                showExitDialog()
                return true
            }

            else -> {
                return super.onOptionsItemSelected(item)

            }
        }
    }


    override fun onBackPressed() {

        if (binding.viewPager.currentItem == 6) {
            Toast.makeText(this, "you can not go back", Toast.LENGTH_SHORT).show()
        } else {
            if (binding.viewPager.currentItem > 0) {
                // Navigate to the previous page in ViewPager2
                binding.viewPager.currentItem -= 1
            } else {
                // Show confirmation dialog
                showExitDialog()
            }

        }
    }


    private fun setUpToolBar() {
        val toolbar = findViewById<Toolbar>(R.id.materialToolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = ""

    }

    private fun showExitDialog() {
        val bottomSheetFragment = ExitFragment()
        bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
    }

}