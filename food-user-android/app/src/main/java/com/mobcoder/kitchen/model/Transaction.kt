package com.mobcoder.kitchen.model

import java.io.Serializable

class Transaction : Serializable {
    var _id: String? = null
    var amount : Float= 0.0f
    var orderId: String? = null
    var description: String? = null
    var created: String? = null
    var transactionType: Int? = null // 0 = credit, 1 = debit
    var empName: String? = null
    var couponWalletAmount : Float= 0.0f
    var walletAmount : Float= 0.0f
    var amountType : Int =0
    var status : Int =0


}