package com.example.weightcalculator

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.activity_main.*

var barcodes = arrayListOf<Weight>()
lateinit var weight: Weight
var sum = 0

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        fun sil(pos: Int) {

        }

        btnScan.setOnClickListener {
            scanBarcode()
        }

        btnSave.setOnClickListener {
            var barcode = etBarcode.text.toString().toInt()
            weight = Weight(barcode)
            barcodes.add(weight)
            var adapter = WeightAdapter(barcodes, tvSum)
            var recycler = recyclerview
            recycler.adapter = adapter
            var layoutmanager = LinearLayoutManager(this)
            recycler.layoutManager = layoutmanager
            adapter.notifyDataSetChanged()
            etBarcode.text.clear()
            sum = barcodes.sumBy { it.weight }
            tvSum.setText(sum.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        val sharedPref =
            this?.getSharedPreferences("com.example.weightcalculator", Context.MODE_PRIVATE)

        val vibrator: Vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                var name = result.contents
                Toast.makeText(applicationContext, name, Toast.LENGTH_SHORT).show()
                weight = Weight(name.toInt())
                barcodes.add(weight)
                var adapter = WeightAdapter(barcodes, tvSum)
                var recycler = recyclerview
                recycler.adapter = adapter
                recycler.layoutManager = LinearLayoutManager(this)
                adapter.notifyDataSetChanged()
                sum = barcodes.sumBy { it.weight }
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