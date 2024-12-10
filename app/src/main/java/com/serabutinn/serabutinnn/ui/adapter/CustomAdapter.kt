package com.serabutinn.serabutinnn.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.serabutinn.serabutinnn.R
import com.serabutinn.serabutinnn.data.ListItem

class CustomAdapter(
    context: Context,
    private val resource: Int,
    private val items: List<ListItem>
) : ArrayAdapter<ListItem>(context, resource, items) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(resource, parent, false)

        val item = items[position]

        val titleView = view.findViewById<TextView>(R.id.itemTitle)
        val subtitleView = view.findViewById<TextView>(R.id.itemSubtitle)

        titleView.text = item.title
        subtitleView.text = item.subtitle

        view.setOnClickListener {
            item.onClickAction()
        }

        return view
    }
}
