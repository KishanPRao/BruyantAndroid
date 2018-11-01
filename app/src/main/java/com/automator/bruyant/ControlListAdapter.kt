package com.automator.bruyant

import android.support.v4.app.Fragment
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

/**
 * Created by Kishan P Rao on 26/09/18.
 */
class ControlListAdapter : RecyclerView.Adapter<ControlListAdapter.ViewHolder>() {

    interface ControlListHelper {
        fun openFragment(fragment: Fragment)
    }

    private var fragments: Array<Fragment> = arrayOf(LightStatusFragment(), TemperatureFragment(), DoorFragment())
    private var items = arrayOf("Light Control", "Sensors", "Door Control")
    private var itemIcons = arrayOf(R.drawable.ic_bulb_vector, R.drawable.ic_sensor_vector, R.drawable.ic_door_vector)
    lateinit var helper: ControlListHelper

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent?.context).inflate(R.layout.layout_control_item, null))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        holder?.apply {
            holder.text.text = items[holder.adapterPosition]
            holder.image.setImageResource(itemIcons[holder.adapterPosition])
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val image: ImageView = view.findViewById(R.id.item_image)
        val text: TextView = view.findViewById(R.id.item_text)

        override fun onClick(v: View?) {
            val fragment = fragments[adapterPosition]
            helper.openFragment(fragment)
        }

        init {
            view.setOnClickListener(this)
        }
    }
}