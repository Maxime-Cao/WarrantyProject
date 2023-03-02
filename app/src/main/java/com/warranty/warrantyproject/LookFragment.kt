package com.warranty.warrantyproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.warranty.warrantyproject.databinding.FragmentLookBinding

class LookFragment : Fragment() {
    private lateinit var binding : FragmentLookBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLookBinding.inflate(inflater,container,false)

        binding.returnCompLookScreen.setOnClickListener {
            goBackMenu(it)
        }

        binding.validateButton.setOnClickListener {
            goBackMenu(it)
        }

        return binding.root
    }

    fun goBackMenu(view:View) {
        view.findNavController().navigate(R.id.action_lookFragment_to_homeFragment)
    }
}