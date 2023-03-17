package com.warranty.warrantyproject

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.warranty.warrantyproject.databinding.FragmentHomeBinding
import com.warranty.warrantyproject.infrastructures.db.WarrantyDatabase
import com.warranty.warrantyproject.infrastructures.db.WarrantyEntity
import com.warranty.warrantyproject.presenters.HomePresenter
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModel
import com.warranty.warrantyproject.presenters.viewmodel.WarrantyViewModelFactory
import com.warranty.warrantyproject.presenters.views.CanCreateHomeView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment(),CanCreateHomeView, OnWarrantyClickListener{

    private lateinit var binding : FragmentHomeBinding
    private lateinit var presenter: HomePresenter
    private lateinit var adapter: WarrantyAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater,container,false)

        binding.searchMaterial.visibility = View.GONE

        val viewModel = ViewModelProvider(requireActivity(), WarrantyViewModelFactory(
            WarrantyDatabase.getDatabase(requireContext()).warrantyDao())
        )[WarrantyViewModel::class.java]

        presenter = HomePresenter(this,viewModel)

        binding.addCompHomeScreen.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_addFragment)
        }

        binding.searchComp.setOnClickListener {
            if(binding.searchMaterial.visibility == View.GONE){
                binding.searchMaterial.visibility = View.VISIBLE
            }else{
                binding.searchMaterial.visibility = View.GONE
                displayWarranties()
                adapter.notifyDataSetChanged()
            }
        }

        binding.searchMaterialEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // ...
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Lorsque le texte change, vous pouvez appeler la méthode updateList() de votre adaptateur
                // avec une nouvelle liste de garanties qui correspondent à la recherche
                //Si la taille est +0 alors on affiche la liste sinon on affiche la liste de base
                if(s.toString().length > 0){
                    val searchText = s.toString()
                    val warranties = getWarrantiesMatchingSearch(searchText)
                    adapter.setList(warranties)
                    adapter.notifyDataSetChanged()
                }else{
                    displayWarranties()
                    adapter.notifyDataSetChanged()
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // ...
            }
        })
        binding.root.setOnClickListener {
            binding.searchMaterial.visibility = View.GONE
            displayWarranties()
            adapter.notifyDataSetChanged()
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
    private fun getWarrantiesMatchingSearch(searchText: String): List<WarrantyEntity> {
        val warranties = presenter.getWarranties()
        val warrantiesMatchingSearch = mutableListOf<WarrantyEntity>()
        if(warranties.isNotEmpty()){
            for (warranty in warranties) {
                if (warranty.title.contains(searchText, ignoreCase = true)) {
                    warrantiesMatchingSearch.add(warranty)
                    break
                }
                if (warranty.summary.contains(searchText, ignoreCase = true)) {
                    warrantiesMatchingSearch.add(warranty)
                    break
                }
                if (warranty.shopName.contains(searchText, ignoreCase = true)) {
                    warrantiesMatchingSearch.add(warranty)
                    break
                }
                if(searchText.toDoubleOrNull() != null && warranty.price == searchText.toDouble()){
                    warrantiesMatchingSearch.add(warranty)
                    break
                }

                try {
                    //Je veux un string de ma date dd/mm/yyyy
                    val dateOfExpiry : String = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(warranty.dateOfExpiry)
                    if (dateOfExpiry.contains(searchText, ignoreCase = true)) {
                        warrantiesMatchingSearch.add(warranty)
                        break
                    }
                    val dateOfPurchase : String? =
                        warranty.dateOfPurchase?.let {
                            SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(
                                it
                            )
                        }
                    if (dateOfPurchase != null && dateOfPurchase.contains(searchText, ignoreCase = true)) {
                        warrantiesMatchingSearch.add(warranty)
                        break
                    }
                } catch (e: ParseException) {
                    // La chaîne n'est pas une date valide
                }
            }

        }
        return warrantiesMatchingSearch
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