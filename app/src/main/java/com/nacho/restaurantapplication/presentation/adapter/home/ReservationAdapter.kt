package com.nacho.restaurantapplication.presentation.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.data.model.Reservation
import com.nacho.restaurantapplication.databinding.ItemReservationBinding

class ReservationAdapter(
    private val reservationList: List<Reservation>,
    private val onItemClick: (Reservation) -> Unit
) : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val binding = ItemReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val reservation = reservationList[position]
        holder.bind(reservation)
        holder.itemView.setOnClickListener { onItemClick(reservation) }
    }

    override fun getItemCount(): Int = reservationList.size

    class ReservationViewHolder(private val binding: ItemReservationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reservation: Reservation) {
            with(binding) {
                itemReservationTxtCity.text = reservation.city
                itemReservationTxtDate.text = reservation.day
                itemReservationTxtTime.text = reservation.hour
                itemReservationTxtPlaces.text = reservation.places

                itemReservationTxtAddress.text = when (reservation.city) {
                    "Río Tercero" -> binding.root.context.getString(R.string.reservations_address_rio)
                    else -> binding.root.context.getString(R.string.reservations_address_vdd)
                }
            }
        }

    }
}