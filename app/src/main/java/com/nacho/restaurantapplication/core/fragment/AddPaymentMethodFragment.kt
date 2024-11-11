package com.nacho.restaurantapplication.core.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.core.extensions.formatAsCardNumber
import com.nacho.restaurantapplication.core.extensions.formatAsExpirationDate
import com.nacho.restaurantapplication.core.extensions.loseFocusAfterAction
import com.nacho.restaurantapplication.core.extensions.onTextChanged
import com.nacho.restaurantapplication.core.fragment.state.PaymentMethodViewState
import com.nacho.restaurantapplication.core.utils.Constants.CARD_NUMBER_LENGTH
import com.nacho.restaurantapplication.core.utils.Constants.SUPPORTED_CARD_NUMBERS
import com.nacho.restaurantapplication.databinding.FragmentAddPaymentMethodBinding
import com.nacho.restaurantapplication.domain.model.UserCard
import com.nacho.restaurantapplication.presentation.viewmodel.payment.PaymentMethodViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddPaymentMethodFragment : Fragment() {

    private var _binding: FragmentAddPaymentMethodBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PaymentMethodViewModel by activityViewModels()

    private lateinit var cardBank: String
    private lateinit var cardType: String
    private lateinit var cardBrand: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddPaymentMethodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        with(binding) {
            addPaymentTieNumber.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            addPaymentTieNumber.formatAsCardNumber()
            addPaymentTieNumber.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            addPaymentTieNumber.onTextChanged { onFieldChanged() }

            addPaymentTieSince.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            addPaymentTieSince.formatAsExpirationDate()
            addPaymentTieSince.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            addPaymentTieSince.onTextChanged { onFieldChanged() }

            addPaymentTieUntil.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            addPaymentTieUntil.formatAsExpirationDate()
            addPaymentTieUntil.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            addPaymentTieUntil.onTextChanged { onFieldChanged() }

            addPaymentTieTitularName.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            addPaymentTieTitularName.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            addPaymentTieTitularName.onTextChanged { onFieldChanged() }
            addPaymentTieTitularName.addTextChangedListener { editable ->
                addPaymentCardTxtName.text = editable.toString()
            }

            addPaymentTieCvv.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            addPaymentTieCvv.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            addPaymentTieCvv.onTextChanged { onFieldChanged() }

            addPaymentButton.setOnClickListener {
                val uid = viewModel.uId.value
                val cardInfo = UserCard(
                    cardBank = cardBank,
                    cardType = cardType,
                    cardNumber = binding.addPaymentTieNumber.text.toString(),
                    cardName = binding.addPaymentTieTitularName.text.toString(),
                    cardSince = binding.addPaymentTieSince.text.toString(),
                    cardUntil = binding.addPaymentTieUntil.text.toString(),
                    cardCvv = binding.addPaymentTieCvv.text.toString(),
                    cardBrand = cardBrand
                )

                if (uid != null) {
                    viewModel.addCard(uid, cardInfo)
                    goToLoading()
                }

            }

        }

    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewStatePaymentMethod.collect { viewState ->
                    updateUI(viewState)
                }
            }
        }
    }

    private fun validateCardNumber(cardNumber: String): Boolean = SUPPORTED_CARD_NUMBERS.contains(cardNumber)

    private fun onFieldChanged(hasFocus: Boolean = false) {
        val cardNumber = binding.addPaymentTieNumber.text.toString()

        if (!hasFocus) {
            val userCard = UserCard(
                cardNumber = cardNumber,
                cardSince = binding.addPaymentTieSince.text.toString(),
                cardUntil = binding.addPaymentTieUntil.text.toString(),
                cardName = binding.addPaymentTieTitularName.text.toString(),
                cardCvv = binding.addPaymentTieCvv.text.toString()
            )

            val isCardValid = validateCardNumber(cardNumber)
            viewModel.onFieldsChangedPaymentMethod(userCard, isCardValid)

            binding.addPaymentTilNumber.error = when {
                cardNumber.length < CARD_NUMBER_LENGTH -> getString(R.string.add_payment_methods_error)
                !isCardValid -> getString(R.string.add_payment_methods_error_card_number)
                else -> null
            }

            if (isCardValid) {
                setCardBackgroundAndBrand(cardNumber)
            }

        }
    }

    private fun updateUI(viewState: PaymentMethodViewState) {

        with(binding) {
            val isCardNumberValidAndSupported = viewState.isValidCardNumber && validateCardNumber(addPaymentTieNumber.text.toString())

            if (isCardNumberValidAndSupported) {
                addPaymentCardTxtNumber.text = addPaymentTieNumber.text.toString()
                addPaymentTilSince.visibility = View.VISIBLE
                addPaymentTilUntil.visibility = View.VISIBLE
                addPaymentCard.visibility = View.VISIBLE
            } else {
                addPaymentTilSince.visibility = View.GONE
                addPaymentTilUntil.visibility = View.GONE
                addPaymentCard.visibility = View.GONE
            }

            if (viewState.isValidCardSince) addPaymentCardTxtSinceDate.text = addPaymentTieSince.text.toString()
            if (viewState.isValidCardUntil) addPaymentCardTxtUntilDate.text = addPaymentTieUntil.text.toString()

            val isNumberValid = !addPaymentTieNumber.text.isNullOrEmpty() && viewState.isValidCardNumber
            val isSinceValid = !addPaymentTieSince.text.isNullOrEmpty() && viewState.isValidCardSince
            val isUntilValid = !addPaymentTieUntil.text.isNullOrEmpty() && viewState.isValidCardUntil
            val isNameValid = !addPaymentTieTitularName.text.isNullOrEmpty() && viewState.isValidCardName
            val isCvvValid = !addPaymentTieCvv.text.isNullOrEmpty() && viewState.isValidCardCvv

            if (isSinceValid && isUntilValid) addPaymentTilTitularName.visibility = View.VISIBLE
            if (isNameValid) addPaymentTilCvv.visibility = View.VISIBLE

            addPaymentTilNumber.error = if (!viewState.isValidCardNumber) getString(R.string.add_payment_methods_error_card_number) else null
            addPaymentTilSince.error = if (!viewState.isValidCardSince) getString(R.string.add_payment_methods_error_since) else null
            addPaymentTilUntil.error = if (!viewState.isValidCardUntil) getString(R.string.add_payment_methods_error_since) else null
            addPaymentTilTitularName.error = if (!viewState.isValidCardName) getString(R.string.add_payment_methods_error_card_name) else null
            addPaymentTilCvv.error = if (!viewState.isValidCardCvv) getString(R.string.add_payment_methods_error_card_cvv) else null

            if (isNumberValid && isSinceValid && isUntilValid && isNameValid && isCvvValid) {

                addPaymentButton.apply {
                    isClickable = true
                    background = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.btn_gradient_red
                    )
                }

            }

        }
    }

    private fun setCardBackgroundAndBrand(cardNumber: String) {
        with(binding) {

            when (cardNumber) {
                "4546  4200  0617  4342" -> {
                    addPaymentCardBackground.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_galicia))
                    addPaymentCardImgBrand.setImageResource(R.drawable.ic_mastercard)
                    addPaymentCardImgBankLogo.setImageResource(R.drawable.ic_galicia)
                    cardBank = "Galicia"
                    cardType = "Crédito"
                    cardBrand = "Mastercard"
                }

                "4547  4400  0819  4546" -> {
                    addPaymentCardBackground.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_galicia))
                    addPaymentCardImgBrand.setImageResource(R.drawable.ic_visa)
                    addPaymentCardImgBankLogo.setImageResource(R.drawable.ic_galicia)
                    cardBank = "Galicia"
                    cardType = "Débito"
                    cardBrand = "Visa"
                }

                "4546  3900  1724  3131" -> {
                    addPaymentCardBackground.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_macro))
                    addPaymentCardImgBrand.setImageResource(R.drawable.ic_visa)
                    addPaymentCardImgBankLogo.setImageResource(R.drawable.ic_macro)
                    cardBank = "Macro"
                    cardType = "Crédito"
                    cardBrand = "Visa"
                }

                "4546  3900  0618  8484" -> {
                    addPaymentCardBackground.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_macro))
                    addPaymentCardImgBrand.setImageResource(R.drawable.ic_mastercard)
                    addPaymentCardImgBankLogo.setImageResource(R.drawable.ic_macro)
                    cardBank = "Macro"
                    cardType = "Débito"
                    cardBrand = "Mastercard"
                }

                else -> {
                    addPaymentCardBackground.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_santander))
                    addPaymentCardImgBrand.setImageResource(R.drawable.ic_visa)
                    addPaymentCardImgBankLogo.setImageResource(R.drawable.ic_santander)
                    cardBank = "Santander"
                    cardType = "Visa"
                    cardBrand = "Mastercard"
                }
            }

        }
    }

    private fun goToLoading() {
        findNavController().navigate(R.id.action_addPaymentMethodFragment_to_loadingFragment2)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.onFieldsChangedPaymentMethod(UserCard(), true)
    }

}