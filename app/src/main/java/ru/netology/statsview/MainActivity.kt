package ru.netology.statsview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.netology.statsview.ui.StatsView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Находит View с идентификатором statsView с помощью метода findViewById.
        //Приводит найденную View к типу StatsView.
        findViewById<StatsView>(R.id.statsView)
        //Присваивает свойству data найденной вьюхи список значений listOf, содержащий четыре
            .data = listOf( //элемента типа Float со значениями 500F каждый.
            500F,
            500F,
            500F,
            500F,
        )
    }
}
