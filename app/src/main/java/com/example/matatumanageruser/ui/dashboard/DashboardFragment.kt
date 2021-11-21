package com.example.matatumanageruser.ui.dashboard

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.matatumanageruser.MatManagerUserApp
import com.example.matatumanageruser.R
import com.example.matatumanageruser.data.Issue
import com.example.matatumanageruser.data.Statistics
import com.example.matatumanageruser.databinding.FragmentDashboardBinding
import com.example.matatumanageruser.services.TrackingUtils
import com.example.matatumanageruser.ui.StartDayDialog
import com.example.matatumanageruser.ui.issueDetail.IssueDetailDialog
import com.example.matatumanageruser.ui.other.showLongToast
import com.example.matatumanageruser.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class DashboardFragment : Fragment(), EasyPermissions.PermissionCallbacks, StartDayDialog.SaveDayDialogListener {

    private lateinit var dashboardBinding: FragmentDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private var locationPermissionsGranted = false
    private val driverId : String by lazy {  ( activity?.application as MatManagerUserApp).driverObject!!.driverId }
    private var stat: Statistics? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = dashboardBinding.root

        observeCardClicked()
        listenButtonClicked()
        setStat()
        observeDayCreated()


        return view

    }

    private fun setStat() {
        stat = ( activity?.application as MatManagerUserApp).statisticsObject
    }

    private fun listenButtonClicked() {
       dashboardBinding.goToIssuesCard.setOnClickListener {
           dashboardViewModel.issueCardClicked(true)
       }

        dashboardBinding.goToExpensesList.setOnClickListener {
            dashboardViewModel.expenseCardClicked(true)
        }

        dashboardBinding.goTripCard.setOnClickListener {
            checkOrRequestPermissions()
            if(locationPermissionsGranted){
                dashboardViewModel.tripCardClicked(true)
            }
        }

        dashboardBinding.goToPerformanceCard.setOnClickListener {  }

        dashboardBinding.startDayCard.setOnClickListener {
            checkOrRequestPermissions()
            if(locationPermissionsGranted && stat == null){
                    openStartDayDialog()
            }
        }

    }

    private fun observeDayCreated(){
        dashboardViewModel.startDayResult.observe(viewLifecycleOwner, {
            when(it){
                is DashboardViewModel.StartDayStatus.Success -> {
                   showLongToast(it.resultText)
                   this.findNavController().navigate(R.id.action_dashboardFragment_to_tripFragment)
                }

                is DashboardViewModel.StartDayStatus.Failed -> {
                    showLongToast(it.errorText)
                }


            }
        })
    }


    private fun observeCardClicked() {


        dashboardViewModel.issuesCardClicked.observe(viewLifecycleOwner, {
            if (it){
                this.findNavController().navigate(R.id.action_dashboardFragment_to_issueFragment)
                dashboardViewModel.issueCardClicked(false)
            }
        })

        dashboardViewModel.expenseCardClicked.observe(viewLifecycleOwner, {
            if (it){
                this.findNavController().navigate(R.id.action_dashboardFragment_to_expenseListFragment)
                dashboardViewModel.expenseCardClicked(false)
            }
        })

        dashboardViewModel.tripCardClicked.observe(viewLifecycleOwner, {
            if (it){
            this.findNavController().navigate(R.id.action_dashboardFragment_to_tripFragment)
            dashboardViewModel.tripCardClicked(false)
            }
        })
    }

    fun openStartDayDialog(){
        val startDayDialog = StartDayDialog()
        startDayDialog.setListener(this)
        startDayDialog.show(parentFragmentManager, "Issue")
    }

    private fun checkOrRequestPermissions() {
        if(TrackingUtils.hasLocationPermissions(requireContext())) {
            locationPermissionsGranted = true
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                Constant.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permissions to use this app.",
                Constant.REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions(perms.toTypedArray(), requestCode)
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {}

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onSaveButtonClicked(plate: String) {
        if (stat == null){
            dashboardViewModel.startDayRequest(plate, driverId)
        }
    }
}



