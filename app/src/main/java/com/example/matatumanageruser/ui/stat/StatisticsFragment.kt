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
import com.example.matatumanageruser.databinding.FragmentTripBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import com.github.mikephil.charting.data.LineData

import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import androidx.core.content.ContextCompat
import android.graphics.DashPathEffect
import com.github.mikephil.charting.utils.Utils


@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    lateinit var statisticsBinding: FragmentStatisticsBinding
    lateinit var lineChart: LineChart

    val statViewModel: StatViewModel by viewModels()
    var allStats = mutableListOf<Statistics>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        statisticsBinding = FragmentStatisticsBinding.inflate(inflater,container, false )
        val view = statisticsBinding.root
        lineChart = statisticsBinding.lineChart
        setUpLineChart()
        getStats()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getItemSelectedInSpinner()
    }

    private fun getItemSelectedInSpinner() {
        when(statisticsBinding.spinnerParameterType.selectedItemPosition){
         0 -> numberTripChart()
         1 -> distanceChart()
         2 -> amountCollectedChart()
         3 -> expenses()



        }


    }

    fun getDateArray(): ArrayList<String>{
        var dates = ArrayList<String>()
        for (day in allStats){
            dates.add(day.timeStarted)
        }
        return dates

    }

    private fun expenses() {
        var values = ArrayList<Entry>()
        for ((i, item) in  allStats.withIndex()){
            values.add(Entry(item.expense.toFloat(), i.toFloat()))
        }
        configureChart(values)

    }

    fun configureChart(values: ArrayList<Entry>){

         val xAxis = lineChart.getXAxis()
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawLabels(true);
        xAxis.labelCount = getDateArray().size // important
        xAxis.setSpaceMax(0.5f) // optional
        xAxis.setSpaceMin(0.5f) // optional
        xAxis.valueFormatter = object : ValueFormatter() {
            override
            fun getFormattedValue(value: Float): String {
                // value is x as index
                return getDateArray()[value.toInt()]
            }
        }

        var set1: LineDataSet?
        if (lineChart.getData() != null &&
            lineChart.getData().getDataSetCount() > 0
        ) {
            set1 = lineChart.getData().getDataSetByIndex(0) as LineDataSet?
            set1!!.values = values
            lineChart.getData().notifyDataChanged()
            lineChart.notifyDataSetChanged()
        } else {
            set1 = LineDataSet(values, "Sample Data")
            set1.setDrawIcons(false)
            set1.enableDashedLine(10f, 5f, 0f)
            set1.enableDashedHighlightLine(10f, 5f, 0f)
            set1.color = Color.DKGRAY
            set1.setCircleColor(Color.DKGRAY)
            set1.lineWidth = 1f
            set1.circleRadius = 3f
            set1.setDrawCircleHole(false)
            set1.valueTextSize = 9f
            set1.setDrawFilled(true)
            set1.formLineWidth = 1f
            set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
            set1.formSize = 15f
            if (Utils.getSDKInt() >= 18) {
                val drawable = ContextCompat.getDrawable(requireContext(), com.example.matatumanageruser.R.color.black)
                set1.fillDrawable = drawable
            } else {
                set1.fillColor = Color.DKGRAY
            }
            val dataSets: ArrayList<ILineDataSet> = ArrayList()
            dataSets.add(set1)
            val data = LineData(dataSets)
            lineChart.setData(data)
        }


    }

    private fun amountCollectedChart() {
        TODO("Not yet implemented")
    }

    private fun distanceChart() {
        TODO("Not yet implemented")
    }

    private fun numberTripChart() {
        TODO("Not yet implemented")
    }

    private fun setUpLineChart(){
        lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(false)
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        lineChart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        lineChart.axisRight.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }
        lineChart.apply {
            description.text = "Avg Speed Over Time"
            legend.isEnabled = false
        }
    }

    fun getStats(){
        statViewModel.statsValues.observe(viewLifecycleOwner, {
            when(it){
                is StatViewModel.StatStatus.Failed ->{

                }

                is StatViewModel.StatStatus.Success -> {
                    allStats = it.stats as MutableList<Statistics>
                }
            }
        })
    }


}