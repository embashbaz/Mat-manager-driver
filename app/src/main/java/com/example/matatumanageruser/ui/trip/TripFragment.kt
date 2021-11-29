package com.example.matatumanageruser.ui.trip

import android.location.Location
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.matatumanageruser.MatManagerUserApp
import com.example.matatumanageruser.R
import com.example.matatumanageruser.data.Statistics
import com.example.matatumanageruser.data.Trip
import com.example.matatumanageruser.databinding.FragmentTripBinding
import com.example.matatumanageruser.databinding.FragmentTripListBinding
import com.example.matatumanageruser.services.TrackingService
import com.example.matatumanageruser.ui.dialogs.NoticeDialogFragment
import com.example.matatumanageruser.ui.other.showLongToast
import com.example.matatumanageruser.ui.placeRecomTrip.LocationSearchDialog
import com.example.matatumanageruser.ui.tipDetailBottomSheet.TripDetailBottomSheet
import com.example.matatumanageruser.utils.Constant
import com.example.matatumanageruser.utils.fromJsonToPolylines
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripFragment : Fragment(), NoticeDialogFragment.NoticeDialogListener,
                LocationSearchDialog.LocationSearchListener, TripDetailBottomSheet.TripDetailBtSheetListener{

    lateinit var tripBinding: FragmentTripBinding
    lateinit var mapView: MapView
    private val tripViewModel: TripViewModel by viewModels()
    private var stat: Statistics? = null
    private var activeTrip: Trip? = null
    private var pathPoint = mutableListOf<MutableList<LatLng>>()
    private var locMarker: Marker? = null

    private var map: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
       tripBinding = FragmentTripBinding.inflate(inflater,container, false )
        mapView = tripBinding.projectMapView
        val view = tripBinding.root

       getStatAndTrip()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapView.onCreate(savedInstanceState)

        mapView.getMapAsync {
            map = it
            addAllPolylines()

        }

        subscribeToObserver()
        subscribeTotrackingObserver()
        subscribeToLocationObserver()
    }

    fun getStatAndTrip(){
        stat = (activity?.application as MatManagerUserApp).statisticsObject
        //if (stat!=null){
           // pathPoint = fromJsonToPolylines(stat!!.pathPoints)
       // }
        activeTrip = (activity?.application as MatManagerUserApp).activeTrip
    }

    fun subscribeTotrackingObserver(){
        TrackingService.pathPoints.observe(viewLifecycleOwner, {
            pathPoint = it
            addLatestPolyline()
            moveCameraToUser()
        })
    }

    fun subscribeToLocationObserver(){
        TrackingService.currentLocation.observe(viewLifecycleOwner, {
            if(it != null){
                setLocationMarker(it)

            }
        })
    }

    private fun setLocationMarker(location: Location) {
        val loc = LatLng(location.latitude, location.longitude)
        if (locMarker == null){
            val markerOptions = MarkerOptions()
            markerOptions.position(loc)
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.final_bus))
            markerOptions.rotation(location.bearing)
            locMarker = map?.addMarker(markerOptions)
        }else{
            locMarker!!.position = loc
            locMarker!!.rotation = location.bearing
        }


    }

    fun subscribeToObserver(){
        tripViewModel.createNewTrip.observe(viewLifecycleOwner, {
            when(it){
                 is TripViewModel.TripStatus.Failed -> {
                     showLongToast(it.errorText)
                 }

                is TripViewModel.TripStatus.Success -> {
                    (activity?.application as MatManagerUserApp).activeTrip = it.trip
                    showLongToast("Trip Started")
                    getStatAndTrip()
                    tripViewModel.setNewTripStatusToEmpty()

                }

            }
        })

        tripViewModel.updateTrip.observe(viewLifecycleOwner, {
            when(it){
                is TripViewModel.TripStatus.Failed -> {
                    showLongToast(it.errorText)
                }

                is TripViewModel.TripStatus.Success -> {
                    ( activity?.application as MatManagerUserApp).statisticsObject!!.amount += activeTrip!!.moneyCollected
                    (activity?.application as MatManagerUserApp).statisticsObject!!.maxSpeed += 1.0
                    showLongToast("Trip ended")
                    (activity?.application as MatManagerUserApp).activeTrip = null
                    getStatAndTrip()
                    tripViewModel.setEndTripStatusToEmpty()

                }

            }

        })


    }

    private fun moveCameraToUser() {
        if(pathPoint.isNotEmpty() && pathPoint.last().isNotEmpty()) {
            map?.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    pathPoint.last().last(),
                    Constant.MAP_ZOOM
                )
            )
        }
    }

    private fun addAllPolylines() {
        for(polyline in pathPoint) {
            val polylineOptions = PolylineOptions()
                .color(Constant.POLYLINE_COLOR)
                .width(Constant.POLYLINE_WIDTH)
                .addAll(polyline)
            map?.addPolyline(polylineOptions)
        }
    }

    private fun addLatestPolyline() {
        if(pathPoint.isNotEmpty() && pathPoint.last().size > 1) {
            val preLastLatLng = pathPoint.last()[pathPoint.last().size - 2]
            val lastLatLng = pathPoint.last().last()
            val polylineOptions = PolylineOptions()
                .color(Constant.POLYLINE_COLOR)
                .width(Constant.POLYLINE_WIDTH)
                .add(preLastLatLng)
                .add(lastLatLng)
            map?.addPolyline(polylineOptions)
        }
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.trip_menu, menu)
        //
        //    menu.getItem(R.id.new_trip_loc).title = "End trip"
        //}

    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.new_trip_loc){
            if(activeTrip == null){
                if (stat == null){
                    openNoticeDialog("Ok","You can not create a trip unless you have started a day")
                }else{
                    openLocationDataText()
                }

            }else{
                openTripDetail(activeTrip!!)
            }
        }else if (item.itemId == R.id.trip_list_menu){
            this.findNavController().navigate(R.id.action_tripFragment_to_tripListFragment)

        }

        return super.onOptionsItemSelected(item)
    }

    fun openNoticeDialog(positiveButton: String,  message: String){
        val dialog = NoticeDialogFragment(positiveButton, message)
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "Day has not started yet")

    }

    fun openLocationDataText(){
        val dialog = LocationSearchDialog()
        dialog.setLocListen(this)
        dialog.show(parentFragmentManager, "From where to")
    }

    fun openTripDetail(trip: Trip){
        val dialog = TripDetailBottomSheet(trip)
        dialog.setListener(this)
        dialog.show(parentFragmentManager, "Trip")
    }

    override fun onSearchLocation(firstText: String, secondText: String) {
        val trip = Trip(pickupPoint =firstText+" / "+secondText )
        openTripDetail(trip)
    }

    override fun onStartTripClicked(trip: Trip, nextAction: Boolean) {
        if (nextAction){
            tripViewModel.createTrip(trip.pickupPoint, trip.date, stat!!.busPlate, stat!!.driverId)
        }else{
            tripViewModel.updateTrip(trip)
        }
    }


    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }


}