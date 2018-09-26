package com.automator.bruyant

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ControlListAdapter.ControlListHelper {
	private val mqttController = MqttController()
	
	companion object {
		private val TAG = MainActivity::class.java.simpleName
	}
	
	override fun onAttachFragment(fragment: Fragment?) {
		super.onAttachFragment(fragment)
		if (fragment is LightStatusFragment) {
			fragment.mqttController = mqttController
		}
	}
	
	override fun openFragment(fragment: Fragment) {
		Log.d(TAG, "openFragment, ")
		val transaction = supportFragmentManager.beginTransaction()
		transaction.replace(R.id.containerMain, fragment)
		transaction.addToBackStack(null)
		transaction.commit()
	}
	
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		mqttController.begin(this)
//		Need to ping server once.
		
		val adapter = ControlListAdapter()
		adapter.helper = this
		recyclerView.adapter = adapter
		recyclerView.layoutManager = GridLayoutManager(this, 2)
	}
	
	override fun onDestroy() {
		mqttController.end()
		super.onDestroy()
	}
}
