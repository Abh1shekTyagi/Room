package com.example.apparchitecturedao

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.apparchitecturedao.database.SleepNight

@BindingAdapter("sleepDurationFormatted")
fun TextView.setSleepDurationsFormatted(item: SleepNight?) {
    item?.let {
        text = convertDurationToFormatted(item.startTime, item.endTime, context.resources)
    }
}

@BindingAdapter("sleepQualityFormatted")
fun TextView.setSleepQualityFormatted(item: SleepNight?) {
    item?.let {
        text = convertNumericQualityToString(item.sleepRating, context.resources)
    }
}

@BindingAdapter("sleepQualityString")
fun TextView.setSleepQualityString(item: SleepNight?) {
    item?.let {
        text = convertNumericQualityToString(item.sleepRating, context.resources)
    }
}

@BindingAdapter("sleepImage")
fun ImageView.setSleepImage(item: SleepNight?){
    item?.let {
        setImageResource(
            when (item.sleepRating) {
                0 -> R.drawable.ic_sleep_0
                1 -> R.drawable.ic_sleep_1
                2 -> R.drawable.ic_sleep_2
                3 -> R.drawable.ic_sleep_3
                4 -> R.drawable.ic_sleep_4
                5 -> R.drawable.ic_sleep_5
                else -> {
                    R.drawable.ic_sleep_0
                }
            }
        )
    }
}