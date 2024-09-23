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

/**
 * Fragment for searching and displaying contacts.
 */
@AndroidEntryPoint
class SearchFragment :
    BaseFragment<FragmentSearchBinding>(FragmentSearchBinding::inflate) {

    /**
     * ViewModel for managing search operations.
     */
    private val viewModel: SearchViewModel by viewModels()

    /**
     * Adapter for displaying search results.
     * Uses a [SearchActionListener] to handle clicks on a contact.
     */
    private val adapter: SearchAdapter by lazy {
        SearchAdapter(object : SearchActionListener {
            override fun onClick(contact: Contact, position: Int) {
                val action = SearchFragmentDirections.actionSearchFragmentToDetailViewFragment(contact)
                findNavController().navigate(action)
            }
        })
    }

    /**
     * A method that is called after the fragment view is created.
     * Configures the RecyclerView, listeners, and observers.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRecyclerView()
        setListeners()
        setObservers()
    }

    /**
     * Configures a RecyclerView with a LinearLayoutManager and adapter.
     */
    private fun setRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewContacts.layoutManager = layoutManager
        binding.recyclerViewContacts.adapter = adapter
    }

    /**
     * Configures event listeners for the search text field and the delete button.
     * Starts a search when text is entered
     * and returns to the previous screen when the delete button is pressed.
     */
    override fun setListeners() {
        binding.textInputLayoutSearch.editText?.addTextChangedListener {
            val query = it.toString()
            viewModel.searchContacts(query)
        }
        binding.imageButtonSearchErase.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    /**
     * Configures watchers for search results.
     * Updates the adapter and shows or hides the message about no results.
     */
    private fun setObservers() {
        viewModel.contacts.observe(viewLifecycleOwner, Observer { contacts ->
            adapter.submitList(contacts)
            if (contacts.isEmpty()) {
                binding.textViewSearchNoResults.visible()
                binding.textViewSearchSeeMore.visible()
            } else {
                binding.textViewSearchNoResults.gone()
                binding.textViewSearchSeeMore.gone()
            }
        })
    }
}