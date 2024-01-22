package com.example.myprofile.presentation.ui.fragments.add_contact

import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprofile.R
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.FragmentAddContactBinding
import com.example.myprofile.domain.ApiState
import com.example.myprofile.presentation.ui.base.BaseFragment
import com.example.myprofile.presentation.ui.fragments.add_contact.adapter.AddContactsAdapter
import com.example.myprofile.presentation.ui.fragments.add_contact.adapter.interfaces.AddContactActionListener
import com.example.myprofile.presentation.utils.ext.invisible
import com.example.myprofile.presentation.utils.ext.log
import com.example.myprofile.presentation.utils.ext.navigateToFragment
import com.example.myprofile.presentation.utils.ext.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactFragment : BaseFragment<FragmentAddContactBinding>(FragmentAddContactBinding::inflate) {

    private val viewModel: AddContactViewModel by viewModels()
    private lateinit var progressBar: ProgressBar

    private val adapter: AddContactsAdapter by lazy {
        AddContactsAdapter(listener = object : AddContactActionListener {

            override fun onClickAddButton(contact: Contact, position: Int) {
                viewModel.addContact(contact)
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressBar = binding.progressBar
        setRecyclerView()
        setObserves()
        setListeners()
    }

    private fun setRecyclerView() {
        viewModel.getAllUsers()
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewAddContactUsers.layoutManager = layoutManager
        binding.recyclerViewAddContactUsers.adapter = adapter
    }


    private fun setObserves() {
        lifecycleScope.launch {
            viewModel.contactsToAdd.observe(viewLifecycleOwner, Observer {
                adapter.submitList(it)
            })
        }
        lifecycleScope.launch {
            viewModel.allUsersLiveData.observe(viewLifecycleOwner, Observer {apiState ->
                when (apiState) {
                    is ApiState.Success<*> -> {
                        progressBar.invisible()
                    }
                    is ApiState.Error -> {
                        progressBar.invisible()
                        log(apiState.error)
                    }
                    is ApiState.Initial -> {
                        progressBar.invisible()
                        log(apiState)
                    }
                    is ApiState.Loading -> {
                        progressBar.visible()
                        log(apiState)
                    }
                }
            })
        }
        lifecycleScope.launch {
            viewModel.states.observe(viewLifecycleOwner, Observer {
                adapter.setStates(it)
            })
        }
    }

    override fun setListeners() {
        with(binding) {
            imageButtonAddContactBack.setOnClickListener {
                navigateToFragment(R.id.action_addContactFragment_to_pagerFragment)
            }
        }
    }
}