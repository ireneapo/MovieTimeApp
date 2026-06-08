package gr.ihu.movietimeapp.rest

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Path

interface MovieApi {

    @GET("api/movies/")
    fun getMovies(

        @Query("title") title: String?,
        @Query("country") country: String?,
        @Query("date_from") dateFrom: String?,
        @Query("date_to") dateTo: String?,
        @Query("director") director: String?

    ): Call<List<Movie>>
    @GET("api/movies/{id}/")
    fun getMovie(
        @Path("id") id: Int
    ): Call<List<Movie>>

}