package iooojik.dev.customviews.parking

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import iooojik.dev.customviews.R


class Park : View {

    private val mPaint = Paint()
    //граница
    private val mainFrame = RectF()
    //ширина линии
    private val strokeWidth = 30f
    //количество стоянок
    private val standsCount = 7
    //длина линии стоянки
    private val parkLength = 300f
    //машинка
    private lateinit var car : Bitmap
    private val carFrame = RectF()
    private val carSize = 150f
    private var heightFloat = 0f
    private var widthFloat = 0f



    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        heightFloat = height.toFloat()
        widthFloat = width.toFloat()
        carFrame.set(0f, 0f, carSize, carSize)
        carFrame.offsetTo(widthFloat/2, heightFloat-250)
    }

    private fun drawFrame(canvas: Canvas?){
        //внешняя рамка
        if (canvas != null){
            mPaint.color = Color.BLACK
            mPaint.style = Paint.Style.STROKE
            mPaint.strokeWidth = strokeWidth
            mainFrame.set(0f, 0f, widthFloat, heightFloat)
            canvas.drawRect(mainFrame, mPaint)
        }
    }

    private fun drawStands(canvas: Canvas?){
        //рисуем стоянки
        if (canvas != null){
            //количество стоянок с левой стороны
            val leftCountStands = standsCount/2
            //количество стоянок с правой стороны
            val rightCountStands = standsCount - leftCountStands
            //массив с координатами стоянок
            val stands = mutableListOf<Stand>()
            //получаем координаты стоянок
            var startXPos = 0f
            for (i in 1..leftCountStands){
                val coordinateY = ((height/leftCountStands)*i.toFloat())
                stands.add(Stand(startXPos, coordinateY))
            }
            startXPos = width.toFloat()
            for (i in 1..rightCountStands){
                val coordinateY = ((height/rightCountStands)*i.toFloat())
                stands.add(Stand(startXPos, coordinateY))
            }
            //рисуем стоянки по координатам
            for (stand in stands){
                val xEnd = if (stand.x + parkLength > width) stand.x - parkLength else stand.x + parkLength
                canvas.drawLine(stand.x, stand.y, xEnd, stand.y, mPaint)
            }
        }
    }

    private fun drawCar(canvas: Canvas?){
        if (canvas != null){
            mPaint.style = Paint.Style.STROKE
            mPaint.color = Color.GREEN
            car = BitmapFactory.decodeResource(resources, R.drawable.baseline_directions_bus_black_36dp)
            canvas.drawBitmap(car, null, carFrame, mPaint)

            //car = RectF(widthFloat / 2.toFloat(), 150f, 100f, 100f)
            //canvas.drawRect(car, mPaint)
        }
    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {
            canvas.drawARGB(80, 102, 204, 255);
            drawFrame(canvas)
            drawStands(canvas)
            drawCar(canvas)
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when(event!!.action){
            MotionEvent.ACTION_MOVE -> {
                carFrame.offsetTo(event.x, event.y)
                Log.e("ttt", event.y.toString())
            }
        }
        return true
    }

    private fun convertPixelsToDp(pixels: Float) = pixels / resources.displayMetrics.density

    private fun convertDpToPixels(dp: Float) = dp * context.resources.displayMetrics.density
}