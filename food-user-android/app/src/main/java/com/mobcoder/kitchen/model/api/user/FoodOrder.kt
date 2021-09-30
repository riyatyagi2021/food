package com.mobcoder.kitchen.model.api.user

import java.io.Serializable

class FoodOrder : Serializable {

    var name: String? = null
    var foodId: String? = null
    var price: Float = 0.0f
    var quantity: Int = 0
    var total: Float = 0.0f
}
