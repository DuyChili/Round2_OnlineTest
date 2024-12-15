package com.example.codingskills

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.codingskills.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn1.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, InventoryActivity::class.java)
            startActivity(intent)

        })
        binding.btn2.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, MissingNumberArrayActivity::class.java)
            startActivity(intent)

        })
    }
}