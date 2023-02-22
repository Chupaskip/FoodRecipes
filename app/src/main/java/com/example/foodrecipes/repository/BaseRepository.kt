package com.example.foodrecipes.repository

import com.example.foodrecipes.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

abstract class BaseRepository {

    suspend fun <T> safeApiCall(apiToBeCalled: suspend()-> Response<T>):Resource<T>{

        return withContext(Dispatchers.IO){
            try {
                val response: Response<T> = apiToBeCalled()
                if (response.isSuccessful) {
                    Resource.Success(data = response.body()!!)
                } else {
                    Resource.Error(errorMessage = "Something went wrong with api")
                }
            } catch (e: HttpException) {
                Resource.Error(errorMessage = e.message ?: "Something went wrong")
            } catch (e: IOException) {
                Resource.Error("Please check your network connection")
            } catch (e: Exception) {
                Resource.Error(errorMessage = "Something went wrong")
            }
        }
    }
}