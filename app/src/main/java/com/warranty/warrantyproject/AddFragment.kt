package com.warranty.warrantyproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.warranty.warrantyproject.databinding.FragmentAddBinding

class AddFragment : Fragment() {

    private lateinit var binding : FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(inflater,container,false)

        binding.addCompAddScreen.setOnClickListener {
            goBackHome(it)
        }

        binding.returnCompAddScreen.setOnClickListener {
            goBackHome(it)
        }

        return binding.root
    }

    fun goBackHome(view:View) {
        view.findNavController().navigate(R.id.action_addFragment_to_homeFragment)
    }
}