package com.khiaroslav.composition.presentation

import android.content.Context
import android.content.res.ColorStateList
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import com.khiaroslav.composition.R
import com.khiaroslav.composition.domain.entity.GameResult
import java.util.Locale

interface OnOptionClickListener{
    fun onOptionClick(option: Int)
}

@BindingAdapter("requiredAnswers")
fun bindRequiredAnswers(textView: TextView, count: Int){
    textView.text = String.format(
        Locale.getDefault(),
        ContextCompat.getString(textView.context, R.string.required_score),
        count
    )
}

@BindingAdapter("scoreAnswers")
fun bindScoreAnswers(textView: TextView, count: Int){
    textView.text = String.format(
        Locale.getDefault(),
        ContextCompat.getString(textView.context, R.string.score_answers),
        count
    )
}

@BindingAdapter("requiredPercentage")
fun bindRequiredPercentage(textView: TextView, count: Int){
    textView.text = String.format(
        Locale.getDefault(),
        ContextCompat.getString(textView.context, R.string.required_percentage),
        count
    )
}

@BindingAdapter("scorePercentage")
fun bindScorePercentage(textView: TextView, gameResult: GameResult){
    textView.text = String.format(
        Locale.getDefault(),
        ContextCompat.getString(textView.context, R.string.score_percentage),
        getPercentOfRightAnswers(gameResult)
    )
}

@BindingAdapter("emojiResult")
fun bindEmojiResult(imageView: ImageView, state: Boolean){
    imageView.setImageDrawable(
        ContextCompat.getDrawable(
            imageView.context,
            getSmileResId(state)
        )
    )
}

@BindingAdapter("numberAsText")
fun bindNumberAsText(textView: TextView, count: Int){
    textView.text = String.format(Locale.getDefault(), "%s", count)
}

@BindingAdapter("enoughCount")
fun bindEnoughCount(textView: TextView, state: Boolean){
   textView.setTextColor(
        getColorByState(textView.context ,state)
    )
}

@BindingAdapter("enoughPercent")
fun bindEnoughPercent(progressBar: ProgressBar, state: Boolean){
    progressBar.progressTintList = ColorStateList.valueOf(getColorByState(progressBar.context, state))
}

@BindingAdapter("onOptionClickListener")
fun bindOnOptionClickListener(textView: TextView, clickListener: OnOptionClickListener)
{
    textView.setOnClickListener {
        clickListener.onOptionClick(textView.text.toString().toInt())
    }
}

private fun getColorByState(context: Context, state: Boolean): Int {
    val colorResId = if (state) {
        android.R.color.holo_green_light
    } else {
        android.R.color.holo_red_light
    }
    return ContextCompat.getColor(
        context,
        colorResId
    )

}

private fun getSmileResId(state: Boolean): Int {
    return if (state) {
        R.drawable.ic_smile
    } else {
        R.drawable.ic_sad
    }
}

private fun getPercentOfRightAnswers(gameResult: GameResult): Int {
    return if (gameResult.countOfQuestions == 0) {
        0
    } else {
        ((gameResult.countOfRightAnswers / gameResult.countOfQuestions.toDouble()) * 100).toInt()
    }
}