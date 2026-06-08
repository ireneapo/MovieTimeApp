package gr.ihu.movietimeapp.rest

data class Movie(

    val id: Int,
    val title: String,
    val duration: Int,
    val release_date: String,
    val imdb_url: String,
    val poster: String?,
    val imdb_rating: Double,
    val description: String,
    val release_country: String,
    val trailer_url:String?,
    val director_name: String?,
    val genre_name: String?
)