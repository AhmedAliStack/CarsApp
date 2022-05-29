package com.example.carsapp.features.presentation.main

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.work.WorkInfo
import com.example.carsapp.databinding.ActivityMainBinding
import com.example.carsapp.features.domain.model.CarsModel
import com.example.carsapp.features.domain.viewstate.RequestState
import com.example.carsapp.features.presentation.main.adapter.CarsAdapter
import com.example.carsapp.features.presentation.utils.PaginationScrollListener
import com.example.carsapp.features.presentation.utils.observeConnection
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var carsAdapter: CarsAdapter
    lateinit var binding: ActivityMainBinding
    private val mainViewModel: MainViewModel by viewModels()
    private val carsList: ArrayList<CarsModel.Data> = arrayListOf()
    var page = 1
    private var isLastPage = false
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        //init work manager observer
        initWorkManager(binding)
        observeViewModel()
    }

    private fun initWorkManager(binding: ActivityMainBinding) {
        //detect connection
        observeConnection(this).observe(this) { info ->
            Log.d("NetworkInfo",info.state.toString())
            if (info.state == WorkInfo.State.FAILED||info.state == WorkInfo.State.ENQUEUED) {
                binding.apply {
                    tvError.text = "Internet Connection Required"
                    tvError.visibility = View.VISIBLE
                    rvCars.visibility  = View.GONE
                }
            } else if (info.state == WorkInfo.State.SUCCEEDED) {
                binding.apply {
                    tvError.text = "Internet Connection Required"
                    tvError.visibility = View.GONE
                    rvCars.visibility  = View.VISIBLE
                }
               mainViewModel.getCars(page)
            }
        }
    }

    private fun initViews() {
        carsAdapter = CarsAdapter(carsList)
        binding.apply {
            swiperefresh.setOnRefreshListener {
                swiperefresh.isRefreshing = true
                isLoading = true
                page = 1
                carsList.clear()
                mainViewModel.getCars(page)
            }
            rvCars.adapter = carsAdapter
            val layoutManager = LinearLayoutManager(this@MainActivity)
            rvCars.layoutManager = layoutManager
            rvCars.addOnScrollListener(object : PaginationScrollListener(layoutManager) {
                override fun loadMoreItems() {
                    isLoading = true
                    page++
                    mainViewModel.getCars(page)
                }

                override fun isLastPage(): Boolean {
                    return isLastPage
                }

                override fun isLoading(): Boolean {
                    return isLoading
                }
            })
            carsAdapter.notifyDataSetChanged()
        }
    }

    private fun observeViewModel() {
        lifecycleScope.launchWhenCreated {
            mainViewModel.requestState.collect {
                when (it) {
                    is RequestState.Cars -> {
                        binding.swiperefresh.isRefreshing = false
                        isLoading = false
                        extractCars(it.cars)
                    }
                    is RequestState.Error -> {
                        displayError(it.error)
                    }
                    RequestState.Idle -> {

                    }
                    RequestState.Loading -> {
                        binding.swiperefresh.isRefreshing = true
                        isLoading = true
                    }
                }
            }
        }
    }

    private fun displayError(error: String?) {
        binding.apply {
            tvError.visibility = View.VISIBLE
            tvError.text = error
            rvCars.visibility = View.GONE
        }
    }

    private fun extractCars(cars: CarsModel) {
        isLastPage = cars.data.isEmpty()
        carsList.addAll(cars.data)
        carsAdapter.notifyDataSetChanged()
    }
}