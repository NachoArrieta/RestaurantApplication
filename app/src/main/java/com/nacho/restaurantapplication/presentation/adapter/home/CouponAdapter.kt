package com.nacho.restaurantapplication.presentation.adapter.home

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.data.model.Coupon
import com.nacho.restaurantapplication.databinding.ItemCouponBinding

class CouponAdapter(
    private val couponList: List<Coupon>,
    private val onItemClick: (Coupon) -> Unit
) : RecyclerView.Adapter<CouponAdapter.CouponViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val binding = ItemCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CouponViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val coupon = couponList[position]
        holder.bind(coupon)
        holder.itemView.setOnClickListener { onItemClick(coupon) }
    }

    override fun getItemCount(): Int = couponList.size

    class CouponViewHolder(private val binding: ItemCouponBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(coupon: Coupon) {
            with(binding) {
                itemCouponTxtTitle.text = coupon.title
                itemCouponTxtTitle.paintFlags = itemCouponTxtTitle.paintFlags or Paint.UNDERLINE_TEXT_FLAG
                itemCouponTxtSubtitle.text = coupon.description
                itemCouponTxtExpiration.text = when {
                    coupon.expirationDate.isNullOrEmpty() -> itemView.context.getString(R.string.coupons_not_expire)
                    else -> coupon.expirationDate
                }

                itemCouponTieCode.setText(coupon.code)

                itemCouponBtnWant.setOnClickListener {
                    val clipboardManager = itemView.context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                    val clip = ClipData.newPlainText("Coupon Code", coupon.code)
                    clipboardManager.setPrimaryClip(clip)
                    Toast.makeText(itemView.context, R.string.coupons_copy_code, Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

}