package com.nacho.restaurantapplication.presentation.fragment.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.databinding.FragmentHomeBinding
import com.nacho.restaurantapplication.presentation.activity.neworder.NewOrderActivity
import com.nacho.restaurantapplication.presentation.adapter.home.NewsAdapter
import com.nacho.restaurantapplication.presentation.adapter.neworder.DessertAdapter
import com.nacho.restaurantapplication.presentation.adapter.neworder.PromotionAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by activityViewModels()
    private val newOrderViewModel: NewOrderViewModel by activityViewModels()

    private lateinit var newsAdapter: NewsAdapter
    private lateinit var promotionList: PromotionAdapter
    private lateinit var dessertList: DessertAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        with(binding) {
            homeBtnGoNewOrder.setOnClickListener {
                goToNewOrder()
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
                    //delay(3000)
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

        newOrderViewModel.promotions.observe(viewLifecycleOwner) { promotions ->
            promotionList = PromotionAdapter(promotions) { /* Manejar click */ }
            binding.homeRvPromotions.adapter = promotionList
        }

        newOrderViewModel.desserts.observe(viewLifecycleOwner) { desserts ->
            val allDesserts = desserts.values.flatten()

            dessertList = DessertAdapter(allDesserts) { /* Manejar click */ }
            binding.homeRvDesserts.adapter = dessertList
        }

        newOrderViewModel.isLoading.observe(viewLifecycleOwner) { promotionLoading ->
            with(binding) {
                if (!promotionLoading) {
                    lifecycleScope.launch {
                        //delay(3000)
                        homeRvPromotions.visibility = View.VISIBLE
                        homeRvDesserts.visibility = View.VISIBLE
                        homePromotionsShimmer.visibility = View.GONE
                        homeDessertsShimmer.visibility = View.GONE
                    }
                } else {
                    homeRvPromotions.visibility = View.GONE
                    homeRvDesserts.visibility = View.GONE
                    homePromotionsShimmer.visibility = View.VISIBLE
                    homeDessertsShimmer.visibility = View.VISIBLE
                }
            }
        }

    }

    private fun goToNewOrder() {
        startActivity(Intent(context, NewOrderActivity::class.java))
        activity?.finish()
    }

}