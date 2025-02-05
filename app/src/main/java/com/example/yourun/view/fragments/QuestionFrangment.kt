package com.example.yourun.view.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.yourun.R
import com.example.yourun.databinding.FragmentQuestionBinding
import com.example.yourun.model.repository.QuestionRepository
import com.example.yourun.view.adapters.QuestionPagerAdapter
import com.example.yourun.viewmodel.QuestionViewModel

class QuestionFragment : Fragment(R.layout.fragment_question) {
    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!
    private val questionViewModel: QuestionViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.topBar.txtTopBarWithBackButton.text = "러닝테스트"

        val questions = QuestionRepository.questions
        val adapter = QuestionPagerAdapter(questions) { _, score ->
            questionViewModel.addScore(score)
        }

        binding.viewPager.adapter = adapter

        binding.nextButton.setOnClickListener {
            val nextItem = binding.viewPager.currentItem + 1
            if (nextItem < questions.size) {
                binding.viewPager.currentItem = nextItem
            } else {
                val finalScore = questionViewModel.score.value ?: 0

                val bundle = Bundle().apply {
                    putInt("FINAL_SCORE", finalScore)
                }
                findNavController().navigate(R.id.action_questionFragment_to_resultFragment, bundle)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
