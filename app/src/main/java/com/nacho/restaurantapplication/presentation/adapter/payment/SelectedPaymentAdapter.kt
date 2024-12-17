package com.nacho.restaurantapplication.presentation.adapter.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.data.model.Card
import com.nacho.restaurantapplication.databinding.ItemSelectedPaymentBinding

class SelectedPaymentAdapter(private val cardList: List<Card>) : RecyclerView.Adapter<SelectedPaymentAdapter.SelectedPaymentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedPaymentViewHolder {
        val binding = ItemSelectedPaymentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SelectedPaymentViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SelectedPaymentViewHolder, position: Int) {
        val card = cardList[position]
        holder.bind(card)
    }

    override fun getItemCount(): Int = cardList.size

    class SelectedPaymentViewHolder(private val binding: ItemSelectedPaymentBinding) : RecyclerView.ViewHolder(binding.root) {
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

                itemSelectedPaymentImgBankLogo.setImageResource(bankLogo)
                itemSelectedPaymentBackground.setBackgroundResource(bankBackground)

                itemSelectedPaymentTxtNumber.text = card.cardNumber
                itemSelectedPaymentTxtSinceDate.text = card.cardSince
                itemSelectedPaymentTxtUntilDate.text = card.cardUntil
                itemSelectedPaymentTxtName.text = card.cardName

                val cardType = when (card.cardType) {
                    "Visa" -> R.drawable.ic_visa
                    else -> R.drawable.ic_mastercard
                }

                itemSelectedPaymentImgBrand.setImageResource(cardType)

            }
        }
    }

}