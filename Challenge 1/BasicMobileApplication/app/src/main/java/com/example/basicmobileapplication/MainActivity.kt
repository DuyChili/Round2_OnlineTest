package com.example.basicmobileapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.basicmobileapplication.api.RetrofitInstance
import com.example.basicmobileapplication.databinding.ActivityMainBinding
import com.example.basicmobileapplication.models.Currency
import com.example.basicmobileapplication.models.ExchangeRates
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val apiKey = "0a98e43ede4510cf5f95acfb"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Gọi API để lấy danh sách đơn vị tiền tệ
        fetchCurrencyData()

        //xu ly doi tien khi nhan vao nut chuyen doi
        binding.btnConvert.setOnClickListener{
            xulyDoiTien()
        }
    }

    private fun xulyDoiTien() {
        binding.progressBar2.visibility = View.VISIBLE
        binding.ipResult.visibility = View.GONE
        val amountText = binding.etAmount.text.toString()
        if (amountText.isNotEmpty()) {
            val amount = amountText.toDouble()

            // Gọi API thông qua Retrofit
            val apiCall = RetrofitInstance.api.getCurrency(
                apiKey,
                baseRate = binding.spinnerFrom.selectedItem.toString(),
                baseResult = binding.spinnerTo.selectedItem.toString(),
                amount = amount.toString()
            )

            apiCall.enqueue(object : Callback<Currency> {
                override fun onResponse(
                    call: Call<Currency>,
                    response: Response<Currency>
                ) {
                    if (response.isSuccessful) {
                        val conversionResult = response.body()?.conversion_result
                        // Định dạng số với dấu chấm
                        val symbols = DecimalFormatSymbols().apply {
                            groupingSeparator = '.'
                        }
                        val decimalFormat = DecimalFormat("#,###", symbols)

                        // Chuyển đổi giá trị và hiển thị
                        val formattedResult = decimalFormat.format(conversionResult)
                        binding.tvResult.setText(formattedResult.toString()) // Hiển thị kết quả
                    } else {
                        Log.e("API Error", "Response Error: ${response.message()}")
                        binding.tvResult.setText("API Error: ${response.message()}")
                    }
                    binding.progressBar2.visibility = View.GONE
                    binding.ipResult.visibility = View.VISIBLE
                }

                override fun onFailure(call: Call<Currency>, t: Throwable) {
                    // Hiển thị lỗi qua hộp thoại
                    showErrorDialog("Đường truyền mạng không ổn định")
                    binding.progressBar2.visibility = View.GONE
                    binding.ipResult.visibility = View.VISIBLE
                }
            })
        } else {
            showErrorDialog("Vui lòng nhập số tiền")
            binding.progressBar2.visibility = View.GONE
            binding.ipResult.visibility = View.VISIBLE
        }
    }



    private fun fetchCurrencyData() {
        binding.progressBar.visibility = View.VISIBLE
        // Gọi API thông qua Retrofit
        val apiCall = RetrofitInstance.api.getExchangeRates(apiKey, "USD") // Lấy tỷ giá từ USD

        apiCall.enqueue(object : Callback<ExchangeRates> {
            override fun onResponse(
                call: Call<ExchangeRates>,
                response: Response<ExchangeRates>
            ) {
                if (response.isSuccessful) {
                    val rates = response.body()?.conversion_rates
                    if (rates != null) {
                        setupSpinners(rates.keys.toList()) // Gọi hàm setup spinner với danh sách tiền tệ
                    }
                } else {
                    Toast.makeText(this@MainActivity, "Lỗi khi tải dữ liệu: ${response.code()}", Toast.LENGTH_SHORT).show()
                }
                binding.progressBar.visibility = View.GONE
            }

            override fun onFailure(call: Call<ExchangeRates>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Lỗi kết nối: ${t.message}", Toast.LENGTH_SHORT).show()

                binding.progressBar.visibility = View.VISIBLE
            }
        })
    }

    private fun showErrorDialog(message: String) {
        // Tạo và hiển thị hộp thoại thông báo lỗi
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message) // Thiết lập thông điệp lỗi
            .setPositiveButton("OK") { dialog, id ->
                dialog.dismiss() // Đóng hộp thoại khi người dùng nhấn "OK"
            }
        // Tạo hộp thoại và hiển thị
        val dialog = builder.create()
        dialog.show()
    }

    private fun setupSpinners(currencyList: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, currencyList)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Gắn adapter cho các Spinner
        binding.spinnerFrom.adapter = adapter
        binding.spinnerTo.adapter = adapter

    }



}
