package com.example.matatumanageruser.ui.driverDetail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.matatumanageruser.R
import com.example.matatumanageruser.data.Driver
import com.example.matatumanageruser.databinding.FragmentDriverDetailBinding


class DriverDetailFragment : Fragment() {

    private lateinit var driverDetailBinding: FragmentDriverDetailBinding
    private val driverDetailViewModel: DriverDetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        driverDetailBinding = FragmentDriverDetailBinding.inflate(inflater, container, false)
        val view = driverDetailBinding.root

        driverDetailViewModel.driverObject.observe(viewLifecycleOwner, {
            if (it != null) {
                changeViewBehaviour()
                setDataToView(it)

            }

        })


        return view
    }

    private fun setDataToView(driver: Driver) {
        driverDetailBinding.nameRegister.editText!!.setText(driver.name)
        driverDetailBinding.emailRegisterDriverDetailTl.editText!!.setText(driver.email)
        driverDetailBinding.phoneDriverRegisterTl.editText!!.setText(driver.phoneNumber.toString())
        driverDetailBinding.cityAddressDriverTl.editText!!.setText(driver.address)
        driverDetailBinding.addressDriverTl.editText!!.setText(driver.address)
        driverDetailBinding.permitDriverTl.editText!!.setText(driver.permitNumber)
        driverDetailBinding.idNumberDriverTl.editText!!.setText(driver.cardId)

        if(driver.pictureLink.isNotEmpty())
            Glide.with(driverDetailBinding.root).load(driver.pictureLink).apply(RequestOptions.circleCropTransform()).into(driverDetailBinding.driverImageDetail)
    }

    private fun changeViewBehaviour() {
        driverDetailBinding.nameRegister.isEnabled = false
        driverDetailBinding.emailRegisterDriverDetailTl.isEnabled = false
        driverDetailBinding.phoneDriverRegisterTl.isEnabled = false
        driverDetailBinding.cityAddressDriverTl.isEnabled = false
        driverDetailBinding.addressDriverTl.isEnabled = false
        driverDetailBinding.permitDriverTl.isEnabled = false
        driverDetailBinding.idNumberDriverTl.isEnabled = false
        driverDetailBinding.registerDriverButton.visibility = View.INVISIBLE
    }

}