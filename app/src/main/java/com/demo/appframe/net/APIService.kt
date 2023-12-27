package com.demo.appframe.net

import com.demo.appframe.beans.*
import retrofit2.http.*
import java.util.Objects

interface APIService {

    /**
     * 登录获取短信验证
     */
    @FormUrlEncoded
    @POST("api/send/code")
    suspend fun getPhoneCode(@Field("phone") phone: String): ApiResponse<LoginResponseCodeBean>

    /**
     * 登录接口
     */
    @FormUrlEncoded
    @POST("api/login")
    suspend fun goLogin(@FieldMap phone: MutableMap<String, String?>): ApiResponse<LoginResponBean>


    /**
     * 退出登录
     */
    @GET("api/loginout")
    suspend fun unRegist(): ApiResponse<String>


    /**
     * 推广
     */
    @FormUrlEncoded
    @POST("api/advert/callback")
    suspend fun appTuiGuangRegister(@FieldMap map: java.util.HashMap<String, Any?>): ApiResponse<Objects>


}