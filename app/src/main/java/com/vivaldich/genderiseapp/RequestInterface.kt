package com.vivaldich.genderiseapp

import com.vivaldich.genderiseapp.model.Gender
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface RequestInterface {
    companion object {
        const val BASE_URL = "https://api.genderize.io/"
        const val NAME = "name"
    }

    @GET("/")
    fun get(@Query(NAME) name: String): Observable<Gender>
}