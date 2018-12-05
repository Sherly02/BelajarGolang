package com.example.win10.belajargolang.db


/**
 * Room data access object for accessing the [Repo] table.
 */
@Dao
interface RepoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(posts: List<Repo>)

    // Do a similar query as the search API:
    // Look for repos that contain the query string in the name or in the description
    // and order those results descending, by the number of stars and then by name
//    @Query(
//        "SELECT * FROM repos WHERE (name LIKE :queryString) OR (description LIKE " +
//                ":queryString) ORDER BY stars DESC, name ASC"
//    )

    @Query(
            "SELECT * FROM repos WHERE (name LIKE :queryString) OR (name LIKE " + ":queryString) ORDER BY id ASC"
    )

    fun reposByName(queryString: String): DataSource.Factory<Int, Repo>

}
