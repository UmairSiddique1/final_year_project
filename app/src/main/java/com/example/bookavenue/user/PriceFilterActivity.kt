package com.example.bookavenue.user

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.bookavenue.databinding.ActivityPriceFilterBinding

class PriceFilterActivity : AppCompatActivity() {
    private lateinit var binding:ActivityPriceFilterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityPriceFilterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnApply.setOnClickListener {
            val sharedPref = applicationContext.getSharedPreferences("price-range", Context.MODE_PRIVATE)
            val editor = sharedPref.edit()
            val priceFrom:Int=binding.etFromPrice.text.toString().toInt()
            val priceTo:Int=binding.etToPrice.text.toString().toInt()
            editor.putInt("from", priceFrom)
            editor.putInt("to",priceTo)
            editor.apply()
           startActivity(Intent(applicationContext, UserHomeActivity::class.java))
        }
    }
}