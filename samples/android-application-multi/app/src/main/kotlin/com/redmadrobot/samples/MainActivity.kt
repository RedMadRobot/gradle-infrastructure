package com.redmadrobot.samples

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.redmadrobot.samples.module1.Module1
import com.redmadrobot.samples.module1.Module2

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Call code from other modules
        Module1.doSomething()
        Module2.doSomething()
    }
}
