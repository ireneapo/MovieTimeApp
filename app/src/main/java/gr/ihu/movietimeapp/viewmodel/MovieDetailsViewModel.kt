package gr.ihu.movietimeapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import gr.ihu.movietimeapp.rest.Movie
import gr.ihu.movietimeapp.rest.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetailsViewModel {

    var movie by mutableStateOf<Movie?>(null)

    fun getMovie(id: Int) {

        RetrofitClient.api.getMovie(id)
            .enqueue(object : Callback<List<Movie>> {

                override fun onResponse(
                    call: Call<List<Movie>>,
                    response: Response<List<Movie>>
                ) {

                    if (response.isSuccessful) {
                        movie = response.body()?.firstOrNull()
                    }

                }

                override fun onFailure(
                    call: Call<List<Movie>>,
                    t: Throwable
                ) {

                }
            })
    }
}