package com.automator.bruyant

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by Kishan P Rao on 26/09/18.
 */
class ControlListAdapter : RecyclerView.Adapter<ControlListAdapter.ViewHolder>() {
	
	interface ControlListHelper {
		fun openFragment(fragment: Fragment)
	}
	
	private var fragments = arrayOf(LightStatusFragment())
	private var items = arrayOf("Light Control")
	lateinit var helper: ControlListHelper
	
	override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
		return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.layout_control_item, null))
	}
	
	override fun getItemCount(): Int {
		return items.size
	}
	
	override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
	}
	
	inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
		override fun onClick(v: View?) {
			val fragment = fragments[adapterPosition]
			helper.openFragment(fragment)
		}
		
		init {
			view.setOnClickListener(this)
		}
	}
}