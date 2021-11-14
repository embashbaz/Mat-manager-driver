package com.example.matatumanageruser.ui.issues

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matatumanageruser.R
import com.example.matatumanageruser.data.Issue
import com.example.matatumanageruser.databinding.FragmentIssueBinding
import com.example.matatumanageruser.ui.other.DefaultRecyclerAdapter


class IssueFragment : Fragment() {

    private lateinit var issueBinding: FragmentIssueBinding
    private val issueListViewModel: IssuesViewModel by viewModels()
    private lateinit var  defaultRecyclerAdapter: DefaultRecyclerAdapter
    private var driverId = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        issueBinding = FragmentIssueBinding.inflate(inflater, container, false)
        val view = issueBinding.root
        defaultRecyclerAdapter = DefaultRecyclerAdapter { issue -> onIssueClicked(issue) }
        getIssues()


        return view
    }

    private fun getIssues(){
        issueListViewModel.getIssues(driverId)
        issueListViewModel.issueList.observe(viewLifecycleOwner, {
            when(it){
                is IssuesViewModel.IssueStatus.Success -> {
                    defaultRecyclerAdapter.setData(it.issues as ArrayList<Any>)
                    setRecyclerView()
                }
            }
        })
    }

    private fun onIssueClicked(issue: Any) {
        if(issue is Issue){
            issueListViewModel.setClickedIssueObject(issue)
        }
    }

    fun setRecyclerView(){
        issueBinding.issueListRecycler.layoutManager = LinearLayoutManager(activity)
        issueBinding.issueListRecycler.adapter = defaultRecyclerAdapter
    }

}