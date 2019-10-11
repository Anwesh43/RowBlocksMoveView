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
