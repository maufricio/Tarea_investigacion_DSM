package com.dsm.lugaresudb.datos

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part


interface CloudinaryApi {
    @Multipart
    @POST("https://api.cloudinary.com/v1_1/dehc3bhj9/image/upload")
    suspend fun uploadImage(
        @Part file: MultipartBody.Part,
        @Part("upload_preset") uploadPreset: RequestBody
    ): Response<CloudinaryResponse>
}

