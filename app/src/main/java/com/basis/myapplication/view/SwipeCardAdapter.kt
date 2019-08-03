package com.basis.myapplication.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.basis.myapplication.R
import com.basis.myapplication.model.CardData

class SwipeCardAdapter (var dataList: List<CardData>, var context: Context) : BaseAdapter() {
    lateinit var viewHolder: ViewHolder

    override fun getCount(): Int {
        return dataList.size
    }

    override fun getItem(position: Int): Any {
        return dataList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var rowView: View? = convertView


        if (rowView == null) {
            rowView = LayoutInflater.from(context).inflate(R.layout.layt_card_stack, parent, false)
            viewHolder = ViewHolder()
            viewHolder.txtData = rowView.findViewById(R.id.item_card_txt)

            rowView?.tag = viewHolder

        } else {
            viewHolder = convertView?.tag as ViewHolder
        }
        viewHolder.txtData?.text = dataList[position].text


        return rowView!!
    }

    inner class ViewHolder {
        var txtData: TextView? = null

    }

}