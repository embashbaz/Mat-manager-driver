package com.example.matatumanageruser.ui.dashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.matatumanageruser.R
import com.example.matatumanageruser.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

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

        dashboardBinding.goTripCard.setOnClickListener {  }

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
    }


}