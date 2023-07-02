package ru.netology.statsview.utils

import android.content.Context
import kotlin.math.ceil

object AndroidUtils {
//Функция dp принимает Context и dp (значение в пикселях) в качестве параметров и возвращает значение в пикселях.
    fun dp(context: Context, dp: Int): Int =
//Вычисляет значение пикселей, округляя вверх результат произведения плотности экрана устройства из
//displayMetrics на значение dp. Приводит результат к типу Int и возвращает его.
        ceil(context.resources.displayMetrics.density * dp).toInt()
}
