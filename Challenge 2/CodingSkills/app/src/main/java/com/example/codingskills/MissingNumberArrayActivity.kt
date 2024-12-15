package com.example.codingskills

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.codingskills.Utils.Inventory
import com.example.codingskills.databinding.ActivityMissingNumberArrayBinding

class MissingNumberArrayActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMissingNumberArrayBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMissingNumberArrayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        // Khi người dùng nhấn nút "Tạo mảng và tìm số bị thiếu"
        binding.btnGenerateAndFindMissing.setOnClickListener {
            val inputText = binding.edtNumberInput.text.toString().trim()

            if (TextUtils.isEmpty(inputText)) {
                Toast.makeText(this, "Vui lòng nhập số n", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Lấy giá trị n
            val n = inputText.toIntOrNull()
            if (n == null || n <= 0) {
                Toast.makeText(this, "Số n không hợp lệ", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Tạo mảng thiếu
            val randomArray = Inventory.generateRandomArray(n)

            // Tính số bị thiếu trong mảng
            val missingNumber = Inventory.findMissingNumber(randomArray)

            // In ra mảng thiếu
            val displayArray = randomArray.joinToString(", ")

            // Hiển thị kết quả
            binding.tvResult.text = "Mảng số: $displayArray\nSố bị thiếu là: $missingNumber"
        }
    }
}
