package com.warranty.warrantyproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.warranty.warrantyproject.databinding.FragmentHomeBinding
import com.warranty.warrantyproject.infrastructures.db.WarrantyDatabase
import com.warranty.warrantyproject.presenters.HomePresenter
import com.warranty.warrantyproject.presenters.views.CanCreateHomeView
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModel
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModelFactory

class HomeFragment : Fragment(),CanCreateHomeView {

    private lateinit var binding : FragmentHomeBinding
    private lateinit var presenter: HomePresenter
    private lateinit var adapter: WarrantyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        val viewModel = ViewModelProvider(requireActivity(), WarrantyViewModelFactory(
            WarrantyDatabase.getDatabase(requireContext()).warrantyDao())
        )[WarrantyViewModel::class.java]

        presenter = HomePresenter(this,viewModel)


        binding.addCompHomeScreen.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        val recyclerView = binding.warrantiesView
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        adapter = WarrantyAdapter()
        recyclerView.adapter = adapter
        displayWarranties()
    }

    private fun displayWarranties() {
        presenter.getWarrantyList(requireActivity(),adapter)
    }
}