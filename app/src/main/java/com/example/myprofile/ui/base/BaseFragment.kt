package com.example.myprofile.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding

/**
 * The BaseFragment abstract class serves as a base for all fragments in the application.
 * @param VBinding The type of ViewBinding associated with the fragment.
 * @param inflaterMethod The method to inflate the ViewBinding for the fragment.
 * Provides functionality to handle ViewBinding and basic lifecycle management for fragments.
 * Subclasses should implement the setListeners() method to set up listeners and handle UI events.
 */
abstract class BaseFragment<VBinding : ViewBinding>(
    private val inflaterMethod: (LayoutInflater, ViewGroup?, Boolean) -> VBinding
) : Fragment() {

    private var _binding: VBinding? = null
    val binding get() = requireNotNull(_binding)

    val navController: NavController
        get() = findNavController()
    /**
     * Called when the fragment's view is created.
     * Inflates the ViewBinding layout and returns the root view.
     * @param inflater The layout inflater to inflate the ViewBinding layout.
     * @param container The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState The saved state of the fragment, if any.
     * @return The root view of the fragment's layout.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflaterMethod.invoke(inflater, container, false)
        return binding.root
    }
    /**
     * Called when the fragment's view is destroyed.
     * Clears the ViewBinding reference to avoid memory leaks.
     */
    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
    /**
     * Abstract method to be implemented by subclasses.
     * Used to set up listeners and handle UI events specific to the fragment.
     */
    abstract fun setListeners()
}