package com.nacho.restaurantapplication.presentation.activity.home

import android.content.Intent
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
import com.google.firebase.auth.FirebaseAuth
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.fragment.DialogAlertFragment
import com.nacho.restaurantapplication.databinding.ActivityHomeBinding
import com.nacho.restaurantapplication.presentation.activity.login.LoginActivity
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel

class HomeActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityHomeBinding
    private val homeVM: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupObservers()

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

        binding.homeLogout.setOnClickListener {
            showAlertDialog(
                title = getString(R.string.dialog_log_out),
                acceptButtonText = getString(R.string.dialog_out),
                onAcceptClick = { logout() }
            )
        }

    }

    private fun setupObservers() {
        homeVM.backInHome.observe(this) { navigateOnBack ->
            if (navigateOnBack) {
                showAlertDialog(
                    title = getString(R.string.dialog_quit_title),
                    acceptButtonText = getString(R.string.dialog_exit),
                    onAcceptClick = { finishAffinity() }
                )
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_home)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)

        finishAffinity()
    }

    private fun showAlertDialog(
        title: String,
        acceptButtonText: String,
        onAcceptClick: () -> Unit
    ) {
        val dialogFragment = DialogAlertFragment.newInstance(
            title = title,
            acceptButtonText = acceptButtonText,
            cancelButtonText = getString(R.string.cancel),
            onAcceptClick = onAcceptClick,
            onCancelClick = {}
        )
        dialogFragment.show(supportFragmentManager, "DialogAlertFragment")
    }

}