package com.mobcoder.kitchen.api

import android.content.Intent
import com.mobcoder.kitchen.R
import com.mobcoder.kitchen.base.App
import com.mobcoder.kitchen.model.TransactionResponse
import com.mobcoder.kitchen.model.VendorResponse
import com.mobcoder.kitchen.model.api.user.FoodCreateRequest
import com.mobcoder.kitchen.model.api.user.FoodOrderRequest
import com.mobcoder.kitchen.model.api.user.MyProfileResponse
import com.mobcoder.kitchen.model.api.user.UserProfileResponse
import com.mobcoder.kitchen.model.base.BaseResponse
import com.mobcoder.kitchen.model.base.CommonApiResponse
import com.mobcoder.kitchen.model.base.Errors
import com.mobcoder.kitchen.model.food.FoodResponse
import com.mobcoder.kitchen.model.food.ImageResponse
import com.mobcoder.kitchen.model.food.OrderDetailResponse
import com.mobcoder.kitchen.model.food.OrderResponse
import com.mobcoder.kitchen.ui.auth.LoginActivity
import com.mobcoder.kitchen.utils.PreferenceKeeper
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class APICallManager<T>(
    private val mCallType: APIType,
    private val mAPICallHandler: APICallHandler<T>
) : Callback<BaseResponse<T>> {


    override fun onResponse(call: Call<BaseResponse<T>>?, response: Response<BaseResponse<T>>) {
        if (response.code() == APIStatusCode.OK || response.code() == APIStatusCode.CREATED || response.code() == APIStatusCode.NO_CONTENT) {
            val body = response.body()
            if (body != null) {
                if (body.statusCode == 1) {
                    mAPICallHandler.onSuccess(mCallType, body.responseData)
                } else {
                    if (body.error != null) {

                        if (body.error?.errorCode == 17) {
                            PreferenceKeeper.getInstance().isLogin = false
                            PreferenceKeeper.getInstance().accessToken = null
                            val intent =
                                Intent(App.INSTANCE, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            App.INSTANCE.startActivity(intent)

                        } else {
                            mAPICallHandler.onFailure(mCallType, body.error!!)
                        }
                    } else {
                        val errors = Errors()
                        val errorMessage =
                            App.INSTANCE.resources.getString(R.string.error_something_wrong)
                        errors.errorMessage = errorMessage
                        mAPICallHandler.onFailure(mCallType, errors)
                    }
                }
            }
        } else {
            val errors = Errors()
            val errorMessage =
                App.INSTANCE.resources.getString(R.string.error_something_wrong)
            errors.errorMessage = errorMessage
            mAPICallHandler.onFailure(mCallType, errors)
        }
    }

    override fun onFailure(call: Call<BaseResponse<T>>?, throwable: Throwable) {
        val errorCode = 0
        val message: String? =
            if (throwable is UnknownHostException || throwable is SocketException || throwable is SocketTimeoutException) {
                App.INSTANCE.resources.getString(R.string.error_something_wrong)
            } else {
                throwable.message
            }
        val errors = Errors()
        errors.errorMessage = message
        mAPICallHandler.onFailure(mCallType, errors)
    }

    fun loginAPI(
        email: String?,
        password: String?
    ) {
        APIClient.getClient()
            .loginAPI(email, password)
            .enqueue(this@APICallManager as Callback<BaseResponse<UserProfileResponse?>>)
    }


    fun getAllFoodAPI(vendorId : String?) {
        APIClient.getClient().getAllFoodAPI(vendorId)
            .enqueue(this@APICallManager as Callback<BaseResponse<FoodResponse?>>)
    }


    fun paymentBuyAPI(paymentProduct: FoodOrderRequest?) {
        APIClient.getClient().paymentBuyAPI(paymentProduct)
            .enqueue(this@APICallManager as Callback<BaseResponse<CommonApiResponse?>>)
    }


    fun getUseOrders() {
        APIClient.getClient().getUseOrders()
            .enqueue(this@APICallManager as Callback<BaseResponse<OrderResponse?>>)
    }


    fun getRequestOrder() {
        APIClient.getClient().getRequestOrder()
            .enqueue(this@APICallManager as Callback<BaseResponse<OrderResponse?>>)
    }

    fun foodDeleteAPI(foodId: String) {
        APIClient.getClient().foodDeleteAPI(foodId)
            .enqueue(this@APICallManager as Callback<BaseResponse<CommonApiResponse?>>)
    }


    fun orderCancelAPI(orderId: String) {
        APIClient.getClient().orderCancelAPI(orderId)
            .enqueue(this@APICallManager as Callback<BaseResponse<CommonApiResponse?>>)
    }

    fun orderAcceptAPI(orderId: String) {
        APIClient.getClient().orderAcceptAPI(orderId)
            .enqueue(this@APICallManager as Callback<BaseResponse<CommonApiResponse?>>)
    }

    fun orderCompleteAPI(orderId: String) {
        APIClient.getClient().orderCompleteAPI(orderId)
            .enqueue(this@APICallManager as Callback<BaseResponse<CommonApiResponse?>>)
    }


    fun addFoodImageUploadAPI(
        multipartList: List<MultipartBody.Part>
    ) {
        APIClient.getClient()
            .addPhotos(multipartList)
            .enqueue(this@APICallManager as Callback<BaseResponse<ImageResponse?>>)
    }

    fun addFoodAPI(
        data: FoodCreateRequest?
    ) {
        APIClient.getClient()
            .addFoodAPI(data)
            .enqueue(this@APICallManager as Callback<BaseResponse<CommonApiResponse?>>)
    }

    fun updateFoodAPI(
        data: FoodCreateRequest?
    ) {
        APIClient.getClient()
            .updateFoodAPI(data)
            .enqueue(this@APICallManager as Callback<BaseResponse<CommonApiResponse?>>)
    }

    fun getTransactionAPI(
    ) {
        APIClient.getClient()
            .getTransactionAPI()
            .enqueue(this@APICallManager as Callback<BaseResponse<TransactionResponse?>>)
    }

    fun getMyProfile(
    ) {
        APIClient.getClient()
            .getMyProfile()
            .enqueue(this@APICallManager as Callback<BaseResponse<MyProfileResponse?>>)
    }

    fun getOrderDetail(
        orderId: String
    ) {
        APIClient.getClient()
            .getOrderDetail(orderId)
            .enqueue(this@APICallManager as Callback<BaseResponse<OrderDetailResponse?>>)
    }

    fun forgotAPI(email: String) {
        APIClient.getClient()
            .forgotAPI(email)
            .enqueue(this@APICallManager as Callback<BaseResponse<CommonApiResponse?>>)
    }

    fun getAllVendorAPI() {
        APIClient.getClient()
            .getAllVendorAPI()
            .enqueue(this@APICallManager as Callback<BaseResponse<VendorResponse?>>)
    }
}
