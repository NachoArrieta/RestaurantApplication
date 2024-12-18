package com.nacho.restaurantapplication.presentation.fragment.home

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.nacho.restaurantapplication.databinding.FragmentStoresBinding
import com.nacho.restaurantapplication.presentation.adapter.home.StoreAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoresFragment : Fragment() {
    private var _binding: FragmentStoresBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var storeAdapter: StoreAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.fetchStores()
        setupObservers()

    }

    private fun setupObservers() {

        viewModel.stores.observe(viewLifecycleOwner) { stores ->
            stores.let {
                storeAdapter = StoreAdapter(it!!)
                binding.storesRv.adapter = storeAdapter
            }
        }

        Handler(Looper.getMainLooper()).postDelayed({
            viewModel.loadingStores.observe(viewLifecycleOwner) { isLoading ->
                binding.apply {
                    storesRv.visibility = if (isLoading) View.GONE else View.VISIBLE
                    storesShimmer.visibility = if (isLoading) View.VISIBLE else View.GONE
                }
            }
        }, 3000)

    }

}