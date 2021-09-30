package com.mobcoder.kitchen.model.api.user

class FoodCreateRequest {
    var foodId: String? = null
    var name: String? = null
    var images: MutableList<String>? = null
    var price: Float = 0.0f
    var isAvailable: Int? = 0
    var description: String? = null
    var status: Int? = 1
    var availableQuantity: Int? = 0
}