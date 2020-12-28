package com.redmadrobot.samples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // This method is implemented only for debug and staging build types.
        DebugPanelInitializer.init()
    }
}
