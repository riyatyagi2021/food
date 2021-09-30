package com.mobcoder.kitchen.api

import com.mobcoder.kitchen.model.TransactionResponse
import com.mobcoder.kitchen.model.VendorResponse
import com.mobcoder.kitchen.model.api.user.FoodCreateRequest
import com.mobcoder.kitchen.model.api.user.FoodOrderRequest
import com.mobcoder.kitchen.model.api.user.MyProfileResponse
import com.mobcoder.kitchen.model.api.user.UserProfileResponse
import com.mobcoder.kitchen.model.base.BaseResponse
import com.mobcoder.kitchen.model.base.CommonApiResponse
import com.mobcoder.kitchen.model.food.FoodResponse
import com.mobcoder.kitchen.model.food.ImageResponse
import com.mobcoder.kitchen.model.food.OrderDetailResponse
import com.mobcoder.kitchen.model.food.OrderResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*


interface IApiService {

    @FormUrlEncoded
    @POST("v1/employee/login")
    fun loginAPI(
        @Field("email") email: String?,
        @Field("password") password: String?
    ): Call<BaseResponse<UserProfileResponse?>>


    @GET("v1/food/list")
    fun getAllFoodAPI(
        @Query("vendorId") foodId: String?
    ): Call<BaseResponse<FoodResponse?>>


    @POST("v1/order/create")
    fun paymentBuyAPI(@Body paymentProduct: FoodOrderRequest?): Call<BaseResponse<CommonApiResponse?>>

    @GET("v1/order/employeeHistory")
    fun getUseOrders(): Call<BaseResponse<OrderResponse?>>


    @GET("v1/order/list")
    fun getRequestOrder(): Call<BaseResponse<OrderResponse?>>

    @DELETE("v1/food/delete")
    fun foodDeleteAPI(
        @Query("foodId") foodId: String?
    ): Call<BaseResponse<CommonApiResponse?>>


    @PUT("v1/order/cancelOrder")
    fun orderCancelAPI(
        @Query("orderId") orderId: String?
    ): Call<BaseResponse<CommonApiResponse?>>


    @PUT("v1/order/acceptOrder")
    fun orderAcceptAPI(
        @Query("orderId") orderId: String?
    ): Call<BaseResponse<CommonApiResponse?>>

    @PUT("v1/order/completeOrder")
    fun orderCompleteAPI(
        @Query("orderId") orderId: String?
    ): Call<BaseResponse<CommonApiResponse?>>


    @Multipart
    @POST("v1/upload/images")
    fun addPhotos(
        @Part mediaFiles: List<MultipartBody.Part>
    ): Call<BaseResponse<ImageResponse?>>


    @POST("v1/food/add")
    fun addFoodAPI(@Body paymentProduct: FoodCreateRequest?): Call<BaseResponse<CommonApiResponse?>>


    @PUT("v1/food/update")
    fun updateFoodAPI(@Body paymentProduct: FoodCreateRequest?): Call<BaseResponse<CommonApiResponse?>>


    @GET("v1/transaction/list")
    fun getTransactionAPI(): Call<BaseResponse<TransactionResponse?>>

    @GET("v1/employee/getMyProfile")
    fun getMyProfile(): Call<BaseResponse<MyProfileResponse?>>

    @GET("v1/order/orderById")
    fun getOrderDetail(
        @Query("orderId") orderId: String?
    ): Call<BaseResponse<OrderDetailResponse?>>

    @FormUrlEncoded
    @POST("v1/employee/forgotPassword")
    fun forgotAPI(
        @Field("email") email: String?
    ): Call<BaseResponse<CommonApiResponse?>>

    @GET("v1/food/vendors")
    fun getAllVendorAPI(): Call<BaseResponse<VendorResponse?>>

    @FormUrlEncoded
    @POST("v1/user")
    fun deviceTokenAPI(
        @Field("deviceToken") deviceToken: String?
    ): Call<CommonApiResponse>


}