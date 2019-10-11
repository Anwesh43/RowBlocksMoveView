package com.anwesh.uiprojects.linkedrowblocksmoveview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.anwesh.uiprojects.rowblocksmoveview.RowBlocksMoveView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RowBlocksMoveView.create(this)
    }
}
