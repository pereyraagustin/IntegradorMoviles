package com.iua.agustinpereyra.view.base

import android.os.Bundle
import android.view.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.iua.agustinpereyra.R
import com.iua.agustinpereyra.controller.CattleManager
import com.iua.agustinpereyra.controller.NetworkHelper
import com.iua.agustinpereyra.controller.viewmodel.BaseCattleViewModel
import com.iua.agustinpereyra.controller.viewmodel.CattleViewModel
import com.iua.agustinpereyra.databinding.FragmentCattleListBinding
import com.iua.agustinpereyra.view.cattleviews.CattleCardRecyclerViewAdapter

/**
 * BaseCattleListFragment is the base fragment used to show lists of cattle data.
 * In principle it is used to shared boilerplate code that would be the same
 * for the views of Monitored Cattle and All Cattle.
 * It sets the layout, observers the viewModel cattleList, sets the
 * recycler view and notifies in case there is no Internet connection when opened.
 */
abstract class BaseCattleListFragment : FilterableCattleRecyclerFragment(){

    protected lateinit var viewModel: BaseCattleViewModel
    protected lateinit var listener: CattleListFragmentListener

    /**
     * onCreateView sets the layout and other boilerplate code.
     * It should be called at the end of the child's as a return value
     * if overwritten. Inheriting classes have to set the recyclerViewAdapter variable
     * before calling this onCreateView, with the corresponding baseCattleList
     * (the global inherited variable), and listener.
     */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentCattleListBinding.inflate(inflater, container, false)

        // Get Activity with needed functions
        listener = activity as CattleListFragmentListener

        // Set up the toolbar corresponding title
        listener.setActionBarTitle(getString(R.string.cattle_list_title))


        // Set up the recycler
        currentCattleList = baseCattleList
        val viewManager = LinearLayoutManager(context)

        val recyclerView = fragmentBinding.cattleListRecycler
        recyclerView.apply {
            layoutManager = viewManager
            adapter = recyclerViewAdapter
        }

        // Observe and update UI and local variable
        viewModel.cattleList.observe(viewLifecycleOwner, Observer { newCattle ->
            baseCattleList = newCattle
            // Order showed cattle list based on preferences
            currentCattleList =  CattleManager.orderBasedOnPreferences(baseCattleList, context)
            recyclerViewAdapter.setCattle(currentCattleList)
        })

        // Check for Internet and notify no conection available
        if (!NetworkHelper.isNetworkConnected(context)) {
            listener.notifyNoInternet()
        }

        return fragmentBinding.root
    }

    interface CattleListFragmentListener : ActionBarModifier {
        fun navigateToSpecificBovine(caravan: String)
        fun notifyNoInternet()
    }
}