package gr.ihu.movietimeapp
import android.content.Intent
import android.net.Uri
import androidx.compose.material3.Button
import androidx.compose.ui.platform.LocalContext

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import gr.ihu.movietimeapp.viewmodel.MovieDetailsViewModel
import coil.compose.AsyncImage
import androidx.compose.foundation.layout.fillMaxWidth



@Composable
fun MovieDetailsScreen(movieId: Int) {

    val viewModel = remember {
        MovieDetailsViewModel()
    }
    val context = LocalContext.current
    LaunchedEffect(movieId) {
        viewModel.getMovie(movieId)
    }

    val movie = viewModel.movie

    val imageUrl = movie?.poster?.let {
        "http://192.168.2.4:8000$it"
    }





    if (movie == null) {

        Text("Loading...")

    } else {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            if (imageUrl != null) {

                AsyncImage(
                    model = imageUrl,
                    contentDescription = movie.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = movie.title,
                style = MaterialTheme.typography.headlineMedium
            )


            Spacer(modifier = Modifier.height(8.dp))

            Text("Rating: ${movie.imdb_rating}")

            Text("Country: ${movie.release_country}")

            Text("Release date: ${movie.release_date}")
            Text("Duration: ${movie.duration} min")
            Text("Director: ${movie.director_name}")

            Text("Genre: ${movie.genre_name}")

            Spacer(modifier = Modifier.height(12.dp))

            Text(movie.description)
            Spacer(modifier = Modifier.height(16.dp))


            if (!movie.trailer_url.isNullOrBlank()) {

                Button(
                    onClick = {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse(movie.trailer_url)
                        )
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Watch Trailer")
                }
            }
        }
    }
}