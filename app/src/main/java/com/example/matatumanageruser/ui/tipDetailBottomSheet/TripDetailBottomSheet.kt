package com.example.matatumanageruser.ui.tipDetailBottomSheet

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.matatumanageruser.data.Trip
import com.example.matatumanageruser.databinding.TripDetailsBinding
import com.example.matatumanageruser.ui.other.showLongToast
import com.example.matatumanageruser.ui.other.stringFromTl
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TripDetailBottomSheet(var trip: Trip): BottomSheetDialogFragment() {

    lateinit var tripDetailDialogBinding: TripDetailsBinding
    internal lateinit var listener: TripDetailBtSheetListener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        tripDetailDialogBinding = TripDetailsBinding.inflate(inflater, container, false)
        val view = tripDetailDialogBinding.root


        tripDetailDialogBinding.locationsTripDetailTxt.setText(trip.pickupPoint)
        if (trip.tripStatus == "started"){
            endTrip()
        }else{
            startTrip()
        }


        return view
    }



    private fun startTrip() {
        tripDetailDialogBinding.startTripBt.setOnClickListener {
            trip.tripStatus = "started"
            //yes this doesn't make sense, i just want to pass this data
            trip.date = stringFromTl(tripDetailDialogBinding.amountTl)
            listener.onStartTripClicked(trip, true)
            dialog?.dismiss()

        }
    }

    private fun endTrip() {
        tripDetailDialogBinding.startTripBt.setText("End trip")
        tripDetailDialogBinding.startTripBt.setOnClickListener {
            if(trip.moneyCollected < 1.0){
                if (stringFromTl(tripDetailDialogBinding.amountTl).isNotEmpty()){
                    val actualAmount = stringFromTl(tripDetailDialogBinding.amountTl).toDoubleOrNull()
                    if (actualAmount == null){
                        tripDetailDialogBinding.amountTl.boxStrokeColor = Color.RED
                        showLongToast("invalid number")
                        return@setOnClickListener
                    }else{
                        trip.moneyCollected = actualAmount
                    }
                }else{
                    tripDetailDialogBinding.amountTl.boxStrokeColor = Color.RED
                    showLongToast("Amount of money collected must not be 0")
                    return@setOnClickListener
                }
            }

            trip.tripStatus = "ended"
            listener.onStartTripClicked(trip, false)
            dialog?.dismiss()

        }
    }

    interface TripDetailBtSheetListener{
        fun onStartTripClicked(trip: Trip, nextAction: Boolean)
    }

    fun setListener(listener: TripDetailBtSheetListener){
        this.listener = listener
    }
}