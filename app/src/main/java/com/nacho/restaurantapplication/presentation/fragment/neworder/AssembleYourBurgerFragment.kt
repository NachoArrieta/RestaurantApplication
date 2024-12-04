package com.nacho.restaurantapplication.presentation.fragment.neworder

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.data.model.BurgerSize
import com.nacho.restaurantapplication.databinding.FragmentAssembleYourBurgerBinding
import com.nacho.restaurantapplication.presentation.adapter.neworder.DressingAdapter
import com.nacho.restaurantapplication.presentation.adapter.neworder.InformationBurgerAdapter
import com.nacho.restaurantapplication.presentation.adapter.neworder.SizeAdapter
import com.nacho.restaurantapplication.presentation.adapter.neworder.ToppingAdapter
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AssembleYourBurgerFragment : Fragment() {

    private var _binding: FragmentAssembleYourBurgerBinding? = null
    private val binding get() = _binding!!

    private val newOrderVM: NewOrderViewModel by activityViewModels()
    private lateinit var sizeAdapter: SizeAdapter
    private lateinit var toppingList: ToppingAdapter
    private lateinit var dressingList: DressingAdapter
    private lateinit var informationAdapter: InformationBurgerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssembleYourBurgerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
    }

    private fun setupObservers() {

        newOrderVM.toppings.observe(viewLifecycleOwner) { toppings ->
            toppingList = ToppingAdapter(toppings) { title, isChecked ->
                newOrderVM.updateSelectedToppings(title, isChecked)
            }
            binding.assembleToppingsRv.adapter = toppingList
        }

        newOrderVM.dressings.observe(viewLifecycleOwner) { dressings ->
            dressingList = DressingAdapter(dressings) { title, isChecked ->
                newOrderVM.updateSelectedDressings(title, isChecked)
            }
            binding.assembleDressingsRv.adapter = dressingList
        }

        newOrderVM.selectedItems.observe(viewLifecycleOwner) { selectedItems ->
            informationAdapter = InformationBurgerAdapter(emptyList())
            binding.assembleCvInformationRv.adapter = informationAdapter
            informationAdapter.updateItems(selectedItems)
        }

        lifecycleScope.launch {
            delay(3000)
            binding.apply {
                assembleShimmer.visibility = View.GONE
                assembleCvSize.visibility = View.VISIBLE
                assembleBtnConfirm.visibility = View.VISIBLE
            }
        }

        sizesObserve()

    }

    private fun sizesObserve() {
        val sizeList = listOf(
            BurgerSize(getString(R.string.assemble_title_s), getString(R.string.assemble_subtitle_s), R.drawable.ic_burger_s, 11000),
            BurgerSize(getString(R.string.assemble_title_m), getString(R.string.assemble_subtitle_m), R.drawable.ic_burger_m, 12000),
            BurgerSize(getString(R.string.assemble_title_l), getString(R.string.assemble_subtitle_l), R.drawable.ic_burger_l, 13000),
            BurgerSize(getString(R.string.assemble_title_xl), getString(R.string.assemble_subtitle_xl), R.drawable.ic_burger_xl, 14000)
        )

        binding.apply {
            sizeAdapter = SizeAdapter(sizeList) { selectedSize ->
                assembleCvToppings.visibility = View.VISIBLE
                assembleCvDressings.visibility = View.VISIBLE
                assembleCvInformation.visibility = View.VISIBLE
                assembleCvInformationTxtTitle.text = getString(R.string.assemble_title, selectedSize.title)
                assembleCvInformationTxtTotal.text = getString(R.string.total, selectedSize.price.toString())
                assembleBtnConfirm.apply {
                    isClickable = true
                    background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.btn_gradient_red
                    )
                }
            }
            assembleSizeRv.adapter = sizeAdapter
        }

    }

}