package com.example.win10.belajargolang.api

import android.util.Log

private const val TAG = "GithubService"
//private const val IN_QUALIFIER = "in:name"

/**
 * Search repos based on a query.
 * Trigger a request to the Github searchRepo API with the following params:
 * @param query searchRepo keyword
 * @param page request page index
 * @param itemsPerPage number of repositories to be returned by the Github API per page
 *
 * The result of the request is handled by the implementation of the functions passed as params
 * @param onSuccess function that defines how to handle the list of repos received
 * @param onError function that defines how to handle request failure
 */
fun searchRepos(
        service: GithubService,
        query: String,
        page: Int,
        itemsPerPage: Int,
        onSuccess: (repos: List<Repo>) -> Unit,
        onError: (error: String) -> Unit
) {
    Log.d(TAG, "query: $query, page: $page, itemsPerPage: $itemsPerPage")

//    val apiQuery = query + IN_QUALIFIER
    val apiQuery = query

    service.searchRepos(apiQuery, page, itemsPerPage).enqueue(
            object : Callback<RepoSearchResponse> {
                override fun onFailure(call: Call<RepoSearchResponse>?, t: Throwable) {
                    Log.d(TAG, "fail to get data")
                    onError(t.message ?: "unknown error")
                }

                override fun onResponse(
                        call: Call<RepoSearchResponse>?,
                        response: Response<RepoSearchResponse>
                ) {
                    Log.d(TAG, "got a response $response")
                    if (response.isSuccessful) {
                        val repos = response.body().items ?: emptyList()
                        onSuccess(repos)
                    } else {
                        onError(response.errorBody().string() ?: "Unknown error")
                    }
                }
            }
    )
}

/**
 * Github API communication setup via Retrofit.
 */
interface GithubService {
    /**
     * Get repos ordered by stars.
     */
    @GET("users")
    fun searchRepos(
            @Query("s") query: String,
            @Query("page") page: Int,
            @Query("per_page") itemsPerPage: Int
    ): Call<RepoSearchResponse>


    companion object {
        private const val BASE_URL = "http://172.17.32.1:8000/"


        fun create(): GithubService {
            val logger = HttpLoggingInterceptor()
            logger.level = Experimental.Level.BASIC

            val client = OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GithubService::class.java)
        }
    }
}
