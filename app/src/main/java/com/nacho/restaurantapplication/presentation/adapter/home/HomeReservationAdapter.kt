package com.nacho.restaurantapplication.presentation.adapter.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nacho.restaurantapplication.R
import com.nacho.restaurantapplication.data.model.Reservation
import com.nacho.restaurantapplication.databinding.ItemHomeReservationBinding

class HomeReservationAdapter(private val reservationList: List<Reservation>) : RecyclerView.Adapter<HomeReservationAdapter.ReservationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeReservationAdapter.ReservationViewHolder {
        val binding = ItemHomeReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeReservationAdapter.ReservationViewHolder, position: Int) {
        val reservation = reservationList[position]
        holder.bind(reservation)
    }

    override fun getItemCount(): Int = reservationList.size

    inner class ReservationViewHolder(private val binding: ItemHomeReservationBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(reservation: Reservation) {
            with(binding) {
                itemHomeReservationTxtCity.text = reservation.city
                itemHomeReservationTxtDate.text = reservation.day
                itemHomeReservationTxtTime.text = reservation.hour
                itemHomeReservationTxtPlaces.text = reservation.places

                itemHomeReservationTxtAddress.text = when (reservation.city) {
                    "Río Tercero" -> binding.root.context.getString(R.string.reservations_address_rio)
                    else -> binding.root.context.getString(R.string.reservations_address_vdd)
                }

            }
        }
    }

}