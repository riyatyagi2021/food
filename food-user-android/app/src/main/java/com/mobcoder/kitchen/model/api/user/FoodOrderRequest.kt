package com.mobcoder.kitchen.model.api.user

import com.mobcoder.kitchen.model.food.Food

class FoodOrderRequest {
    var foods: MutableList<Food>? = null
    var totalPrice: Float = 0.0f
}