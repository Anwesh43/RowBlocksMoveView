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

fun Canvas.drawRowBlock(i : Int, sc : Float, size : Float, xGap : Float, gap : Float, paint : Paint) {
    val sci : Float = sc.divideScale(i, blocks)
    save()
    translate(xGap * (i + 1), gap * sci)
    rotate(90f * sci)
    drawRect(RectF(-size, -size, size, size), paint)
    restore()
}

fun Canvas.drawRowBlock(scale : Float, gap : Float, hSize : Float, w : Float, paint : Paint) {
    val xGap : Float = w / (blocks + 1)
    val size : Float = Math.min(hSize, xGap / sizeFactor)
    for (j in 0..(blocks - 1)) {
        drawRowBlock(j, scale, size, xGap, gap, paint)
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
    drawRowBlock(scale, gap, size, w, paint)
    restore()
}

class RowBlocksMoveView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val renderer : Renderer = Renderer(this)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var dir : Float = 0f, var prevScale : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += scGap * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (animated) {
                cb()
                try {
                    Thread.sleep(delay)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class RBMNode(var i : Int, val state : State = State()) {

        private var next : RBMNode? = null
        private var prev : RBMNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor() {
            if (i < nodes - 1) {
                next = RBMNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawRBMNode(i, state.scale, paint)
        }

        fun update(cb : (Float) -> Unit) {
            state.update(cb)
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : RBMNode {
            var curr : RBMNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class RowBlocksMove(var i : Int) {

        private var curr : RBMNode = RBMNode(0)
        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(cb : (Float) -> Unit) {
            curr.update {
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }

    data class Renderer(var view : RowBlocksMoveView) {

        private val animator : Animator = Animator(view)
        private val rbm : RowBlocksMove = RowBlocksMove(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(backColor)
            rbm.draw(canvas, paint)
            animator.animate {
                rbm.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            rbm.startUpdating {
                animator.start()
            }
        }
    }

    companion object {

        fun create(activity : Activity) : RowBlocksMoveView {
            val view : RowBlocksMoveView = RowBlocksMoveView(activity)
            activity.setContentView(view)
            return view
        }
    }
}