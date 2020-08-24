package com.example.weightcalculator

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.item_row.view.*

class WeightAdapter(
    var allWeights: ArrayList<Weight>,
    var tvAllSum: TextView,
    var deleteAllButton: MaterialButton,
    var tvProductCount: TextView,
    var barcodeInput: EditText
) :
    RecyclerView.Adapter<WeightAdapter.WeightViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeightViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_row, parent, false)
        return WeightViewHolder(inflater)
    }

    override fun getItemCount(): Int {
        return allWeights.size
    }

    override fun onBindViewHolder(holder: WeightViewHolder, position: Int) {
        holder.itemView.apply {
            tvWeight.setText(String.format("%.3f", allWeights[position].weight) + " kq")
            tvSingleCount.setText(allWeights[position].barcode)
            btnDelete.setOnClickListener {
                MaterialAlertDialogBuilder(it.context)
                    .setTitle("Seçilmiş qeydi silməyə əminsiniz ?")
                    .setNegativeButton("Xeyir") { dialog, which ->
                        dialog.dismiss()
                    }
                    .setPositiveButton("Bəli") { dialog, which ->
                        allWeights.removeAt(position)
                        notifyItemRemoved(position)
                        notifyItemRangeChanged(position, allWeights.size)
                        barcodeInput.requestFocus()
                        sum = barcodes.sumByDouble { it.weight }
                        tvAllSum.setText("Toplam: " + String.format("%.3f", sum) + " kq")
                        tvProductCount.text = allWeights.size.toString()
                        if (allWeights.size == 0) {
                            deleteAllButton.visibility = View.INVISIBLE
                            barcodeInput.requestFocus()
                            tvAllSum.setText("Toplam: 0.0 kq")
                        }
                    }.show()
            }
        }
    }

    inner class WeightViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview)
}