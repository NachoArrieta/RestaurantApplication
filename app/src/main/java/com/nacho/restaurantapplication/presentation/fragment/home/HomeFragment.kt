package com.nacho.restaurantapplication.presentation.fragment.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.databinding.FragmentHomeBinding
import com.nacho.restaurantapplication.presentation.activity.neworder.NewOrderActivity
import com.nacho.restaurantapplication.presentation.adapter.home.HomeReservationAdapter
import com.nacho.restaurantapplication.presentation.adapter.home.NewsAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels()

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var reservationAdapter: HomeReservationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel.fetchNews()

        val user = FirebaseAuth.getInstance().currentUser?.uid
        if (user != null) {
            homeViewModel.fetchUserReservations(user)
        }

        setupObservers()

        with(binding) {
            homeBtnGoNewOrder.setOnClickListener {
                goToNewOrder()
            }

            homeBtnInstagram.setOnClickListener {
                openInstagramProfile()
            }

            homeBtnWhatsapp.setOnClickListener {
                openWhatsApp("+543571539872")
            }

            homeBtnCall.setOnClickListener {
                openCall("3571414900")
            }
        }

    }

    private fun setupObservers() {
        homeViewModel.news.observe(viewLifecycleOwner) { news ->
            news?.let {
                newsAdapter = NewsAdapter(it) { selectedNews ->
                    val action = HomeFragmentDirections.actionNavHomeToNewsFragment(
                        title = selectedNews.title,
                        description = selectedNews.description,
                        image = selectedNews.image
                    )
                    findNavController().navigate(action)
                }
                binding.homeVp.adapter = newsAdapter
            }
        }

        homeViewModel.loadingNews.observe(viewLifecycleOwner) { isLoading ->
            if (!isLoading) {
                lifecycleScope.launch {
                    delay(2000)
                    binding.apply {
                        homeVp.visibility = View.VISIBLE
                        homeNewsShimmer.visibility = View.GONE
                    }
                }
            } else {
                binding.apply {
                    homeVp.visibility = View.INVISIBLE
                    homeNewsShimmer.visibility = View.VISIBLE
                }
            }
        }

        homeViewModel.userReservations.observe(viewLifecycleOwner) { reservations ->

            val sortedReservations = reservations.sortedWith(compareBy(
                { LocalDate.parse(it.day, DateTimeFormatter.ofPattern("d/MM/yyyy")) },
                { LocalTime.parse(it.hour, DateTimeFormatter.ofPattern("H.mm 'hs'")) }
            ))

            binding.apply {
                homeReservationShimmer.visibility = View.VISIBLE

                if (reservations.isNullOrEmpty()) {
                    lifecycleScope.launch {
                        delay(2000)
                        homeTxtReservation.visibility = View.VISIBLE
                        homeRvReservation.visibility = View.INVISIBLE
                        homeCvEmptyReservation.visibility = View.VISIBLE
                        homeReservationShimmer.visibility = View.GONE
                    }
                } else {

                    reservationAdapter = HomeReservationAdapter(reservationList = sortedReservations)
                    homeRvReservation.adapter = reservationAdapter
                    lifecycleScope.launch {
                        delay(2000)
                        homeTxtReservation.visibility = View.VISIBLE
                        homeCvEmptyReservation.visibility = View.GONE
                        homeRvReservation.visibility = View.VISIBLE
                        homeReservationShimmer.visibility = View.GONE
                    }
                }

            }
        }

        lifecycleScope.launch {
            delay(2500)
            binding.apply {
                homeBtnShimmer.visibility = View.GONE
                homeBtnGoNewOrder.visibility = View.VISIBLE
                homeBtnInstagram.visibility = View.VISIBLE
                homeBtnCall.visibility = View.VISIBLE
                homeBtnWhatsapp.visibility = View.VISIBLE
            }
        }

    }

    private fun goToNewOrder() {
        startActivity(Intent(context, NewOrderActivity::class.java))
        activity?.finish()
    }

    private fun openInstagramProfile() {
        val username = "beicon.bsb"
        val appUri = Uri.parse("http://instagram.com/_u/$username")
        val webUri = Uri.parse("https://www.instagram.com/$username")

        val instagramAppIntent = Intent(Intent.ACTION_VIEW, appUri).apply {
            setPackage("com.instagram.android")
        }

        val packageManager = requireContext().packageManager
        if (instagramAppIntent.resolveActivity(packageManager) != null) {
            startActivity(instagramAppIntent)
        } else {
            val webIntent = Intent(Intent.ACTION_VIEW, webUri)
            startActivity(webIntent)
        }
    }

    private fun openCall(phoneNumber: String) {
        val dialIntent = Intent(Intent.ACTION_DIAL).apply {
            data = Uri.parse("tel:$phoneNumber")
        }
        startActivity(dialIntent)
    }

    private fun openWhatsApp(phoneNumber: String) {
        val uri = Uri.parse("https://wa.me/$phoneNumber")
        val intent = Intent(Intent.ACTION_VIEW, uri).apply {
            setPackage("com.whatsapp")
        }

        if (intent.resolveActivity(requireContext().packageManager) != null) {
            startActivity(intent)
        } else {
            showToast(getString(R.string.home_no_wpp))
        }
    }

    private fun showToast(message: String) = Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

}



