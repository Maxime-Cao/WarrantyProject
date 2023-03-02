package com.warranty.warrantyproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.warranty.warrantyproject.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var binding : FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.addCompHomeScreen.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }

        // Ajouter lien de home vers look quand recyclerview ok

        return binding.root
    }
}