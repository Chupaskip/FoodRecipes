package com.example.foodrecipes.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.viewbinding.ViewBinding
import com.example.foodrecipes.R
import com.example.foodrecipes.ui.FoodViewModel

abstract class BaseFragment<VB : ViewBinding> : Fragment() {
    private var _binding: VB? = null
    val binding
        get() = _binding!!

    protected var swipeRefresher: SwipeRefreshLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = viewBinding
        swipeRefresher = activity?.findViewById<SwipeRefreshLayout>(R.id.swipe_refresher_main)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (swipeRefresher == null)
            Toast.makeText(requireContext(), "Hello", Toast.LENGTH_SHORT).show()
        swipeRefresher?.setOnRefreshListener { setOnScrollRefresher() }
    }

    abstract val viewBinding: VB

    protected val viewModel: FoodViewModel by activityViewModels()

    protected fun throwErrorResponseWithToast(
        nameOfData: String,
        errorMessage: String?,
    ) {
        Toast.makeText(requireContext(),
            getString(R.string.error_response, nameOfData, errorMessage),
            Toast.LENGTH_SHORT).show()
    }


    open fun setOnScrollRefresher(){}
}