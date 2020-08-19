package com.example.weightcalculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_row.view.*

class WeightAdapter(var allWeights: ArrayList<Weight>,var tvAllSum: TextView) :
    RecyclerView.Adapter<WeightAdapter.WeightViewHolder>() {

    private val mItemClickListener: OnItemClickListener? = null


    lateinit var tvSum: TextView



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)

        return WeightViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return allWeights.size
    }

    override fun onBindViewHolder(holder: WeightViewHolder, position: Int) {
        holder.itemView.apply {
            tvWeight.text = allWeights[position].weight.toString()

            btnDelete.setOnClickListener {
                allWeights.removeAt(position)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, allWeights.size)
                sum = barcodes.sumBy { it.weight }
                tvAllSum.setText(sum.toString())
            }
        }
    }

    inner class WeightViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {

    }

    interface OnItemClickListener {
        fun onItemClick(
            view: View?,
            position: Int,
            tag: String?
        )
    }
}