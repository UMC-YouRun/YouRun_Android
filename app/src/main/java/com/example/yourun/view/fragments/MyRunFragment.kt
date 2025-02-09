package com.example.yourun.view.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.yourun.databinding.FragmentMyrunBinding
import com.example.yourun.model.network.ApiClient
import com.example.yourun.model.repository.MyRunRepository
import com.example.yourun.view.activities.MyRun2Activity
import com.example.yourun.viewmodel.MyRunViewModel
import com.example.yourun.viewmodel.MyRunViewModelFactory

class MyRunFragment : Fragment() {
    private var _binding: FragmentMyrunBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MyRunViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyrunBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository = MyRunRepository(ApiClient.getApiService())
        val factory = MyRunViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[MyRunViewModel::class.java]

        viewModel.fetchMyRunData()

        viewModel.userInfo.observe(viewLifecycleOwner) { userInfo ->
            userInfo?.let {
                binding.tvMyrunName.text = it.nickname
                binding.tvMyrunTag.text = it.tags.joinToString (", ")
                binding.txtCrewReward1.text = it.crewReward.toString()
                binding.txtSoloReward.text = it.personalReward.toString()
                binding.txtMvp.text = it.mvp.toString()
            }
        }

        binding.btnEdit.setOnClickListener {
            val intent = Intent(activity, MyRun2Activity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
