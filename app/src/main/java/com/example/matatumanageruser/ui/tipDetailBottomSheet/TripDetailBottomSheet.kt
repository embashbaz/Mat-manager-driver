package com.example.matatumanageruser.ui.tipDetailBottomSheet

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import com.example.matatumanageruser.data.Trip
import com.example.matatumanageruser.databinding.TripDetailsBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TripDetailBottomSheet(var trip: Trip): BottomSheetDialogFragment() {

    lateinit var tripDetailDialogBinding: TripDetailsBinding
    internal lateinit var listener: TripDetailBtSheetListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater;
            tripDetailDialogBinding = TripDetailsBinding.inflate(inflater)

            tripDetailDialogBinding.locationsTripDetailTxt.setText(trip.pickupPoint)
            if (trip.tripStatus == "started"){
                endTrip()
            }else{
                startTrip()
            }


            builder.create()

        }?: throw IllegalStateException("Activity cannot be null")


    }

    private fun startTrip() {
        tripDetailDialogBinding.startTripBt.setOnClickListener {
            trip.tripStatus = "started"
            listener.onStartTripClicked(trip, true)

        }
    }

    private fun endTrip() {
        tripDetailDialogBinding.startTripBt.setText("End trip")
        tripDetailDialogBinding.startTripBt.setOnClickListener {
            trip.tripStatus = "ended"
            listener.onStartTripClicked(trip, false)

        }
    }

    interface TripDetailBtSheetListener{
        fun onStartTripClicked(trip: Trip, nextAction: Boolean)
    }

    fun setListener(listener: TripDetailBtSheetListener){
        this.listener = listener
    }
}