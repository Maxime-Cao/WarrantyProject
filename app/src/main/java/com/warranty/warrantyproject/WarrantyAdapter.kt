package com.warranty.warrantyproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.warranty.warrantyproject.infrastructures.db.WarrantyEntity

class WarrantyAdapter(private val listener: OnWarrantyClickListener) : RecyclerView.Adapter<WarrantyViewHolder>(){

    private val warrantyList = ArrayList<WarrantyEntity>()
    private var mListener: OnWarrantyClickListener = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WarrantyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val listItem = layoutInflater.inflate(R.layout.list_item,parent,false)
        return WarrantyViewHolder(listItem)
    }

    override fun onBindViewHolder(holder: WarrantyViewHolder, position: Int) {
        holder.bind(warrantyList[position])
        holder.itemView.setOnClickListener {
            mListener.onItemClick(position)
        }
    }

    override fun getItemCount(): Int {
        return warrantyList.size
    }

    fun setList(warranties:List<WarrantyEntity>) {
        warrantyList.clear()
        warrantyList.addAll(warranties)
    }
}

class WarrantyViewHolder(private val view:View) : RecyclerView.ViewHolder(view) {
    fun bind(warrantyEntity: WarrantyEntity) {
        val titleWarrantyView = view.findViewById<TextView>(R.id.title_warranty_item)
        val summaryWarrantyView = view.findViewById<TextView>(R.id.summary_warranty_item)
        val shopWarrantyView = view.findViewById<TextView>(R.id.shop_warranty_item)
        titleWarrantyView.text = warrantyEntity.title
        summaryWarrantyView.text = warrantyEntity.summary
        shopWarrantyView.text = warrantyEntity.shopName
    }
}