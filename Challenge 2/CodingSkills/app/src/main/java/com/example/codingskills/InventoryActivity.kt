package com.example.codingskills

import android.R
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codingskills.Adapter.ProductAdapter
import com.example.codingskills.Utils.Inventory
import com.example.codingskills.databinding.ActivityInventoryBinding
import com.example.codingskills.models.Product

class InventoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInventoryBinding
    private lateinit var productAdapter: ProductAdapter
    private var allProducts = listOf<Product>() // Danh sách sản phẩm gốc
    private var currentSortBy: String = "" // Giá trị hiện tại của spinnerSortBy
    private var currentOrder: String = "" // Giá trị hiện tại của spinnerOrder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInventoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Khởi tạo danh sách sản phẩm
        ShowAllProduct()

        // Tổng giá trị hàng tồn kho
        val totalValue = Inventory.calculateTotalValue(allProducts)
        binding.tvTotalValue.text = "Tổng giá trị hàng tồn kho: $totalValue"

        // Sản phẩm đắt nhất
        val mostExpensive = Inventory.findMostExpensiveProduct(allProducts)
        binding.tvMostExpensive.text = "Sản phẩm đắt nhất: $mostExpensive"

        // Tìm kiếm sản phẩm
        binding.btnSearch.setOnClickListener {
            val search: String = binding.edtsearch.text.toString().trim()
            if (search.isNullOrEmpty()) {
                Toast.makeText(this@InventoryActivity, "Bạn chưa nhập vào", Toast.LENGTH_SHORT).show()
            } else {
                searchProduct(search)
            }
        }

       binding.toolbar.setNavigationOnClickListener {
            finish()
        }

        HandlingSort()


    }
    // thiet lap AutoCompleteTextView
    private fun HandlingSort() {
        // Thiết lập AutoCompleteTextView cho "Giá" hoặc "Số lượng"
        val sortByOptions = arrayOf("Giá", "Số lượng")
        val sortByAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, sortByOptions)
        binding.spinnerSortBy.setAdapter(sortByAdapter)

        // Thiết lập AutoCompleteTextView cho "Tăng dần" hoặc "Giảm dần"
        val orderOptions = arrayOf("Tăng dần", "Giảm dần")
        val orderAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, orderOptions)
        binding.spinnerOrder.setAdapter(orderAdapter)

        // Lắng nghe sự kiện thay đổi lựa chọn AutoCompleteTextView
        binding.spinnerSortBy.setOnItemClickListener { _, _, position, _ ->
            currentSortBy = sortByOptions[position]
            if (currentOrder.isNotEmpty()) {
                sortProducts() // Sắp xếp nếu đã chọn giá trị cho spinnerOrder
            } else {
                productAdapter.updateList(allProducts) // Hiển thị danh sách ban đầu
            }
        }

        binding.spinnerOrder.setOnItemClickListener { _, _, position, _ ->
            currentOrder = orderOptions[position]
            if (currentSortBy.isNotEmpty()) {
                sortProducts() // Sắp xếp nếu đã chọn giá trị cho spinnerSortBy
            } else {
                productAdapter.updateList(allProducts) // Hiển thị danh sách ban đầu
            }
        }
    }

   // xu ly AutoCompleteTextView
    private fun sortProducts() {
        val sortedProducts = when (currentSortBy) {
            "Giá" -> {
                if (currentOrder == "Tăng dần") {
                    allProducts.sortedBy { it.price }
                } else {
                    allProducts.sortedByDescending { it.price }
                }
            }
            "Số lượng" -> {
                if (currentOrder == "Tăng dần") {
                    allProducts.sortedBy { it.quantity }
                } else {
                    allProducts.sortedByDescending { it.quantity }
                }
            }
            else -> allProducts
        }

        // Cập nhật RecyclerView với danh sách đã được sắp xếp
        productAdapter.updateList(sortedProducts)
    }
    // hien thị Product
    private fun ShowAllProduct() {
        allProducts = listOf(
            Product("Laptop", 999.99, 5),
            Product("Smartphone", 499.99, 10),
            Product("Tablet", 299.99, 0),
            Product("Smartwatch", 199.99, 3)
        )

        // Thiết lập RecyclerView
        productAdapter = ProductAdapter(allProducts)
        binding.rvProducts.apply {
            layoutManager = LinearLayoutManager(this@InventoryActivity)
            adapter = productAdapter
        }
    }

    // tìm kiem product co ton tai trong kho khong
    private fun searchProduct(search: String) {
        // Lọc sản phẩm từ danh sách dựa trên tên sản phẩm
        val filteredProducts = allProducts.filter { it.name.equals(search, ignoreCase = true) }

        if (filteredProducts.isNotEmpty()) {
            // Cập nhật RecyclerView với sản phẩm tìm thấy
            productAdapter.updateList(filteredProducts)
        } else {
            // Hiển thị thông báo nếu không tìm thấy sản phẩm
            showProductNotFoundDialog()

        }
    }

    private fun showProductNotFoundDialog() {
        val dialog = AlertDialog.Builder(this)
            .setTitle("Không tìm thấy sản phẩm")
            .setMessage("Sản phẩm bạn tìm kiếm không có trong kho.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss() // Đóng dialog khi nhấn OK
                ShowAllProduct()
            }
            .create()
        dialog.show()
    }
}
