package com.mobcoder.kitchen.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mobcoder.kitchen.api.APICallHandler
import com.mobcoder.kitchen.api.APICallManager
import com.mobcoder.kitchen.api.APIType
import com.mobcoder.kitchen.model.Media
import com.mobcoder.kitchen.model.TransactionResponse
import com.mobcoder.kitchen.model.VendorResponse
import com.mobcoder.kitchen.model.api.user.FoodCreateRequest
import com.mobcoder.kitchen.model.api.user.FoodOrderRequest
import com.mobcoder.kitchen.model.api.user.MyProfileResponse
import com.mobcoder.kitchen.model.api.user.UserProfileResponse
import com.mobcoder.kitchen.model.base.CommonApiResponse
import com.mobcoder.kitchen.model.base.Errors
import com.mobcoder.kitchen.model.food.FoodResponse
import com.mobcoder.kitchen.model.food.ImageResponse
import com.mobcoder.kitchen.model.food.OrderDetailResponse
import com.mobcoder.kitchen.model.food.OrderResponse
import com.mobcoder.kitchen.utils.AppConstant
import com.mobcoder.kitchen.utils.AppUtil
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.util.*


class AuthViewModel : ViewModel(), APICallHandler<Any> {


    var loginSuccess =
        MutableLiveData<UserProfileResponse>()

    var foodUserSuccess =
        MutableLiveData<FoodResponse>()

    var foodVendorSuccess =
        MutableLiveData<FoodResponse>()

    var paymentSuccess =
        MutableLiveData<CommonApiResponse>()

    var userOrderListSuccess =
        MutableLiveData<OrderResponse>()


    var requestOrderListSuccess =
        MutableLiveData<OrderResponse>()

    var foodDeleteSuccess =
        MutableLiveData<CommonApiResponse>()

    var orderCancelSuccess =
        MutableLiveData<CommonApiResponse>()

    var orderAcceptSuccess =
        MutableLiveData<CommonApiResponse>()

    var orderCompleteSuccess =
        MutableLiveData<CommonApiResponse>()

    var imageUploadResponse =
        MutableLiveData<ImageResponse>()

    var addFoodSuccess =
        MutableLiveData<CommonApiResponse>()

    var transactionListSuccess =
        MutableLiveData<TransactionResponse>()

    var myProfileSuccess =
        MutableLiveData<MyProfileResponse>()

    var orderDetailSuccess =
        MutableLiveData<OrderDetailResponse>()

    var forgotSuccess =
        MutableLiveData<CommonApiResponse>()

    var vendorListSuccess =
        MutableLiveData<VendorResponse>()

    var error =
        MutableLiveData<Errors>()


    fun loginAPI(
        email: String?,
        password: String?
    ) {
        val apiCallManager = APICallManager(APIType.SIGN_IN, this)
        apiCallManager.loginAPI(email, password)
    }

    fun getFoodUser(vendorId : String?) {
        val apiCallManager = APICallManager(APIType.FOOD_LIST_USER, this)
        apiCallManager.getAllFoodAPI(vendorId)
    }

    fun getFoodVendor() {
        val apiCallManager = APICallManager(APIType.FOOD_LIST_VENDOR, this)
        apiCallManager.getAllFoodAPI(null)
    }

    fun paymentBuyAPI(paymentProduct: FoodOrderRequest?) {
        val apiCallManager = APICallManager(APIType.PAYMENT, this)
        apiCallManager.paymentBuyAPI(paymentProduct)
    }

    fun getUseOrders() {
        val apiCallManager = APICallManager(APIType.USER_ORDER_LIST, this)
        apiCallManager.getUseOrders()
    }

    fun getRequestOrder() {
        val apiCallManager = APICallManager(APIType.REQUEST_ORDER_LIST, this)
        apiCallManager.getRequestOrder()
    }

    fun foodDeleteAPI(foodId: String?) {
        val apiCallManager = APICallManager(APIType.FOOD_DELETE, this)
        apiCallManager.foodDeleteAPI(foodId!!)
    }


    fun orderCancelAPI(orderId: String?) {
        val apiCallManager = APICallManager(APIType.ORDER_CANCEL, this)
        apiCallManager.orderCancelAPI(orderId!!)
    }

    fun orderAcceptAPI(orderId: String?) {
        val apiCallManager = APICallManager(APIType.ORDER_ACCEPT, this)
        apiCallManager.orderAcceptAPI(orderId!!)
    }

    fun orderCompleteAPI(orderId: String?) {
        val apiCallManager = APICallManager(APIType.ORDER_COMPLETE, this)
        apiCallManager.orderCompleteAPI(orderId!!)
    }

    fun addFoodImageUploadAPI(
        selectedMediaFiles: List<Media?>?
    ) {
        val multipartList: MutableList<MultipartBody.Part> =
            ArrayList()
        for (i in selectedMediaFiles!!.indices) {
            val file = File(selectedMediaFiles[i]?.image)
            val requestFileSocialImage = RequestBody.create(
                AppUtil.getMimeType(selectedMediaFiles[i]?.image).toMediaTypeOrNull(), file
            )

            val socialImageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                AppConstant.MT.UPLOAD_MEDIA,
                file.name,
                requestFileSocialImage
            )

            multipartList.add(socialImageMultipart)
        }
        val apiCallManager =
            APICallManager(APIType.ADD_PHOTO, this)
        apiCallManager.addFoodImageUploadAPI(multipartList)
    }


    fun addFoodAPI(data: FoodCreateRequest?) {
        val apiCallManager = APICallManager(APIType.ADD_FOOD, this)
        apiCallManager.addFoodAPI(data)
    }


    fun updateFoodAPI(data: FoodCreateRequest?) {
        val apiCallManager = APICallManager(APIType.UPDATE_FOOD, this)
        apiCallManager.updateFoodAPI(data)
    }


    fun getTransactionAPI() {
        val apiCallManager = APICallManager(APIType.TRANSACTION_LIST, this)
        apiCallManager.getTransactionAPI()
    }

    fun getMyProfile() {
        val apiCallManager = APICallManager(APIType.USER_PROFILE, this)
        apiCallManager.getMyProfile()
    }

    fun getOrderDetail(orderId: String) {
        val apiCallManager = APICallManager(APIType.ORDER_DETAIL, this)
        apiCallManager.getOrderDetail(orderId)
    }

    fun forgotAPI(email: String) {
        val apiCallManager = APICallManager(APIType.FORGOT_API, this)
        apiCallManager.forgotAPI(email)
    }

    fun getAllVendorAPI() {
        val apiCallManager = APICallManager(APIType.VENDOR_LIST, this)
        apiCallManager.getAllVendorAPI()
    }

    override fun onSuccess(apiType: APIType, response: Any?) {

        when (apiType) {

            APIType.SIGN_IN -> {
                val userProfileResponse = response as UserProfileResponse
                loginSuccess.setValue(userProfileResponse)
            }

            APIType.FOOD_LIST_USER -> {
                val foodResponse = response as FoodResponse
                foodUserSuccess.setValue(foodResponse)
            }

            APIType.FOOD_LIST_VENDOR -> {
                val foodResponse = response as FoodResponse
                foodVendorSuccess.setValue(foodResponse)
            }

            APIType.PAYMENT -> {
                val foodResponse = response as CommonApiResponse
                paymentSuccess.setValue(foodResponse)
            }


            APIType.USER_ORDER_LIST -> {
                val foodResponse = response as OrderResponse
                userOrderListSuccess.setValue(foodResponse)
            }

            APIType.REQUEST_ORDER_LIST -> {
                val foodResponse = response as OrderResponse
                requestOrderListSuccess.setValue(foodResponse)
            }

            APIType.FOOD_DELETE -> {
                val foodResponse = response as CommonApiResponse
                foodDeleteSuccess.setValue(foodResponse)
            }

            APIType.ORDER_CANCEL -> {
                val foodResponse = response as CommonApiResponse
                orderCancelSuccess.setValue(foodResponse)
            }

            APIType.ORDER_ACCEPT -> {
                val foodResponse = response as CommonApiResponse
                orderAcceptSuccess.setValue(foodResponse)
            }

            APIType.ORDER_COMPLETE -> {
                val foodResponse = response as CommonApiResponse
                orderCompleteSuccess.setValue(foodResponse)
            }

            APIType.ADD_PHOTO -> {
                val foodResponse = response as ImageResponse
                imageUploadResponse.setValue(foodResponse)
            }

            APIType.ADD_FOOD, APIType.UPDATE_FOOD -> {
                val foodResponse = response as CommonApiResponse
                addFoodSuccess.setValue(foodResponse)
            }

            APIType.TRANSACTION_LIST -> {
                val foodResponse = response as TransactionResponse
                transactionListSuccess.setValue(foodResponse)
            }

            APIType.USER_PROFILE -> {
                val foodResponse = response as MyProfileResponse
                myProfileSuccess.setValue(foodResponse)
            }

            APIType.ORDER_DETAIL -> {
                val foodResponse = response as OrderDetailResponse
                orderDetailSuccess.setValue(foodResponse)
            }

            APIType.FORGOT_API -> {
                val foodResponse = response as CommonApiResponse
                forgotSuccess.setValue(foodResponse)
            }

            APIType.VENDOR_LIST -> {
                val foodResponse = response as VendorResponse
                vendorListSuccess.setValue(foodResponse)
            }

            else -> {
            }
        }

    }

    override fun onFailure(apiType: APIType, error: Errors) {
        this.error.value = error
    }
}