package com.example.myprofile.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myprofile.R

class DetailViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // We return the root view of the fragment by reading the XML file
        return inflater.inflate(R.layout.fragment_detail_view, container, false)
    }
}