package com.nacho.restaurantapplication.presentation.fragment.neworder.payment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
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
import com.nacho.restaurantapplication.core.utils.Constants
import com.nacho.restaurantapplication.databinding.FragmentAddCardBinding
import com.nacho.restaurantapplication.domain.model.UserCard
import com.nacho.restaurantapplication.presentation.viewmodel.neworder.NewOrderViewModel
import com.nacho.restaurantapplication.presentation.viewmodel.payment.PaymentMethodViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddCardFragment : Fragment() {

    private var _binding: FragmentAddCardBinding? = null
    private val binding get() = _binding!!

    private val newOrderVM: NewOrderViewModel by activityViewModels()
    private val paymentMethodVM: PaymentMethodViewModel by activityViewModels()

    private lateinit var cardBank: String
    private lateinit var cardType: String
    private lateinit var cardBrand: String
    private var cardAmount: Int = 0
    private var cardLimit: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddCardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        newOrderVM.setToolbarTitle(getString(R.string.toolbar_title_add_payment_method))

        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        with(binding) {
            addCardTieNumber.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            addCardTieNumber.formatAsCardNumber()
            addCardTieNumber.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            addCardTieNumber.onTextChanged { onFieldChanged() }

            addCardTieSince.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            addCardTieSince.formatAsExpirationDate()
            addCardTieSince.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            addCardTieSince.onTextChanged { onFieldChanged() }

            addCardTieUntil.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            addCardTieUntil.formatAsExpirationDate()
            addCardTieUntil.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            addCardTieUntil.onTextChanged { onFieldChanged() }

            addCardTieTitularName.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            addCardTieTitularName.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            addCardTieTitularName.onTextChanged { onFieldChanged() }
            addCardTieTitularName.addTextChangedListener { editable ->
                addCardCardTxtName.text = editable.toString()
            }

            addCardTieCvv.loseFocusAfterAction(EditorInfo.IME_ACTION_NEXT)
            addCardTieCvv.setOnFocusChangeListener { _, hasFocus -> onFieldChanged(hasFocus) }
            addCardTieCvv.onTextChanged { onFieldChanged() }

            addCardButton.setOnClickListener {

                if (!addCardButton.isClickable) {
                    return@setOnClickListener
                }

                try {
                    val uid = paymentMethodVM.uId.value
                    val cardInfo = UserCard(
                        cardBank = cardBank,
                        cardType = cardType,
                        cardNumber = binding.addCardTieNumber.text.toString(),
                        cardName = binding.addCardTieTitularName.text.toString(),
                        cardSince = binding.addCardTieSince.text.toString(),
                        cardUntil = binding.addCardTieUntil.text.toString(),
                        cardCvv = binding.addCardTieCvv.text.toString(),
                        cardBrand = cardBrand,
                        cardAmount = cardAmount,
                        cardLimit = cardLimit
                    )

                    if (uid != null) {
                        paymentMethodVM.addCard(uid, cardInfo)
                        findNavController().navigate(R.id.action_addCardFragment_to_selectedPaymentMethodFragment)
                    }
                } catch (_: UninitializedPropertyAccessException) { }
            }

        }

    }

    private fun setupObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                paymentMethodVM.viewStatePaymentMethod.collect { viewState ->
                    updateUI(viewState)
                }
            }
        }
    }

    private fun validateCardNumber(cardNumber: String): Boolean = Constants.SUPPORTED_CARD_NUMBERS.contains(cardNumber)

    private fun validateCardDuplicated(cardNumber: String): Boolean = paymentMethodVM.userCards.value?.any { it.cardNumber == cardNumber } == false

    private fun onFieldChanged(hasFocus: Boolean = false) {
        val cardNumber = binding.addCardTieNumber.text.toString()
        val cardSince = binding.addCardTieSince.text.toString()
        val cardUntil = binding.addCardTieUntil.text.toString()

        if (!hasFocus) {
            val userCard = UserCard(
                cardNumber = cardNumber,
                cardSince = cardSince,
                cardUntil = cardUntil,
                cardName = binding.addCardTieTitularName.text.toString(),
                cardCvv = binding.addCardTieCvv.text.toString()
            )

            val isCardValid = validateCardNumber(cardNumber)
            val isCardDuplicated = validateCardDuplicated(cardNumber)
            paymentMethodVM.onFieldsChangedPaymentMethod(userCard, isCardValid)

            binding.addCardTilNumber.error = when {
                cardNumber.length < Constants.CARD_NUMBER_LENGTH -> getString(R.string.add_payment_methods_error)
                !isCardValid -> getString(R.string.add_payment_methods_error_card_number)
                !isCardDuplicated -> getString(R.string.add_payment_methods_error_card_duplicated)
                else -> null
            }

            if (isCardValid) {
                setCardBackgroundAndBrand(cardNumber)
            }

            val isValidDateRange = validateDateRange(cardSince, cardUntil)
            binding.addCardTilUntil.error = if (!isValidDateRange) getString(R.string.add_payment_methods_card_data_error) else null

        }
    }

    private fun updateUI(viewState: PaymentMethodViewState) {

        with(binding) {
            val isCardNumberValidAndSupported =
                viewState.isValidCardNumber && validateCardNumber(addCardTieNumber.text.toString()) && validateCardDuplicated(addCardTieNumber.text.toString())
            val cardSince = binding.addCardTieSince.text.toString()
            val cardUntil = binding.addCardTieUntil.text.toString()
            val isValidDateRange = validateDateRange(cardSince, cardUntil)

            if (isCardNumberValidAndSupported) {
                addCardCardTxtNumber.text = addCardTieNumber.text.toString()
                addCardTilSince.visibility = View.VISIBLE
                addCardTilUntil.visibility = View.VISIBLE
                addCardCard.visibility = View.VISIBLE
            } else {
                addCardTilSince.visibility = View.GONE
                addCardTilUntil.visibility = View.GONE
                addCardCard.visibility = View.GONE
            }

            if (viewState.isValidCardSince) addCardCardTxtSinceDate.text = addCardTieSince.text.toString()
            if (viewState.isValidCardUntil) addCardCardTxtUntilDate.text = addCardTieUntil.text.toString()

            val isNumberValid = !addCardTieNumber.text.isNullOrEmpty() && viewState.isValidCardNumber
            val isSinceValid = !addCardTieSince.text.isNullOrEmpty() && viewState.isValidCardSince
            val isUntilValid = !addCardTieUntil.text.isNullOrEmpty() && viewState.isValidCardUntil
            val isNameValid = !addCardTieTitularName.text.isNullOrEmpty() && viewState.isValidCardName
            val isCvvValid = !addCardTieCvv.text.isNullOrEmpty() && viewState.isValidCardCvv

            if (isSinceValid && isUntilValid && isValidDateRange) addCardTilTitularName.visibility = View.VISIBLE
            if (isNameValid) addCardTilCvv.visibility = View.VISIBLE

            addCardTilNumber.error = if (!viewState.isValidCardNumber) getString(R.string.add_payment_methods_error_card_number) else null
            addCardTilSince.error = if (!viewState.isValidCardSince) getString(R.string.add_payment_methods_error_since) else null
            addCardTilUntil.error = if (!viewState.isValidCardUntil) getString(R.string.add_payment_methods_error_since) else null
            addCardTilTitularName.error = if (!viewState.isValidCardName) getString(R.string.add_payment_methods_error_card_name) else null
            addCardTilCvv.error = if (!viewState.isValidCardCvv) getString(R.string.add_payment_methods_error_card_cvv) else null

            if (isNumberValid && isSinceValid && isUntilValid && isNameValid && isCvvValid) {

                addCardButton.apply {
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
                    addCardCardBackground.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_galicia))
                    addCardCardImgBrand.setImageResource(R.drawable.ic_mastercard)
                    addCardCardImgBankLogo.setImageResource(R.drawable.ic_galicia)
                    cardBank = "Galicia"
                    cardType = "Crédito"
                    cardBrand = "Mastercard"
                    cardAmount = 0
                    cardLimit = 250000
                }

                "4547  4400  0819  4546" -> {
                    addCardCardBackground.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_galicia))
                    addCardCardImgBrand.setImageResource(R.drawable.ic_visa)
                    addCardCardImgBankLogo.setImageResource(R.drawable.ic_galicia)
                    cardBank = "Galicia"
                    cardType = "Débito"
                    cardBrand = "Visa"
                    cardAmount = 240000
                    cardLimit = 0
                }

                "4546  3900  1724  3131" -> {
                    addCardCardBackground.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_macro))
                    addCardCardImgBrand.setImageResource(R.drawable.ic_visa)
                    addCardCardImgBankLogo.setImageResource(R.drawable.ic_macro)
                    cardBank = "Macro"
                    cardType = "Crédito"
                    cardBrand = "Visa"
                    cardAmount = 0
                    cardLimit = 20000
                }

                "4546  3900  0618  8484" -> {
                    addCardCardBackground.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_macro))
                    addCardCardImgBrand.setImageResource(R.drawable.ic_mastercard)
                    addCardCardImgBankLogo.setImageResource(R.drawable.ic_macro)
                    cardBank = "Macro"
                    cardType = "Débito"
                    cardBrand = "Mastercard"
                    cardAmount = 2000
                    cardLimit = 0
                }

                else -> {
                    addCardCardBackground.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.color_santander))
                    addCardCardImgBrand.setImageResource(R.drawable.ic_visa)
                    addCardCardImgBankLogo.setImageResource(R.drawable.ic_santander)
                    cardBank = "Santander"
                    cardType = "Credito"
                    cardBrand = "Visa"
                    cardAmount = 0
                    cardLimit = 24000
                }
            }

        }
    }

    private fun validateDateRange(since: String, until: String): Boolean {
        if (since.length == 5 && until.length == 5) {
            val sinceMonth = since.substring(0, 2).toInt()
            val sinceYear = since.substring(3, 5).toInt()
            val untilMonth = until.substring(0, 2).toInt()
            val untilYear = until.substring(3, 5).toInt()

            return when {
                untilYear > sinceYear -> true
                untilYear == sinceYear && untilMonth > sinceMonth -> true
                else -> false
            }
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        paymentMethodVM.onFieldsChangedPaymentMethod(UserCard(), true)
    }

}