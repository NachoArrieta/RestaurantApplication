package com.nacho.restaurantapplication.presentation.fragment.home

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.databinding.FragmentHomeBinding
import com.nacho.restaurantapplication.presentation.activity.neworder.NewOrderActivity
import com.nacho.restaurantapplication.presentation.adapter.home.NewsAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchNews()
        setupObservers()

        with(binding) {
            homeBtnGoNewOrder.setOnClickListener {
                goToNewOrder()
            }
        }

    }

    private fun setupObservers() {
        viewModel.news.observe(viewLifecycleOwner) { news ->
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

        viewModel.loadingNews.observe(viewLifecycleOwner) { isLoading ->
            binding.apply {
                homeVp.visibility = if (isLoading) View.INVISIBLE else View.VISIBLE
                homeNewsShimmer.visibility = if (isLoading) View.VISIBLE else View.GONE
            }
        }
    }

    private fun goToNewOrder() {
        startActivity(Intent(context, NewOrderActivity::class.java))
        activity?.finish()
    }

}