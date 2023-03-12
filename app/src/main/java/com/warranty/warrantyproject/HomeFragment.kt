package com.warranty.warrantyproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.warranty.warrantyproject.databinding.FragmentHomeBinding
import com.warranty.warrantyproject.infrastructures.db.WarrantyDatabase
import com.warranty.warrantyproject.presenters.HomePresenter
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModel
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModelFactory
import com.warranty.warrantyproject.presenters.views.CanCreateHomeView

class HomeFragment : Fragment(),CanCreateHomeView, OnWarrantyClickListener{

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
        val layoutManager = GridLayoutManager(context, 2)
        recyclerView.layoutManager = layoutManager
        val listener = this
        adapter = WarrantyAdapter(listener)
        recyclerView.adapter = adapter
        displayWarranties()
    }

    private fun displayWarranties() {
        presenter.getWarrantyList(requireActivity(),adapter)
    }

    override fun onItemClick(position: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToLookFragment()
        presenter.setCurrentWarranty(position)
        binding.root.findNavController().navigate(action)
    }
}