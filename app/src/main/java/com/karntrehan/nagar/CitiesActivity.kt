package com.karntrehan.nagar

import android.arch.lifecycle.LifecycleActivity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.karntrehan.nagar.databinding.ActivityCitiesBinding


class CitiesActivity : LifecycleActivity() {

    /*@Inject
    lateinit var cityDao: CityDao*/

    lateinit var binding: ActivityCitiesBinding
    lateinit var citiesViewModel: CitiesViewModel
    internal lateinit var citiesAdapter: CitiesAdapter
    lateinit var context: Context
    lateinit var layoutManager: LinearLayoutManager
    var canLoadMore = true

    //private val logger = KotlinLogging.logger {}
    val TAG = "CitiesActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cities)
        binding.rvCities.setHasFixedSize(true)
        layoutManager = binding.rvCities.layoutManager as LinearLayoutManager

        context = this

        citiesAdapter = CitiesAdapter(context)
        binding.rvCities.adapter = citiesAdapter

        citiesViewModel = ViewModelProviders.of(this).get(CitiesViewModel::class.java)

        binding.rvCities.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (citiesAdapter.itemCount > 1 && lastPosition == citiesAdapter.itemCount - 1
                        && canLoadMore) {
                    Log.d(TAG, "Load more $lastPosition")
                    citiesViewModel.setInput(lastPosition)
                    canLoadMore = false
                }
            }
        })
        citiesViewModel.setInput(0)
        citiesViewModel.cities.observe(this, Observer { citiesList ->
            val lastVisi = layoutManager.findLastVisibleItemPosition()

            Log.d(TAG, "Got cities: $citiesList")
            if (citiesList != null && citiesList.isNotEmpty()) {
                canLoadMore = true
                citiesAdapter.addItems(lastVisi, citiesList)
            }
        })


        binding.srlCities.setOnRefreshListener({
            Log.d(TAG, "Refresh")
            citiesAdapter.clearAll()
            citiesViewModel.refreshDb()
        })

    }
}
