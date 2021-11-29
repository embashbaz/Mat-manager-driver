package com.example.matatumanageruser.ui.stat

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.matatumanageruser.data.Statistics
import com.example.matatumanageruser.databinding.FragmentStatisticsBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import androidx.core.content.ContextCompat
import android.graphics.DashPathEffect
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matatumanageruser.MatManagerUserApp
import com.example.matatumanageruser.ui.other.DefaultRecyclerAdapter
import com.example.matatumanageruser.ui.other.showLongToast
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.utils.Utils


@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    lateinit var statisticsBinding: FragmentStatisticsBinding
    lateinit var lineChart: LineChart

    val statViewModel: StatViewModel by viewModels()
    var allStats = mutableListOf<Statistics>()
    private val driverId: String by lazy { (activity?.application as MatManagerUserApp).driverObject!!.driverId }
    private lateinit var defaultRecyclerAdapter: DefaultRecyclerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        statisticsBinding = FragmentStatisticsBinding.inflate(inflater, container, false)
        val view = statisticsBinding.root
        lineChart = statisticsBinding.lineChart
        defaultRecyclerAdapter = DefaultRecyclerAdapter { stat -> onStatClicked(stat) }

        //setUpLineChart()
        getStats()

        return view
    }

    private fun onStatClicked(stat: Any) {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getItemSelectedInSpinner()
    }

    private fun getItemSelectedInSpinner() {
        // when(statisticsBinding.spinnerParameterType.selectedItemPosition){
        // 0 -> numberTripChart()
        // 1 -> distanceChart()
        //2 -> amountCollectedChart()
        // 3 -> expenses()
        // }

        statisticsBinding.spinnerParameterType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    when (position) {
                        0 -> numberTripChart()
                        1 -> distanceChart()
                        2 -> amountCollectedChart()
                        3 -> expenses()
                    }
                }

            }


        if (statisticsBinding.spinnerParameterType.selectedItemPosition == 0) {
            numberTripChart()
        } else if (statisticsBinding.spinnerParameterType.selectedItemPosition == 1) {
            distanceChart()
        } else if (statisticsBinding.spinnerParameterType.selectedItemPosition == 2) {
            amountCollectedChart()
        } else
            expenses()


    }


    private fun expenses() {
        var values = ArrayList<Entry>()
        for ((i, item) in allStats.withIndex()) {
            values.add(Entry(item.expense.toFloat(), i))
        }

        showGraph(getAllDates(), values, "Expenses")

    }


    private fun amountCollectedChart() {


        var values = ArrayList<Entry>()
        for ((i, item) in allStats.withIndex()) {
            values.add(Entry(item.amount.toFloat(), i))
        }

        showGraph(getAllDates(), values, "Amount collected")


    }

    private fun distanceChart() {
        var values = ArrayList<Entry>()
        for ((i, item) in allStats.withIndex()) {
            values.add(Entry(item.distance.toFloat(), i))
        }

        showGraph(getAllDates(), values, "Distance covered")
    }

    private fun numberTripChart() {
        var values = ArrayList<Entry>()
        for ((i, item) in allStats.withIndex()) {
            values.add(Entry(item.maxSpeed.toFloat(), i))
        }

        showGraph(getAllDates(), values, "Number of trip")
    }

    private fun getAllDates(): ArrayList<String> {
        if (allStats.isNotEmpty()) {
            val xValues = ArrayList<String>()
            for (stat in allStats) {
                xValues.add(stat.dayId)
            }
            return xValues
        } else return ArrayList()

    }

    private fun showGraph(
        xValues: ArrayList<String>,
        lineDataset: ArrayList<Entry>,
        description: String
    ) {
        val lineDataSet = LineDataSet(lineDataset, description)
        lineDataSet.color = Color.BLUE

        val data = LineData(xValues, lineDataSet)
        lineChart.data = data

        lineChart.animateXY(2000, 2000)
    }


    fun getStats() {
        statViewModel.getStat(driverId)
        statViewModel.statsValues.observe(viewLifecycleOwner, {
            when (it) {
                is StatViewModel.StatStatus.Failed -> {
                    showLongToast(it.errorText)
                }

                is StatViewModel.StatStatus.Success -> {
                    allStats = it.stats as MutableList<Statistics>
                    defaultRecyclerAdapter.setData(it.stats as ArrayList<Any>)
                    setRecyclerView()
                }
            }
        })
    }

    fun setRecyclerView() {
        statisticsBinding.statisticsRecyclerView.layoutManager = LinearLayoutManager(activity)
        statisticsBinding.statisticsRecyclerView.adapter = defaultRecyclerAdapter
    }


}