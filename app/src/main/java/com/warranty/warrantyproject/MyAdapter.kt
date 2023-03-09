package com.warranty.warrantyproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.warranty.warrantyproject.domains.WarrantyCover

class MyAdapter(private val myObjectList: List<WarrantyCover>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private var cardList : List<WarrantyCover> = myObjectList;


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.card,
            parent, false
        )

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val myObject = myObjectList[position]
        holder.bind(myObject)
    }

    override fun getItemCount() = myObjectList.size

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title_text_view)
        private val summary: TextView = itemView.findViewById(R.id.summary_text_view)
        private val shop: TextView = itemView.findViewById(R.id.shop_text_view)

        fun bind(myObject: WarrantyCover) {
            title.text = myObject.title
            summary.text = myObject.summary
            shop.text = myObject.shopName

        }
    }
}