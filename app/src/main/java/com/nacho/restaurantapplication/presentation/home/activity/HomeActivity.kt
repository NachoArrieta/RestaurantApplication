package com.nacho.restaurantapplication.presentation.home.activity

import android.os.Bundle
import androidx.activity.addCallback
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.fragment.DialogAlertFragment
import com.nacho.restaurantapplication.databinding.ActivityHomeBinding
import com.nacho.restaurantapplication.presentation.home.viewmodel.HomeViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private val homeVM: HomeViewModel by viewModels()

    private fun observers() {
        homeVM.backInHome.observe(this) { navigateOnBack ->
            if (navigateOnBack) {
                showAlertDialog()
                homeVM.handleBackNavigation(false)
            }
        }

        homeVM.drawerOpen.observe(this) { drawerOpen ->
            if (drawerOpen) {
                binding.drawerLayout.closeDrawer(GravityCompat.START)
                homeVM.setDrawerOpen(false)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observers()

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.appBarHome.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_home)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_my_profile,
                R.id.nav_my_coupons,
                R.id.nav_my_reservations,
                R.id.nav_my_orders,
                R.id.nav_my_payment_methods,
                R.id.nav_stores
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        onBackPressedDispatcher.addCallback(this) {
            val currentDestinationId = navController.currentDestination?.id
            if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                homeVM.setDrawerOpen(true)
            } else if (currentDestinationId == R.id.nav_home) {
                homeVM.handleBackNavigation(true)
            } else {
                navController.popBackStack()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun showAlertDialog() {
        val dialogFragment = DialogAlertFragment()
        dialogFragment.show(supportFragmentManager, "DialogAlertFragment")
    }

}