package com.khiaroslav.composition.presentation

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.fragment.findNavController
import com.khiaroslav.composition.R
import com.khiaroslav.composition.databinding.FragmentGameFinishBinding
import com.khiaroslav.composition.domain.entity.GameResult
import java.util.Locale

class GameFinishFragment : Fragment() {

    private lateinit var gameResult: GameResult

    private var _binding: FragmentGameFinishBinding? = null
    private val binding: FragmentGameFinishBinding
        get() = _binding ?: throw RuntimeException("FragmentGameFinishBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseArgs()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameFinishBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupClickListener()
        bindViews()
    }

    private fun setupClickListener() = with(binding) {
//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                retryGame()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
        buttonRetry.setOnClickListener {
            retryGame()
        }
    }

    private fun bindViews() = with(binding) {
        emojiResult.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                getSmileResId()
            )
        )
        tvRequiredAnswers.text = String.format(
            Locale.getDefault(),
            ContextCompat.getString(requireContext(), R.string.required_score),
            gameResult.gameSettings.minCountOfRightAnswers
        )
        tvScoreAnswers.text = String.format(
            Locale.getDefault(),
            ContextCompat.getString(requireContext(), R.string.score_answers),
            gameResult.countOfRightAnswers
        )
        tvRequiredPercentage.text = String.format(
            Locale.getDefault(),
            ContextCompat.getString(requireContext(), R.string.required_percentage),
            gameResult.gameSettings.minPercentOfRightAnswers
        )
        tvScorePercentage.text = String.format(
            Locale.getDefault(),
            ContextCompat.getString(requireContext(), R.string.score_percentage),
            getPercentOfRightAnswers()
        )
    }

    private fun getSmileResId(): Int {
        return if (gameResult.winner) {
            R.drawable.ic_smile
        } else {
            R.drawable.ic_sad
        }
    }

    private fun getPercentOfRightAnswers() = with(gameResult) {
        if (countOfQuestions == 0) {
            0
        } else {
            ((countOfRightAnswers / countOfQuestions.toDouble()) * 100).toInt()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseArgs() {
        gameResult = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
            requireArguments().getParcelable(KEY_GAME_RESULT, GameResult::class.java)
        } else {
            requireArguments().getParcelable(KEY_GAME_RESULT) as? GameResult
        } ?: throw RuntimeException("Level is null")
    }

    private fun retryGame() {
        findNavController().popBackStack()
//        requireActivity().supportFragmentManager.popBackStack(
//            GameFragment.NAME,
//            FragmentManager.POP_BACK_STACK_INCLUSIVE
//        )
    }

    companion object {
        const val KEY_GAME_RESULT = "game_result"

        fun newInstance(gameResult: GameResult): GameFinishFragment {
            return GameFinishFragment().apply {
                arguments = Bundle().apply {
                    // putSerializable(KEY_GAME_RESULT, gameResult)
                    putParcelable(KEY_GAME_RESULT, gameResult)
                }
            }
        }
    }
}