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
import com.example.matatumanageruser.R
import com.example.matatumanageruser.databinding.FragmentDashboardBinding
import com.example.matatumanageruser.services.TrackingUtils
import com.example.matatumanageruser.utils.Constant
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class DashboardFragment : Fragment(), EasyPermissions.PermissionCallbacks {

    private lateinit var dashboardBinding: FragmentDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardBinding = FragmentDashboardBinding.inflate(inflater, container, false)
        val view = dashboardBinding.root

        observeCardClicked()
        listenButtonClicked()


        return view

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
        }

        dashboardBinding.goToPerformanceCard.setOnClickListener {  }

        dashboardBinding.startDayCard.setOnClickListener {  }

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

    private fun checkOrRequestPermissions() {
        if(TrackingUtils.hasLocationPermissions(requireContext())) {
            dashboardViewModel.tripCardClicked(true)
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
}



