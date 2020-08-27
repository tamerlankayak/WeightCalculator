package com.example.weightcalculator

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.android.synthetic.main.activity_main.*

var barcodes = arrayListOf<Weight>()
lateinit var weight: Weight
var sum: Double? = null
lateinit var adapter: WeightAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnDeleteAll.clearFocus()
        etBarcode.requestFocus()
        tvAllProductCount.text = barcodes.size.toString()
        btnSave.setOnClickListener {
            var barcode = etBarcode.text.toString()
            if (barcode.length != 13 || !barcode.isDigitsOnly()) {
                MaterialAlertDialogBuilder(this)
                    .setCancelable(false)
                    .setTitle("Yanlış formatlı barkod")
                    .setNegativeButton("Bağla") { dialog, which ->
                        dialog.dismiss()
                        etBarcode.text?.clear()
                    }.show()
                return@setOnClickListener
            }
            addProduct(barcode)
            btnDeleteAll.clearFocus()
            etBarcode.requestFocus()
        }

        etBarcode.addTextChangedListener(checkBarcodeIsEmpty)
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        return when (keyCode) {
            KeyEvent.KEYCODE_ENTER -> {
                btnDeleteAll.clearFocus()
                etBarcode.requestFocus()
                var barcode = etBarcode.text.toString()
                if (barcode.length != 13 || !barcode.isDigitsOnly()) {
                    MaterialAlertDialogBuilder(this)
                        .setCancelable(false)
                        .setTitle("Yanlış formatlı barkod")
                        .setNegativeButton("Bağla") { dialog, which ->
                            dialog.dismiss()
                            btnDeleteAll.clearFocus()
                            etBarcode.requestFocus()
                            etBarcode.text?.clear()
                        }.show()
                    return false
                }
                addProduct(barcode)
                true
            }
            else -> super.onKeyUp(keyCode, event)
        }
    }

    fun addProduct(barcode: String) {
        var cutBarcode = barcode.substring(7, 12)
        val productsWeight =
            cutBarcode.substring(0, 2) + "." + cutBarcode.substring(2, cutBarcode.length)
        weight = Weight(1.toString(), productsWeight.toDouble())
        barcodes.add(0, weight)

        adapter = WeightAdapter(barcodes, tvSum, btnDeleteAll, tvAllProductCount, etBarcode)
        var recycler = recyclerview
        recycler.adapter = adapter
        var layoutmanager = LinearLayoutManager(this)
        recycler.layoutManager = layoutmanager
        adapter.notifyDataSetChanged()
        btnDeleteAll.visibility = View.VISIBLE
        etBarcode.text?.clear()
        sum = barcodes.sumByDouble { it.weight }
        tvAllProductCount.text = barcodes.size.toString()
        tvSum.setText("Toplam: " + String.format("%.3f", sum) + " kq")
        btnDeleteAll.clearFocus()
        etBarcode.requestFocus()
    }

    fun deleteAll(view: View) {
        MaterialAlertDialogBuilder(this)
            .setTitle("Bütün qeydləri silməyə əminsiniz ?")
            .setNegativeButton("Xeyir") { dialog, which ->
                dialog.dismiss()
                btnDeleteAll.clearFocus()
                etBarcode.requestFocus()
            }
            .setPositiveButton("Bəli") { dialog, which ->
                barcodes.clear()
                adapter.notifyDataSetChanged()
                tvAllProductCount.text = barcodes.size.toString()
                btnDeleteAll.visibility = View.INVISIBLE
                tvSum.setText("Toplam: 0.0 kq")
                btnDeleteAll.clearFocus()
                etBarcode.requestFocus()
            }.show()
    }

    private val checkBarcodeIsEmpty: TextWatcher = object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            etBarcode.requestFocus()
            btnDeleteAll.clearFocus()
            var barcodeInput = etBarcode.text.toString().trim()
            btnSave.isEnabled = !(barcodeInput.isEmpty() || barcodeInput.length < 13)
        }
    }

}