package com.nacho.restaurantapplication.presentation.adapter.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.data.model.Card
import com.nacho.restaurantapplication.databinding.ItemMyPaymentMethodsBinding

class CardAdapter(
    private val cardList: List<Card>,
) : RecyclerView.Adapter<CardAdapter.CardViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val binding = ItemMyPaymentMethodsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val card = cardList[position]
        holder.bind(card)
    }

    override fun getItemCount(): Int = cardList.size

    class CardViewHolder(private val binding: ItemMyPaymentMethodsBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(card: Card) {
            with(binding) {

                val bankLogo = when (card.cardBank) {
                    "Galicia" -> R.drawable.ic_galicia
                    "Macro" -> R.drawable.ic_macro
                    else -> R.drawable.ic_santander
                }

                val bankBackground = when (card.cardBank) {
                    "Galicia" -> R.color.color_galicia
                    "Macro" -> R.color.color_macro
                    else -> R.color.color_santander
                }

                itemMyPaymentMethodsImgBankLogo.setImageResource(bankLogo)
                itemMyPaymentMethodsBackground.setBackgroundResource(bankBackground)

                itemMyPaymentTxtNumber.text = card.cardNumber
                itemMyPaymentTxtSinceDate.text = card.cardSince
                itemMyPaymentTxtUntilDate.text = card.cardUntil
                itemMyPaymentTxtName.text = card.cardName

                val cardBrand = when (card.cardBrand) {
                    "Visa" -> R.drawable.ic_visa
                    else -> R.drawable.ic_mastercard
                }

                itemMyPaymentMethodsImgBrand.setImageResource(cardBrand)

            }
        }
    }

}