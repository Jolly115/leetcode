package com.example.flow

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    
    private lateinit var viewModel: FlowDemoViewModel
    
    // StateFlow Counter Views
    private lateinit var tvCounter: TextView
    private lateinit var btnIncrement: Button
    private lateinit var btnDecrement: Button
    private lateinit var btnReset: Button
    
    // Basic Flow Card
    private lateinit var basicFlowCard: FlowDemoCardHolder
    
    // Transform Card
    private lateinit var transformCard: FlowDemoCardHolder
    
    // Filter Card
    private lateinit var filterCard: FlowDemoCardHolder
    
    // SharedFlow Views
    private lateinit var btnEmitShared: Button
    private lateinit var btnClearShared: Button
    private lateinit var rvSharedFlow: RecyclerView
    private lateinit var sharedFlowAdapter: FlowDataAdapter
    
    // Combine Operator Views
    private lateinit var btnCombine: Button
    private lateinit var tvCombineResult: TextView
    
    // Zip Card
    private lateinit var zipCard: FlowDemoCardHolder
    
    // FlatMap Card
    private lateinit var flatMapCard: FlowDemoCardHolder
    
    // Distinct Card
    private lateinit var distinctCard: FlowDemoCardHolder
    
    // Advanced Operators
    private lateinit var btnTake: Button
    private lateinit var btnErrorHandling: Button
    private lateinit var btnBuffer: Button
    
    // Clear All
    private lateinit var btnClearAll: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        
        viewModel = ViewModelProvider(this)[FlowDemoViewModel::class.java]
        
        initViews()
        setupListeners()
        observeViewModel()
    }
    
    private fun initViews() {
        // StateFlow Counter
        tvCounter = findViewById(R.id.tvCounter)
        btnIncrement = findViewById(R.id.btnIncrement)
        btnDecrement = findViewById(R.id.btnDecrement)
        btnReset = findViewById(R.id.btnReset)
        
        // Basic Flow Card
        basicFlowCard = FlowDemoCardHolder(
            findViewById(R.id.basicFlowCard),
            "Basic Flow (Cold)",
            "Cold flow - starts fresh for each collector"
        )
        
        // Transform Card
        transformCard = FlowDemoCardHolder(
            findViewById(R.id.transformCard),
            "Map Operator",
            "Transforms each emitted value"
        )
        
        // Filter Card
        filterCard = FlowDemoCardHolder(
            findViewById(R.id.filterCard),
            "Filter Operator",
            "Filters values based on condition"
        )
        
        // SharedFlow
        btnEmitShared = findViewById(R.id.btnEmitShared)
        btnClearShared = findViewById(R.id.btnClearShared)
        rvSharedFlow = findViewById(R.id.rvSharedFlow)
        sharedFlowAdapter = FlowDataAdapter()
        rvSharedFlow.apply {
            adapter = sharedFlowAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }
        
        // Combine Operator
        btnCombine = findViewById(R.id.btnCombine)
        tvCombineResult = findViewById(R.id.tvCombineResult)
        
        // Zip Card
        zipCard = FlowDemoCardHolder(
            findViewById(R.id.zipCard),
            "Zip Operator",
            "Pairs values from two flows"
        )
        
        // FlatMap Card
        flatMapCard = FlowDemoCardHolder(
            findViewById(R.id.flatMapCard),
            "FlatMapConcat Operator",
            "Flattens nested flows sequentially"
        )
        
        // Distinct Card
        distinctCard = FlowDemoCardHolder(
            findViewById(R.id.distinctCard),
            "DistinctUntilChanged",
            "Emits only when value changes"
        )
        
        // Advanced Operators
        btnTake = findViewById(R.id.btnTake)
        btnErrorHandling = findViewById(R.id.btnErrorHandling)
        btnBuffer = findViewById(R.id.btnBuffer)
        
        // Clear All
        btnClearAll = findViewById(R.id.btnClearAll)
    }
    
    private fun setupListeners() {
        // StateFlow Counter
        btnIncrement.setOnClickListener { viewModel.incrementCounter() }
        btnDecrement.setOnClickListener { viewModel.decrementCounter() }
        btnReset.setOnClickListener { viewModel.resetCounter() }
        
        // Basic Flow
        basicFlowCard.btnStart.setOnClickListener { viewModel.demoBasicFlow() }
        basicFlowCard.btnClear.setOnClickListener { viewModel.clearBasicFlow() }
        
        // Transform Operators
        transformCard.btnStart.setOnClickListener { viewModel.demoTransformOperators() }
        transformCard.btnClear.setOnClickListener { viewModel.clearTransformedData() }
        
        // Filter Operators
        filterCard.btnStart.setOnClickListener { viewModel.demoTransformOperators() }
        filterCard.btnClear.setOnClickListener { viewModel.clearFilteredData() }
        
        // SharedFlow
        btnEmitShared.setOnClickListener {
            viewModel.emitSharedEvent("Event ${System.currentTimeMillis() % 1000}")
        }
        btnClearShared.setOnClickListener { viewModel.clearSharedFlow() }
        
        // Combine Operator
        btnCombine.setOnClickListener { viewModel.demoCombineOperator() }
        
        // Zip Operator
        zipCard.btnStart.setOnClickListener { viewModel.demoZipOperator() }
        
        // FlatMap Operator
        flatMapCard.btnStart.setOnClickListener { viewModel.demoFlatMapOperator() }
        
        // Distinct Operator
        distinctCard.btnStart.setOnClickListener { viewModel.demoDistinctOperator() }
        
        // Advanced Operators
        btnTake.setOnClickListener { viewModel.demoLimitingOperators() }
        btnErrorHandling.setOnClickListener { viewModel.demoErrorHandling() }
        btnBuffer.setOnClickListener { viewModel.demoBufferOperator() }
        
        // Clear All
        btnClearAll.setOnClickListener { viewModel.clearAllData() }
    }
    
    private fun observeViewModel() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect { state ->
                    // Update StateFlow Counter
                    tvCounter.text = state.stateFlowCounter.toString()
                    
                    // Update Basic Flow
                    basicFlowCard.updateData(state.basicFlowData)
                    
                    // Update Transform Data
                    transformCard.updateData(state.transformedData)
                    
                    // Update Filter Data
                    filterCard.updateData(state.filteredData)
                    
                    // Update SharedFlow
                    sharedFlowAdapter.submitList(state.sharedFlowData)
                    rvSharedFlow.visibility = if (state.sharedFlowData.isNotEmpty()) View.VISIBLE else View.GONE
                    
                    // Update Combine Result
                    if (state.combinedData.isNotEmpty()) {
                        tvCombineResult.text = "Combined Result: ${state.combinedData}"
                        tvCombineResult.visibility = View.VISIBLE
                    } else {
                        tvCombineResult.visibility = View.GONE
                    }
                    
                    // Update Zip Data
                    zipCard.updateData(state.zipData)
                    
                    // Update FlatMap Data
                    flatMapCard.updateData(state.flatMapData)
                    
                    // Update Distinct Data
                    distinctCard.updateData(state.distinctData)
                }
            }
        }
    }
    
    inner class FlowDemoCardHolder(
        cardView: View,
        title: String,
        description: String
    ) {
        private val tvTitle: TextView = cardView.findViewById(R.id.tvTitle)
        private val tvDescription: TextView = cardView.findViewById(R.id.tvDescription)
        val btnStart: Button = cardView.findViewById(R.id.btnStart)
        val btnClear: Button = cardView.findViewById(R.id.btnClear)
        private val rvData: RecyclerView = cardView.findViewById(R.id.rvData)
        private val adapter: FlowDataAdapter = FlowDataAdapter()
        
        init {
            tvTitle.text = title
            tvDescription.text = description
            rvData.apply {
                this.adapter = this@FlowDemoCardHolder.adapter
                layoutManager = LinearLayoutManager(this@MainActivity)
            }
        }
        
        fun updateData(data: List<String>) {
            adapter.submitList(data)
            rvData.visibility = if (data.isNotEmpty()) View.VISIBLE else View.GONE
            btnClear.visibility = if (data.isNotEmpty()) View.VISIBLE else View.GONE
        }
    }
}