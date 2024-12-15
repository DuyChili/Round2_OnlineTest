package com.example.codingskills.Utils

import com.example.codingskills.models.Product

object Inventory{

    // Tính toong gia tri hàng tồn kho
    fun calculateTotalValue(products: List<Product>): Double {
        return products.sumOf { it.price * it.quantity }
    }

    //Sản Phaam đăắt nhất
    fun findMostExpensiveProduct(products: List<Product>): String {
        return products.maxByOrNull { it.price }?.name ?: "Không có sản phẩm"
    }
    // Tìm kiếm Product trong kho
    fun isProductInStock(products: List<Product>, productName: String): Boolean {
        return products.any { it.name.equals(productName,true) }
    }

    //Tìm số bị thiếu trong mảng
    fun findMissingNumber(array: List<Int>): Int {
        val n = array.size
        val expectedSum = (n + 1) * (n + 2) / 2  // Tổng của dãy từ 1 đến n+1
        val actualSum = array.sum()  // Tổng các phần tử trong mảng
        return expectedSum - actualSum  // Số bị thiếu
    }

    // Hàm tạo mảng ngẫu nhiên
     fun generateRandomArray(n: Int): List<Int> {
        val fullArray = (1..n + 1).toList()
        val missingNumber = fullArray.random()
        return fullArray.filter { it != missingNumber }
    }
}