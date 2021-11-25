package com.example.matatumanageruser.ui.tripList

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matatumanageruser.MatManagerUserApp
import com.example.matatumanageruser.R
import com.example.matatumanageruser.data.Issue
import com.example.matatumanageruser.data.Trip
import com.example.matatumanageruser.databinding.FragmentTripListBinding
import com.example.matatumanageruser.ui.expenses.ExpenseListViewModel
import com.example.matatumanageruser.ui.issueDetail.IssueDetailDialog
import com.example.matatumanageruser.ui.other.DefaultRecyclerAdapter
import com.example.matatumanageruser.ui.other.showLongToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TripListFragment : Fragment(){

    private lateinit var tripListBinding: FragmentTripListBinding
    private val tripListViewModel : TripListViewModel by viewModels()
    private lateinit var  defaultRecyclerAdapter: DefaultRecyclerAdapter
    private val driverId : String by lazy {  ( activity?.application as MatManagerUserApp).driverObject!!.driverId }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       tripListBinding = FragmentTripListBinding.inflate(inflater, container, false)
        val view = tripListBinding.root
        defaultRecyclerAdapter = DefaultRecyclerAdapter { trip -> onTripClicked(trip) }
        getTripList()

        return view
    }

    private fun getTripList(){
        tripListViewModel.getTrips(driverId)
        tripListViewModel.tripList.observe(viewLifecycleOwner, {
            when(it){
                is TripListViewModel.TripListStatus.Success -> {
                    if(!it.trips.isEmpty()) {
                        defaultRecyclerAdapter.setData(it.trips as ArrayList<Any>)
                        defaultRecyclerAdapter.notifyDataSetChanged()
                        setRecyclerView()
                        hideNoDataText()
                        hideProgressBar()
                    }else{
                        showNoDataText()
                    }
                }

                is TripListViewModel.TripListStatus.Failed -> {
                    showLongToast(it.errorText)
                    showNoDataText()
                    hideProgressBar()
                }
                is TripListViewModel.TripListStatus.Loading -> {
                    showProgressBar()
                    hideNoDataText()
                }

            }

        })

    }

    private fun onTripClicked(trip: Any) {
        if (trip is Trip){
            tripListViewModel.setTripObject(trip)

        }
    }

    fun setRecyclerView(){
        tripListBinding.tripListRecyclerView.layoutManager = LinearLayoutManager(activity)
        tripListBinding.tripListRecyclerView.adapter = defaultRecyclerAdapter
    }

    private fun hideNoDataText(){
        tripListBinding.noDataTripList.visibility = View.INVISIBLE
    }

    private fun hideProgressBar(){
        tripListBinding.tripListProgress.visibility = View.INVISIBLE
    }

    private fun showNoDataText(){
        tripListBinding.noDataTripList.visibility = View.VISIBLE
    }

    private fun showProgressBar(){
        tripListBinding.tripListProgress.visibility = View.VISIBLE
    }




}