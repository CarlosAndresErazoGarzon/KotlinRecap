package com.example.taller2.network

import com.example.taller2.model.PopularResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET

interface APIservice {
    @GET("movie/popular?api_key=ae993b0a343e955375ff2ea74f2227ed")
    suspend fun getMovies(): PopularResponse

    @GET("https://image.tmdb.org/t/p/w500/{id}")
    suspend fun getPicture(id: String?): Call<ResponseBody>

    //
}