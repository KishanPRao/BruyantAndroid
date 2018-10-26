package com.automator.bruyant

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class SimulationActivity : AppCompatActivity() {
	companion object {
		private val TAG = SimulationActivity::class.java.simpleName
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_simul)
	}
	
	override fun onDestroy() {
		super.onDestroy()
	}
}
