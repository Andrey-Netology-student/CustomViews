package ru.netology.statsview.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import androidx.core.content.withStyledAttributes
import ru.netology.statsview.R
import ru.netology.statsview.utils.AndroidUtils
import kotlin.math.min
import kotlin.random.Random

class StatsView @JvmOverloads constructor(
    context: Context, //Контекст приложения
    attributeSet: AttributeSet? = null, //Набор атрибутов вида
    defStyleAttr: Int = 0, //Стиль по умолчанию
    defStyleRes: Int = 0, //Ресурс стиля по умолчанию
) : View(
    context,
    attributeSet,
    defStyleAttr,
    defStyleRes,
) {

    private var textSize = AndroidUtils.dp(context, 20).toFloat() //Хранит размер текста в пикселях
    private var lineWidth = AndroidUtils.dp(context, 5) //Хранит ширину линии в пикселях
    private var colors = emptyList<Int>() //Хранит список цветов в виде списка целочисленных значений

    init { //Инициализатор класса StatsView
        context.withStyledAttributes(attributeSet, R.styleable.StatsView) {
            //Получение значения атрибутов, относящихся к пользовательскому виду StatsView из XML-файла
            textSize = getDimension(R.styleable.StatsView_textSize, textSize)
            lineWidth = getDimension(R.styleable.StatsView_lineWidth, lineWidth.toFloat()).toInt()

            colors = listOf(
                //Получение значений атрибутов color1,2,3,4 и сохранения их в список colors.
                getColor(R.styleable.StatsView_color1, generateRandomColor()),
                getColor(R.styleable.StatsView_color2, generateRandomColor()),
                getColor(R.styleable.StatsView_color3, generateRandomColor()),
                getColor(R.styleable.StatsView_color4, generateRandomColor()),
            )
        }
    }

    var data: List<Float> = emptyList() //Хранит данные, которые будут отображаться
        set(value) {
            field = value
//При изменении значения data вызывается метод invalidate(), что приводит к перерисовке вида.
            invalidate()
        }
    private var radius = 0F //Хранит радиус для отображения элементов
    private var center = PointF() //Координаты центра графика
    private var oval = RectF() //Прямоугольник, внутри которого будет отображаться график.
    private val paint = Paint(
        Paint.ANTI_ALIAS_FLAG//Кисть для отображения графика, ANTIALIASFLAG - для сглаживания краёв
    ).apply {
        strokeWidth = lineWidth.toFloat() //Значение толщины кисти
        style = Paint.Style.STROKE //Для обводки контура фигур
        strokeJoin = Paint.Join.ROUND //Круглое соединение линий
        strokeCap = Paint.Cap.ROUND //Круглое окончание линий
    }

    private val textPaint = Paint( //Создаёт экземпляр класса Paint
        Paint.ANTI_ALIAS_FLAG //Убирает пикселизацию для сглаживания рисунка
    ).apply {
        textSize = this@StatsView.textSize //Размер шрифта
        style = Paint.Style.FILL //Для заполнения фигур цветом
        textAlign = Paint.Align.CENTER //Выравнивание текста по центру
    }
        //Метод вызывается при изменении размеров экрана или View.
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
//Вычисляет радиус фигуры как минимальное значение из ширины и высоты, делённое на 2, за вычетом ширины линии.
        radius = min(w, h) / 2F - lineWidth
//Устанавливает центр фигуры как точку с координатами (w / 2F, h / 2F), где w и h - новые значения ширины и высоты.
        center = PointF(w / 2F, h / 2F)
            //Создает овал, используемый для рисования фигуры, с координатами левого верхнего и
            // правого нижнего углов, определенными относительно центра фигуры и радиуса.
        oval = RectF(
            center.x - radius,
            center.y - radius,
            center.x + radius,
            center.y + radius
        )
    }

    override fun onDraw(canvas: Canvas) { //Отрисовывает View на экране
        if (data.isEmpty()) { //Если данные пустые, то метод завершает свою работу и не выполняет
            return //никаких действий
        }

        var startAngle = -90F //Начальный угол для отрисовки фигур
        val dataSum = data.sum() //Сохраняет сумму всех значений
        var actualSum = 0F //Текущая сумма долей
        var firstArcColor = 0 //Для хранения цвета первой фигуры
        var firstAngle = 0F //Для хранения угла первой фигуры
        data.forEachIndexed { index, datum -> //index - индекс элемента, datum - сам элемент.
            val fraction = datum / dataSum
            val angle = fraction * 360 //Умножает долю на 360 (полный круг)
            actualSum += fraction //Увеличивает actualSum на значение доли.
            paint.color = colors.getOrElse(index) { generateRandomColor() }
            if (index == 0) { //Если индекс равен 0, сохраняет значение цвета и угла первой фигуры.
                firstArcColor = paint.color
                firstAngle = angle
            }
            canvas.drawArc(oval, startAngle, angle, false, paint)
            startAngle += angle
        }

        paint.color = firstArcColor
        canvas.drawArc(oval, -90F, firstAngle / 4, false, paint)
        
        canvas.drawText(
            "%.2f%%".format(actualSum * 100),
            center.x,
            center.y + textPaint.textSize / 4,
            textPaint
        )
    }

    private fun generateRandomColor() = Random.nextInt(0xFF000000.toInt(), 0xFFFFFFFF.toInt())
}
