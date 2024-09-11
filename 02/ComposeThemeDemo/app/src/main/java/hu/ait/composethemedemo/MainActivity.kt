package hu.ait.composethemedemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hu.ait.composethemedemo.ui.theme.ComposeThemeDemoTheme
import hu.ait.composethemedemo.ui.theme.TitleHighlighted
import hu.ait.composethemedemo.ui.theme.myShapes

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposeThemeDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Movies()
                }
            }
        }
    }
}

@Composable
fun Movies(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.verticalScroll(rememberScrollState())
    ) {
        MovieCard(movie = Movie(
            title = "Forrest Gump",
            director = "Robert Zemeckis",
            releaseYear = 1994,
            description = "The history of the United States from the 1950s to the '70s unfolds from the perspective of an Alabama man with an IQ of 75, who yearns to be reunited with his childhood sweetheart.",
            imageResourceId = R.drawable.forrest
        ))
        MovieCard(movie = Movie(
            title = "Forrest Gump",
            director = "Robert Zemeckis",
            releaseYear = 1994,
            description = "The history of the United States from the 1950s to the '70s unfolds from the perspective of an Alabama man with an IQ of 75, who yearns to be reunited with his childhood sweetheart.",
            imageResourceId = R.drawable.forrest
        ))
    }
}

@Composable
fun MovieCard(movie: Movie, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape= MaterialTheme.shapes.medium,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = movie.imageResourceId),
                contentDescription = null, // You can provide a content description here
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(MaterialTheme.shapes.medium),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = movie.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimary
                //color = TitleHighlighted
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Director: ${movie.director}",
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Release Year: ${movie.releaseYear}",
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = movie.description,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

data class Movie(
    val title: String,
    val director: String,
    val releaseYear: Int,
    val description: String,
    val imageResourceId: Int // Resource ID of the movie poster image
)
