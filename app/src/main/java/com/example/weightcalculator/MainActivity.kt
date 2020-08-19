package com.example.weightcalculator

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*
import java.text.DecimalFormat

var barcodes = arrayListOf<Weight>()
lateinit var weight: Weight
var sum: Double? = null

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnScan.setOnClickListener {
            scanBarcode()
        }

        btnSave.setOnClickListener {
            var barcode = etBarcode.text.toString()


            val productsWeight = barcode.substring(0, 2) + "." + barcode.substring(2, barcode.dropLast(1).length)

             val productsWeightOriginal = "%3.f".format(productsWeight)
            weight = Weight(barcode, productsWeightOriginal.toDouble())



            barcodes.add(weight)
            var adapter = WeightAdapter(barcodes, tvSum)
            var recycler = recyclerview
            recycler.adapter = adapter
            var layoutmanager = LinearLayoutManager(this)
            recycler.layoutManager = layoutmanager
            adapter.notifyDataSetChanged()
            etBarcode.text.clear()
            sum = barcodes.sumByDouble { it.weight }
            tvSum.setText(sum.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val sharedPref =
            this?.getSharedPreferences("com.example.weightcalculator", Context.MODE_PRIVATE)
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                var name = result.contents
                Toast.makeText(applicationContext, name, Toast.LENGTH_SHORT).show()
                val productsWeight = etBarcode.text.toString().dropLast(1).toDouble()
                weight = Weight(name, productsWeight)
                barcodes.add(weight)
                var adapter = WeightAdapter(barcodes, tvSum)
                var recycler = recyclerview
                recycler.adapter = adapter
                recycler.layoutManager = LinearLayoutManager(this)
                adapter.notifyDataSetChanged()
                sum = barcodes.sumByDouble { it.weight }
                tvSum.setText(sum.toString())
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    fun scanBarcode() {
        val scanner = IntentIntegrator(this)
        scanner.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES)
        scanner.setPrompt("Barkoda yaxınlaşdıraraq skan edin");
        scanner.setOrientationLocked(false)
        scanner.setBeepEnabled(true)
        scanner.setBarcodeImageEnabled(true);
        scanner.initiateScan()
    }


}