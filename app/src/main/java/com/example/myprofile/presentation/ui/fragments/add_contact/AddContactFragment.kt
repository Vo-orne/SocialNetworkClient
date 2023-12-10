package com.example.myprofile.presentation.ui.fragments.add_contact

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myprofile.R
import com.example.myprofile.data.model.Contact
import com.example.myprofile.databinding.FragmentAddContactBinding
import com.example.myprofile.presentation.ui.base.BaseFragment
import com.example.myprofile.presentation.ui.fragments.add_contact.adapter.AddContactsAdapter
import com.example.myprofile.presentation.ui.fragments.add_contact.adapter.interfaces.AddContactActionListener
import com.example.myprofile.presentation.utils.ext.navigateToFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddContactFragment : BaseFragment<FragmentAddContactBinding>(FragmentAddContactBinding::inflate) {

    private val viewModel: AddContactViewModel by viewModels()

    private val adapter: AddContactsAdapter by lazy {
        AddContactsAdapter(listener = object : AddContactActionListener {

            override fun onClickAddButton(contact: Contact, position: Int) {
                viewModel.selectContact(contact)
            }

//            override fun onClickContact(
//                contact: Contact,
//                transitionPairs: Array<Pair<View, String>>
//            ) {
//                val extras = FragmentNavigatorExtras(*transitionPairs)
//                val direction =
//                    AddContactsFragmentDirections.actionAddContactsFragmentToContactProfile(
//                        !viewModel.supportList.contains(contact), contact
//                    )
//                navController.navigate(direction, extras)
//            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    }

    override fun setListeners() {
        with(binding) {
            imageButtonAddContactBack.setOnClickListener {
                viewModel.addContacts()
                navigateToFragment(R.id.action_addContactFragment_to_pagerFragment)
            }
//            imageSearchView.setOnClickListener {
//                searchView()
//            }
        }
    }

//    private fun searchView() {
//        viewModel.showNotification(requireContext())
//    }
}