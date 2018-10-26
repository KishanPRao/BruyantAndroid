package com.automator.bruyant

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_start)
		simulationMode.setOnClickListener {
			startActivity(Intent(this.applicationContext, SimulationActivity::class.java))
		}
		
		normalMode.setOnClickListener {
			startActivity(Intent(this.applicationContext, MainActivity::class.java))
		}
	}
	
	override fun onDestroy() {
		super.onDestroy()
	}
}
