package com.anwesh.uiprojects.rowblocksmoveview

/**
 * Created by anweshmishra on 11/10/19.
 */

import android.view.View
import android.view.MotionEvent
import android.content.Context
import android.app.Activity
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Canvas
import android.graphics.Color

val nodes : Int = 5
val blocks : Int = 5
val sizeFactor : Float = 2.9f
val strokeFactor : Int = 90
val foreColor : Int = Color.parseColor("#01579B")
val backColor : Int = Color.parseColor("#BDBDBD")
val scGap : Float = 0.01f
val delay : Long = 20

fun Int.inverse() : Float = 1f / this
fun Float.maxScale(i : Int, n : Int) : Float = Math.max(0f, this - i * n.inverse())
fun Float.divideScale(i : Int, n : Int) : Float = Math.min(n.inverse(), maxScale(i, n)) * n

fun Canvas.drawRowBlock(i : Int, sc : Float, size : Float, xGap : Float, paint : Paint) {
    val sci : Float = sc.divideScale(i, blocks)
    save()
    translate(xGap * (i + 1), 0f)
    rotate(90f * sci)
    drawRect(RectF(-size, -size, size, size), paint)
    restore()
}

fun Canvas.drawRowBlock(scale : Float, hSize : Float, w : Float, paint : Paint) {
    val xGap : Float = w / (blocks + 1)
    val size : Float = Math.min(hSize, xGap / sizeFactor)
    for (j in 0..(blocks - 1)) {
        drawRowBlock(j, scale, size, xGap, paint)
    }
}

fun Canvas.drawRBMNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = h / (nodes + 1)
    val size : Float = gap / sizeFactor
    paint.color = foreColor
    paint.strokeCap = Paint.Cap.ROUND
    paint.strokeWidth = Math.min(w, h) / strokeFactor
    save()
    translate(0f, gap * (i + 1))
    drawRowBlock(scale, size, w, paint)
    restore()
}

class RowBlocksMoveView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }
}