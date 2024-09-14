package com.example.myprofile.presentation.ui.fragments.search

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.FragmentSearchBinding
import com.example.myprofile.presentation.ui.base.BaseFragment
import com.example.myprofile.presentation.ui.fragments.search.adapter.SearchAdapter
import com.example.myprofile.presentation.ui.fragments.search.adapter.interfaces.SearchActionListener
import com.example.myprofile.presentation.utils.ext.gone
import com.example.myprofile.presentation.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment :
    BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    private val viewModel: SearchViewModel by viewModels()
    private val adapter: SearchAdapter by lazy {
        SearchAdapter(object : SearchActionListener {
            // Event handler for viewing contact details
            override fun onClick(contact: Contact, position: Int) {
                val action = SearchFragmentDirections.actionSearchFragmentToDetailViewFragment(contact)
                findNavController().navigate(action)
            }
        })
    }

    /**
     * Method called after the fragment's view is created
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setListeners()
        setObservers()
    }

    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewContacts.layoutManager = layoutManager
        binding.recyclerViewContacts.adapter = adapter
    }

    override fun setListeners() {
        binding.textInputLayoutSearch.editText?.addTextChangedListener {
            val query = it.toString()
            viewModel.searchContacts(query)
        }
        binding.imageButtonSearchErase.setOnClickListener {
            navController.navigateUp()
        }
    }

    private fun setObservers() {
        viewModel.contacts.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            if (viewModel.contacts.value?.isEmpty() == true) {
                binding.textViewSearchNoResults.visible()
                binding.textViewSearchSeeMore.visible()
            } else {
                binding.textViewSearchNoResults.gone()
                binding.textViewSearchSeeMore.gone()
            }
        })
    }
}