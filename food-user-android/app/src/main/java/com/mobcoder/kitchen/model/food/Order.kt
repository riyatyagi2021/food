package com.mobcoder.kitchen.model.food

import com.mobcoder.kitchen.model.Vendor
import com.mobcoder.kitchen.model.api.user.UserProfile
import java.io.Serializable


class Order : Serializable {
    var orderId: String? = null
    var employeeId: String? = null
    var foods: MutableList<Food>? = null
    var totalPrice: Float = 0.0f
    var status: Int? = 0
    var created: String? = null
    var employeePhone: String? = null
    var employeeName: UserProfile? = null
    var vendorDetail: Vendor? = null
}