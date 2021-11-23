package com.example.matatumanageruser.ui.placeRecomTrip

import android.app.AlertDialog
import android.app.Dialog
import android.content.ContentValues
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matatumanageruser.data.PredictionAutoComplete
import com.example.matatumanageruser.databinding.LocationSearchDialogBinding
import com.example.matatumanageruser.ui.other.DefaultRecyclerAdapter
import com.example.matatumanageruser.ui.other.showLongToast
import com.example.matatumanageruser.ui.other.stringFromTl
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.LocationBias
import com.google.android.libraries.places.api.model.RectangularBounds
import com.google.android.libraries.places.api.model.TypeFilter
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient

class LocationSearchDialog : DialogFragment(){

    lateinit var locationSearchDialogBinding: LocationSearchDialogBinding
    internal lateinit var listener :LocationSearchListener

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)

            val inflater = requireActivity().layoutInflater;
            locationSearchDialogBinding = LocationSearchDialogBinding.inflate(inflater)

            val view = locationSearchDialogBinding.root
            builder.setView(view)

            locationSearchDialogBinding.addPlacesToTrip.setOnClickListener {
                if (stringFromTl(locationSearchDialogBinding.firstLocationSearch).isNotEmpty() && stringFromTl(locationSearchDialogBinding.secondLocationSearch).isNotEmpty()) {
                    listener.onSearchLocation(
                        stringFromTl(locationSearchDialogBinding.firstLocationSearch),
                        stringFromTl(locationSearchDialogBinding.secondLocationSearch)
                    )
                    dialog?.dismiss()
                }
                else
                    showLongToast("Both locations must be given")
            }



            builder.create()

        }?: throw IllegalStateException("Activity cannot be null")


    }

    interface LocationSearchListener{
        fun onSearchLocation(firstText: String, secondText: String)
    }

    fun setLocListen(listener :LocationSearchListener){
        this.listener = listener
    }


}