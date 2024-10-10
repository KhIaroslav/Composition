package com.khiaroslav.composition.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.khiaroslav.composition.databinding.FragmentGameBinding
import com.khiaroslav.composition.domain.entity.GameResult

class GameFragment : Fragment() {

    private val args by navArgs<GameFragmentArgs>()

    private val viewModelFactory by lazy {
        GameViewModelFactory(args.level, requireActivity().application)
    }

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[GameFragmentViewModel::class.java]
    }
    /*
    private val tvOptions by lazy {
    mutableListOf(
    binding.tvOption1,
    binding.tvOption2,
    binding.tvOption3,
    binding.tvOption4,
    binding.tvOption5,
    binding.tvOption6
    )
    }
    */

    private var _binding: FragmentGameBinding? = null
    private val binding: FragmentGameBinding
        get() = _binding ?: throw RuntimeException("FragmentGameBinding == null")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        return binding.root
    }

    /*
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    parseArgs()
    }
    */

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        observeViewModel()
        /*
        setClickListenersToOptions()
        with(binding) {
        viewModel.startGame(level)
        }
        */
    }
    /*
    private fun setClickListenersToOptions() {
    tvOptions.forEach { textView ->
    textView.setOnClickListener {
    viewModel.chooseAnswer(textView.text.toString().toInt())
    }
    }
    }
    */

    private fun observeViewModel() //= with(binding) {
    {
        /*
        viewModel.question.observe(viewLifecycleOwner) {
        binding.tvSum.text = String.format(Locale.getDefault(), "%s", it.sum)
        binding.tvLeftNumber.text = String.format(Locale.getDefault(), "%s", it.visibleNumber)
        tvOptions.forEachIndexed { i, v ->
        v.text = String.format(Locale.getDefault(), "%s", it.options[i])
        }
        }
        viewModel.percentOfRighrAnswers.observe(viewLifecycleOwner) {
        binding.progressBar.setProgress(it, true)
        }
        viewModel.enoughCount.observe(viewLifecycleOwner) {
        binding.tvAnswersProgress.setTextColor(
        getColorByState(it)
        )
        }
        viewModel.enoughPercent.observe(viewLifecycleOwner) {
        val color = getColorByState(it)
        binding.progressBar.progressTintList = ColorStateList.valueOf(color)
        }
        viewModel.formattedTime.observe(viewLifecycleOwner) {
        tvTimer.text = it
        }
        viewModel.minPercent.observe(viewLifecycleOwner) {
        binding.progressBar.secondaryProgress = it
        }
        */
        viewModel.gameResult.observe(viewLifecycleOwner) {
            launchGameFinishedFragment(it)
        }
        /*
        viewModel.progressAnswer.observe(viewLifecycleOwner) {
        binding.tvAnswersProgress.text = it
        }
        */
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun launchGameFinishedFragment(gameResult: GameResult) {
        /*
        val fragment = GameFinishFragment.newInstance(gameResult)
        requireActivity().supportFragmentManager
        .beginTransaction()
        .replace(R.id.main_container, fragment)
        .addToBackStack(null)
        .commit()
        val args = Bundle().apply {
        putParcelable(KEY_GAME_RESULT, gameResult)
        }
        */
        findNavController().navigate(
            /* R.id.action_gameFragment_to_gameFinishFragment, args */
            GameFragmentDirections.actionGameFragmentToGameFinishFragment(gameResult)
        )
    }
    /*
    private fun parseArgs() {
    level = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.TIRAMISU) {
    requireArguments().getParcelable(KEY_LEVEL, Level::class.java)
    } else {
    requireArguments().getParcelable(KEY_LEVEL) as? Level
    } ?: throw RuntimeException("Level is null")
    }
    companion object {

    const val NAME = "GameFragment"
    const val KEY_LEVEL = "level"

    fun newInstance(level: Level): GameFragment {
    return GameFragment().apply {
    arguments = Bundle().apply {
    putParcelable(KEY_LEVEL, level)
    }
    }
    }
    }
    */
}