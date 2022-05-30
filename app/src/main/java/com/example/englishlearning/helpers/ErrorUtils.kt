package com.example.englishlearning.helpers

import com.example.englishlearning.models.APIErrors
import com.example.englishlearning.services.ServiceBuilder
import okhttp3.ResponseBody

import retrofit2.Converter
import retrofit2.Response
import java.io.IOException


class ErrorUtils {

    fun parseError(response: Response<*>): APIErrors? {
        val converter: Converter<ResponseBody, APIErrors> = ServiceBuilder.retrofit
            .responseBodyConverter(APIErrors::class.java, arrayOfNulls<Annotation>(0))
        val error: APIErrors
        error = try {
            converter.convert(response.errorBody())!!
        } catch (e: IOException) {
            return APIErrors()
        }
        return error
    }
}