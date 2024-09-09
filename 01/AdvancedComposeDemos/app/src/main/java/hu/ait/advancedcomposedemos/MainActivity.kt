package hu.ait.advancedcomposedemos

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import hu.ait.advancedcomposedemos.ui.theme.AdvancedComposeDemosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AdvancedComposeDemosTheme {
                /*Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    OrientationChangeDemo(innerPadding)
                }*/
                /*StairColumn {
                    for (i in 1..12) {
                        Button(onClick = {}) {
                            Text("Button $i")
                        }
                    }
                }*/
                MyDemoComposable()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = "Hello $name!"

        )
        Text(
            text = "Hello $name!"
        )

    }

}

@Composable
fun OrientationChangeDemo(innerPadding: PaddingValues) {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            Row(modifier = Modifier.padding(innerPadding)) {
                Text("text1")
                Text("text2")
            }
        }
        else -> {
            Column(modifier = Modifier.padding(innerPadding)) {
                Text("text1")
                Text("text2")
            }
        }
    }
}


@Composable
fun StairColumn(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Layout(
        content = content,
        modifier = Modifier.then(modifier),
    ) { measurables, constraints ->

        // map parameter List<Measurable> to List<Placeable>
        val placeables: List<Placeable> = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        // calculate our layout width and height
        val itemsTotalWidth = placeables.sumOf { placeable -> placeable.width }
        val ourLayoutTotalWidth = if (itemsTotalWidth > constraints.maxWidth) constraints.maxWidth else itemsTotalWidth
        val ourLayoutTotalHeight = placeables.sumOf { placeable -> placeable.height }

        // place child items
        layout(width = ourLayoutTotalWidth, height = ourLayoutTotalHeight) {
            var y = 0
            var x = 0

            placeables.forEach { placeable ->
                val itemHorizontalEndCoordinate = x + placeable.width
                if (itemHorizontalEndCoordinate > constraints.maxWidth) {
                    x = 0
                }

                placeable.place(x = x, y = y)

                y += placeable.height
                x += placeable.width
            }
        }
    }
}

@Composable
fun MyDemoComposable() {
    Column(
        modifier = Modifier
            .gradientBackground(listOf(Color.Blue, Color.Green, Color.White))
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Custom modifier was applied on me!")
    }
}


fun Modifier.gradientBackground(colors: List<Color>): Modifier = composed {
    drawWithContent {
        drawRect(
            brush = Brush.verticalGradient(colors),
            size = size
        )
        drawContent()
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AdvancedComposeDemosTheme {
        Greeting("Android")
    }
}