package com.campus.rent.mainActivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.campus.rent.R
import com.campus.rent.databinding.ActivityMainBinding
import com.campus.rent.mainActivity.fragments.publish.PublishActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        setUpBottomNavigation()


    }

    private fun setUpBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)


        binding.bottomNavigationView.menu[2].setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.publish -> {
                    startActivity(Intent(this, PublishActivity::class.java))
                    true
                }

                else -> false

            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment, R.id.searchFragment, R.id.profileFragment, R.id.publish, R.id.favoriteFragment -> {
                    binding.bottomNavigationView.animate().translationY(0f).setDuration(300).start()
                }

                else -> {
                    binding.bottomNavigationView.animate().translationY(binding.bottomNavigationView.height.toFloat()).setDuration(300).start()
                }
            }
        }
    }
}