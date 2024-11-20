package com.nacho.restaurantapplication.presentation.fragment.neworder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.nacho.restaurantapplication.core.fragment.DialogAddProductFragment
import com.nacho.restaurantapplication.data.model.Accompaniment
import com.nacho.restaurantapplication.databinding.FragmentAccompanimentsBinding
import com.nacho.restaurantapplication.presentation.adapter.neworder.AccompanimentAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AccompanimentsFragment : Fragment() {

    private var _binding: FragmentAccompanimentsBinding? = null
    private val binding get() = _binding!!

    private val newOrderVM: NewOrderViewModel by activityViewModels()
    private lateinit var accompanimentList: AccompanimentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAccompanimentsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

    }

    private fun setupObservers() {

        newOrderVM.accompaniments.observe(viewLifecycleOwner) { accompaniments ->
            accompanimentList = AccompanimentAdapter(accompaniments) { accompaniment ->
                showDialogAddProduct(accompaniment)
            }
            binding.accompanimentsRv.adapter = accompanimentList
        }

        newOrderVM.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (!isLoading) {
                binding.accompanimentsRv.visibility = View.VISIBLE
                binding.accompanimentsShimmer.visibility = View.GONE
            }
        }

    }

    private fun showDialogAddProduct(accompaniment: Accompaniment) {
        val dialog = DialogAddProductFragment.newInstance(
            productTitle = accompaniment.title,
            productDescription = accompaniment.description,
            productImageUrl = accompaniment.image
        ) { /* Manejar el evento al hacer click en añadir al carrito */ }
        dialog.show(parentFragmentManager, DialogAddProductFragment::class.java.simpleName)
    }

}