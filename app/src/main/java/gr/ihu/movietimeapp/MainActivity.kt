package gr.ihu.movietimeapp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.CardDefaults

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import gr.ihu.movietimeapp.rest.Movie
import gr.ihu.movietimeapp.rest.RetrofitClient
import gr.ihu.movietimeapp.ui.theme.MovieTimeAppTheme
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import coil.compose.AsyncImage
import androidx.compose.foundation.clickable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button



class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var movies by mutableStateOf<List<Movie>>(emptyList())


        RetrofitClient.api.getMovies(null, null, null, null,  null)
            .enqueue(object : Callback<List<Movie>> {

                override fun onResponse(
                    call: Call<List<Movie>>,
                    response: Response<List<Movie>>
                ) {

                    Log.d("MOVIE_API", "Response received")

                    if (response.isSuccessful) {
                        movies = response.body() ?: emptyList()

                        response.body()?.forEach {

                            Log.d("MOVIE_API", it.title)

                        }

                    } else {

                        Log.d("MOVIE_API", "Error: ${response.code()}")

                    }
                }

                override fun onFailure(
                    call: Call<List<Movie>>,
                    t: Throwable
                ) {

                    Log.e("MOVIE_API", t.message.toString())

                }
            })

        setContent {

            MovieTimeAppTheme {
                var titleFilter by remember { mutableStateOf("") }
                var directorFilter by remember { mutableStateOf("") }
                var dateFromFilter by remember { mutableStateOf("") }
                var dateToFilter by remember { mutableStateOf("") }
                var countryFilter by remember { mutableStateOf("") }
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "list"
                ) {

                    composable("list") {

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                        ) { OutlinedTextField(
                            value = titleFilter,
                            onValueChange = { titleFilter = it },
                            label = { Text("Search movie") },
                            modifier = Modifier.fillMaxWidth()
                        )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = directorFilter,
                                onValueChange = { directorFilter = it },
                                label = { Text("Director") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = dateFromFilter,
                                onValueChange = { dateFromFilter = it },
                                label = { Text("Date From (YYYY-MM-DD)") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = dateToFilter,
                                onValueChange = { dateToFilter = it },
                                label = { Text("Date To (YYYY-MM-DD)") },
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = countryFilter,
                                onValueChange = { countryFilter = it },
                                label = { Text("Country") },
                                modifier = Modifier.fillMaxWidth()
                            )

                            Spacer(modifier = Modifier.height(8.dp))



                            Button(
                                onClick = {

                                    RetrofitClient.api.getMovies(
                                        titleFilter.ifBlank { null },
                                        countryFilter.ifBlank { null },
                                        dateFromFilter.ifBlank { null },
                                        dateToFilter.ifBlank { null },
                                        directorFilter.ifBlank { null }


                                    ).enqueue(object : Callback<List<Movie>> {

                                        override fun onResponse(
                                            call: Call<List<Movie>>,
                                            response: Response<List<Movie>>
                                        ) {
                                            if (response.isSuccessful) {
                                                movies = response.body() ?: emptyList()
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<List<Movie>>,
                                            t: Throwable
                                        ) {
                                            Log.e("MOVIE_API", t.message.toString())
                                        }
                                    })
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Search")
                            }


                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = {

                                    titleFilter = ""
                                    countryFilter= ""
                                    directorFilter = ""
                                    dateFromFilter = ""
                                    dateToFilter = ""

                                    RetrofitClient.api.getMovies(
                                        null,
                                        null,
                                        null,
                                        null,
                                        null
                                    ).enqueue(object : Callback<List<Movie>> {

                                        override fun onResponse(
                                            call: Call<List<Movie>>,
                                            response: Response<List<Movie>>
                                        ) {
                                            if (response.isSuccessful) {
                                                movies = response.body() ?: emptyList()
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<List<Movie>>,
                                            t: Throwable
                                        ) {
                                            Log.e("MOVIE_API", t.message.toString())
                                        }
                                    })
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text("Reset Filters")
                            }

                            LazyColumn {

                                items(movies) { movie ->

                                    Card(
                                        colors = CardDefaults.cardColors(
                                            containerColor = Color(0xFF1E1E1E)
                                        ),
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .clickable {
                                                navController.navigate("details/${movie.id}")
                                            }
                                    ) {

                                        Column(
                                            modifier = Modifier.padding(16.dp)
                                        ) {

                                            val imageUrl = movie.poster?.let {
                                                "http://192.168.2.4:8000$it"
                                            }

                                            if (imageUrl != null) {
                                                AsyncImage(
                                                    model = imageUrl,
                                                    contentDescription = movie.title,
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .height(220.dp)
                                                )

                                                Spacer(
                                                    modifier = Modifier.height(8.dp)
                                                )
                                            }

                                            Text(
                                                text = movie.title,
                                                color = Color.White,
                                                style = MaterialTheme.typography.headlineSmall
                                            )

                                            Spacer(
                                                modifier = Modifier.height(8.dp)
                                            )

                                            Text(
                                                text = "Rating: ${movie.imdb_rating}",
                                                color = Color.LightGray
                                            )

                                            Text(
                                                text = "Country: ${movie.release_country}",
                                                color = Color.LightGray
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    composable("details/{movieId}") { backStackEntry ->

                        val movieId =
                            backStackEntry.arguments
                                ?.getString("movieId")
                                ?.toInt() ?: 0

                        MovieDetailsScreen(movieId)

                    }
                }


            }
        }
    }
}