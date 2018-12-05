package com.example.win10.belajargolang.data


import android.arch.paging.LivePagedListBuilder
import android.util.Log
import com.example.win10.belajargolang.api.GithubService


/**
 * Repository class that works with local and remote data sources.
 */
class GithubRepository(
        private val service: GithubService,
        private val cache: GithubLocalCache
) {

    // keep the last requested page. When the request is successful, increment the page number.
//    private var lastRequestedPage = 1
//
//    // LiveData of network errors.
//    private val _networkErrors = MutableLiveData<String>()
//
//    val networkErrors: LiveData<String>
//        get() = _networkErrors
//
//    // avoid triggering multiple requests in the same time
//    private var isRequestInProgress = false

    /**
     * Search repositories whose names match the query.
     */
//    fun search(query: String): RepoSearchResult {
//        Log.d("GithubRepository", "New query: $query")
////        lastRequestedPage = 1
////        requestAndSaveData(query)
//
//
//        // Get data from the local cache
//        val data = cache.reposByName(query)
//        val dataSourceFactory = cache.reposByName(query)
//
//        return RepoSearchResult(data, networkErrors)
//    }

    fun search(query: String): RepoSearchResult {
        Log.d("GithubRepository", "New query: $query")

        // Get data source factory from the local cache
        val dataSourceFactory = cache.reposByName(query)

        // Construct the boundary callback
        val boundaryCallback = RepoBoundaryCallback(query, service, cache)
        val networkErrors = boundaryCallback.networkErrors

        // Get the paged list
        val data = LivePagedListBuilder(dataSourceFactory, DATABASE_PAGE_SIZE)
                .setBoundaryCallback(boundaryCallback)
                .build()

        // Get the network errors exposed by the boundary callback
        return RepoSearchResult(data, networkErrors)
    }

//    fun requestMore(query: String) {
//        requestAndSaveData(query)
//    }

//    private fun requestAndSaveData(query: String) {
//        if (isRequestInProgress) return
//
//        isRequestInProgress = true
//        searchRepos(service, query, lastRequestedPage, NETWORK_PAGE_SIZE, { repos ->
//            cache.insert(repos, {
//                lastRequestedPage++
//                isRequestInProgress = false
//            })
//        }, { error ->
//            _networkErrors.postValue(error)
//            isRequestInProgress = false
//        })
//    }


    companion object {
        private const val NETWORK_PAGE_SIZE = 50
        private const val DATABASE_PAGE_SIZE = 20
    }
}
