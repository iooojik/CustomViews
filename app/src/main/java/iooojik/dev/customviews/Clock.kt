package iooojik.dev.customviews

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

class Clock : View {

    private var mPaint: Paint = Paint()
    private var mRect: Rect = Rect()
    private val clockNumbers = arrayListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12)
    private val fontSize = 70f
    private val clockRadius = 300f
    private val handTruncation = 0f
    private val hourTruncation = 0f

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private fun drawLargeCircle(canvas: Canvas?){
        if (canvas != null){
            mPaint.reset()
            mPaint.color = Color.BLACK
            mPaint.strokeWidth = 10f
            mPaint.style = Paint.Style.STROKE
            mPaint.isAntiAlias = true
            canvas.drawCircle(width / 2f, height / 2f, clockRadius, mPaint)
        }
    }

    private fun drawCenterCircle(canvas: Canvas?){
        if (canvas != null){
            mPaint.reset()
            mPaint.color = Color.BLACK
            canvas.drawCircle(width / 2f, height / 2f, 10f, mPaint)
        }
    }

    private fun drawNumbers(canvas: Canvas?){
        mPaint.reset()
        mPaint.color = Color.RED
        mPaint.textSize = fontSize
        if (canvas != null){
            for (num in clockNumbers){
                mPaint.getTextBounds(num.toString(), 0, num.toString().length, mRect)
                val angle = Math.PI / 6 * (num - 3)
                val x = (width/2 + cos(angle) * clockRadius - mRect.width() / 2).toFloat()
                val y = (height/2 + sin(angle) * clockRadius + mRect.width() / 2).toFloat()
                canvas.drawText(num.toString(), x, y, mPaint)
            }
        }
    }

    private fun drawHand(canvas: Canvas?, loc : Double, isHour : Boolean){
        if (canvas!=null) {
            val angle = Math.PI * loc / 30 - Math.PI / 2
            val rad =
                if (isHour) clockRadius - handTruncation else clockRadius - handTruncation - hourTruncation
            canvas.drawLine(
                (width / 2).toFloat(), (height / 2).toFloat(), (width / 2 + cos(angle) * rad).toFloat(),
                (height / 2 + sin(angle) * rad).toFloat(), mPaint
            )
        }
    }

    private fun drawHands(canvas: Canvas?){
        val hour = if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) > 12) Calendar.getInstance().get(Calendar.HOUR_OF_DAY) - 12
        else Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        drawHand(canvas, ((hour + Calendar.getInstance().get(Calendar.MINUTE) / 60) * 5f).toDouble(), true)
        drawHand(canvas, (Calendar.getInstance().get(Calendar.MINUTE)).toDouble(), false)
        drawHand(canvas, (Calendar.getInstance().get(Calendar.SECOND)).toDouble(), false)

    }

    override fun onDraw(canvas: Canvas?) {
        if (canvas != null) {

            drawLargeCircle(canvas)
            drawCenterCircle(canvas)
            drawNumbers(canvas)
            drawHands(canvas)

            postInvalidateDelayed(500)
            invalidate()
        }
    }


}