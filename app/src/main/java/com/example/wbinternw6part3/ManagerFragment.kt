package com.example.wbinternw6part3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.wbinternw6part3.databinding.FragmentManagerBinding
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlin.random.Random

class ManagerFragment : Fragment() {

    private var seconds = 0
    private var minutes = 0
    private var hours = 0
    private var counter = 0

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentManagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentManagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.pause.setOnClickListener {
            viewModel.applyNewState("pause")
            lifecycleScope.coroutineContext.cancelChildren()
        }

        binding.start.setOnClickListener {
            viewModel.applyNewState("start")
            lifecycleScope.coroutineContext.cancelChildren()
            lifecycleScope.launch {
                renderData()
            }
        }

        binding.restart.setOnClickListener {
            lifecycleScope.coroutineContext.cancelChildren()
            counter = 0
            minutes = 0
            seconds = 0
            binding.timer.text = "00:00:00"
            viewModel.applyNewState("restart")
            lifecycleScope.launch {
                renderData()
            }
        }
    }

    private suspend fun renderData() = withContext(Dispatchers.Main) {
        while (true) {
            calculateTime().collect {
                binding.timer.text = it
            }

            if (counter % 20 == 0) {
                val randomColor = -Random.nextInt(255 * 255 * 255)
                binding.container.setBackgroundColor(randomColor)
            }
            counter++
            delay(1000)
        }
    }

    private fun calculateTime(): Flow<String> = flow {
        seconds = counter % 60
        minutes = counter / 60
        hours = minutes / 60

        val time = String.format("%02d:%02d:%02d", hours, minutes, seconds)
        emit(time)
    }.flowOn(Dispatchers.Default)

}