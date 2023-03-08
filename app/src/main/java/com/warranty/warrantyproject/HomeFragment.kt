package com.warranty.warrantyproject

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.warranty.warrantyproject.databinding.FragmentHomeBinding
import com.warranty.warrantyproject.domains.Notification
import com.warranty.warrantyproject.domains.NotificationPeriod
import com.warranty.warrantyproject.domains.Warranty
import com.warranty.warrantyproject.domains.WarrantyCover
import java.sql.Date

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    private val myObjectList = listOf(
        Warranty(
            1001,
            Date(2002,3,30),
            Date(2003, 10, 30),
            "not",
            Notification(
                false,
                NotificationPeriod.ONE_YEAR
            ),
            WarrantyCover(
                "not",
                "Samsung",
                "A phone cost 1001$",
                "Médiamarkt"
            )
        ),
        Warranty(
            1111,
            Date(2005,3,30),
            Date(2007, 10, 30),
            "not",
            Notification(
                false,
                NotificationPeriod.ONE_MONTH
            ),
            WarrantyCover(
                "not",
                "iPhone",
                "A phone cost 1111$",
                "Apple Store"
            )
        )
    )
    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.addCompHomeScreen.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }
        binding.recyclerView.adapter = MyAdapter(myObjectList.map{ it.warrantyCover })
        return binding.root
    }
}