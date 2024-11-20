package com.nacho.restaurantapplication.presentation.fragment.neworder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.fragment.DialogAddProductFragment
import com.nacho.restaurantapplication.data.model.Accompaniment
import com.nacho.restaurantapplication.data.model.Burger
import com.nacho.restaurantapplication.databinding.FragmentBurgersBinding
import com.nacho.restaurantapplication.presentation.adapter.neworder.BurgerAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BurgersFragment : Fragment() {

    private var _binding: FragmentBurgersBinding? = null
    private val binding get() = _binding!!

    private val newOrderVM: NewOrderViewModel by activityViewModels()

    private lateinit var meatList: BurgerAdapter
    private lateinit var chickenList: BurgerAdapter
    private lateinit var veganList: BurgerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBurgersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()

        with(binding) {
            burgersBtnAssemble.setOnClickListener {
                findNavController().navigate(R.id.action_newOrderFragment_to_assembleYourBurgerFragment)
            }
        }

    }


    private fun setupObservers() {

        newOrderVM.burgers.observe(viewLifecycleOwner) { burgers ->
            burgers?.let { categories ->
                meatList = BurgerAdapter(categories["Meat"] ?: emptyList()) { burger ->
                    showDialogAddProduct(burger)
                }
                chickenList = BurgerAdapter(categories["Chicken"] ?: emptyList()) { burger ->
                    showDialogAddProduct(burger)
                }
                veganList = BurgerAdapter(categories["Vegan"] ?: emptyList()) { burger ->
                    showDialogAddProduct(burger)
                }

                with(binding) {
                    burgersRvMeat.adapter = meatList
                    burgersRvChicken.adapter = chickenList
                    burgersRvVegan.adapter = veganList
                }
            }
        }

        newOrderVM.isLoading.observe(viewLifecycleOwner) { isLoading ->
            setupViews(isLoading)
        }

    }

    private fun setupViews(isLoading: Boolean) {
        with(binding) {
            val visibility = if (isLoading) View.GONE else View.VISIBLE

            val textViews = listOf(burgersTxtMeat, burgersTxtChicken, burgersTxtVegan)
            val recyclerViews = listOf(burgersRvMeat, burgersRvChicken, burgersRvVegan)

            textViews.forEach { it.visibility = visibility }
            recyclerViews.forEach { it.visibility = visibility }

            burgersShimmer.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun showDialogAddProduct(burger: Burger) {
        val dialog = DialogAddProductFragment.newInstance(
            productTitle = burger.title,
            productDescription = burger.description,
            productImageUrl = burger.image
        ) { /* Manejar el evento al hacer click en añadir al carrito */ }
        dialog.show(parentFragmentManager, DialogAddProductFragment::class.java.simpleName)
    }

}