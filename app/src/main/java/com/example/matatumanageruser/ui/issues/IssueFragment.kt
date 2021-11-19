package com.example.matatumanageruser.ui.issues

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matatumanageruser.MatManagerUserApp
import com.example.matatumanageruser.R
import com.example.matatumanageruser.data.Issue
import com.example.matatumanageruser.databinding.FragmentIssueBinding
import com.example.matatumanageruser.ui.issueDetail.IssueDetailDialog
import com.example.matatumanageruser.ui.other.DefaultRecyclerAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IssueFragment : Fragment(), IssueDetailDialog.IssueDetailDialogListener {

    private lateinit var issueBinding: FragmentIssueBinding
    private val issueListViewModel: IssuesViewModel by viewModels()
    private lateinit var  defaultRecyclerAdapter: DefaultRecyclerAdapter
    private val driverId : String by lazy {  ( activity?.application as MatManagerUserApp).driverObject!!.driverId }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        issueBinding = FragmentIssueBinding.inflate(inflater, container, false)
        val view = issueBinding.root
        defaultRecyclerAdapter = DefaultRecyclerAdapter { issue -> onIssueClicked(issue) }


        newIssue()

        return view
    }

    override fun onResume() {
        super.onResume()
        getIssues()
    }

    private fun newIssue() {
        issueListViewModel.newIssueAction.observe(viewLifecycleOwner, {
            if (it){
                openIssueDetailDialog(false, null)
                issueListViewModel.setNextActionNewIssue(false)
            }

        })
    }

    private fun getIssues(){

        issueListViewModel.getIssues(driverId)
        issueListViewModel.issueList.observe(viewLifecycleOwner, {
            when(it){
                is IssuesViewModel.IssueStatus.Success -> {

                    if(!it.issues.isEmpty()){

                    defaultRecyclerAdapter.setData(it.issues as ArrayList<Any>)
                    setRecyclerView()
                        hideNoDataText()
                        hideProgressBar()
                    }else{
                        showNoDataText()
                    }
                }
                is IssuesViewModel.IssueStatus.Failed ->{
                    showNoDataText()
                    hideProgressBar()
                }

                is IssuesViewModel.IssueStatus.Loading ->{
                    showProgressBar()
                    hideNoDataText()
                }
            }
        })
    }

    private fun onIssueClicked(issue: Any) {
        if(issue is Issue){
            issueListViewModel.setClickedIssueObject(issue)
            issueListViewModel.issueObject.observe(viewLifecycleOwner, {
                if(it != null){
                    openIssueDetailDialog(true, issue)
                }
            })
        }
    }

    fun setRecyclerView(){
        issueBinding.issueListRecycler.layoutManager = LinearLayoutManager(activity)
        issueBinding.issueListRecycler.adapter = defaultRecyclerAdapter
    }

    fun openIssueDetailDialog(type:Boolean, issue: Issue?){
        val issueDialog = IssueDetailDialog(type, issue)
        issueDialog.setListener(this)
        issueDialog.show(parentFragmentManager, "Issue")
    }

    private fun hideNoDataText(){
        issueBinding.noDataTxtIssueList.visibility = View.INVISIBLE
    }

    private fun hideProgressBar(){
       issueBinding.progressBarIssueList.visibility = View.INVISIBLE
    }

    private fun showNoDataText(){
        issueBinding.noDataTxtIssueList.visibility = View.VISIBLE
    }

    private fun showProgressBar(){
        issueBinding.progressBarIssueList.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.issue_list_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.new_issue_menu){
            issueListViewModel.setNextActionNewIssue(true)
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onSaveButtonClicked(issue: Issue) {
        issue.driverId = driverId
        issueListViewModel.createNewIssue(issue)
        issueListViewModel.addIssueResult.observe(viewLifecycleOwner, {
            when(it){
                is IssuesViewModel.IssueStatus.Success -> {

                }
            }
        })
    }
}