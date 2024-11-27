@file:OptIn(ExperimentalPermissionsApi::class)

package hu.bme.aut.android.storagetest

import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.startActivityForResult
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import hu.bme.aut.android.storagetest.ui.theme.StorageTestTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.invoke
import kotlinx.coroutines.launch
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.io.PrintWriter
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val file = File(filesDir, "my_file.txt")
        file.outputStream().let { PrintWriter(it) }.use {
            it.print("This is a sample file!")
        }
        setContent {
            StorageTestTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        StorageAccessFrameworkComposable()
                    }
                }
            }
        }
    }
}

@Composable
fun StorageAccessFrameworkComposable(){
    var launcherResult by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.CreateDocument("text/plain")) {
        launcherResult = it.toString()
        if (it == null) return@rememberLauncherForActivityResult

        val contentResolver = context.contentResolver
        coroutineScope.launch(Dispatchers.IO) {
            contentResolver.openOutputStream(it).let { PrintWriter(it) }.use {
                it.print("Hello From Storage Access Framework!")
            }
        }
    }
    Text(launcherResult)
    Button(onClick = {
        launcher.launch("test.txt")
    }) {
        Text("Create document")
    }
}

@Composable
fun MediaStoreComposable() {
    var queryResult by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    Text(queryResult)
    Button(
        onClick = {
            coroutineScope.launch(Dispatchers.IO) {
                val imageList = Util.loadMediaData(context)
                queryResult = imageList.toString()
            }
        }
    ) {
        Text("Load image data")
    }

    val readImagesPermissionState = rememberMultiplePermissionsState(
        listOf(
            android.Manifest.permission.READ_MEDIA_IMAGES,
            android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED,
        )
    )
    if (readImagesPermissionState.allPermissionsGranted) {
        Text("Images permission Granted")
    } else {
        val textToShow = if (readImagesPermissionState.shouldShowRationale) {
            "Images is important for this app. Please grant the permission."
        } else {
            "Images not available"
        }

        Text(textToShow)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { readImagesPermissionState.launchMultiplePermissionRequest() }) {
            Text("Request permission")
        }
    }
    Button(
        onClick = {
            (0..3).forEach {
                coroutineScope.launch(Dispatchers.IO) {
                    val contentResolver = context.contentResolver

                    // Prepare ContentValues for MediaStore
                    val contentValues = ContentValues().apply {
                        put(
                            MediaStore.Images.Media.DISPLAY_NAME,
                            "image${Random.nextInt(1000)}.jpg"
                        )
                        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                        put(
                            MediaStore.Images.Media.RELATIVE_PATH,
                            "Pictures/MyApp"
                        ) // Adjust the folder path as needed
                    }

                    // Insert the new file into MediaStore
                    val uri: Uri? = contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        contentValues
                    )
                    if (uri != null) {
                        // Open the raw resource as an InputStream
                        val inputStream: InputStream? =
                            context.resources.openRawResource(R.raw.image)
                        try {
                            // Open an OutputStream for the new file
                            val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
                            if (inputStream != null && outputStream != null) {
                                // Copy data from InputStream to OutputStream
                                inputStream.copyTo(outputStream)

                                // Close streams
                                inputStream.close()
                                outputStream.close()

                                Log.d("STORAGEAPP", "Image saved to Pictures/MyApp")
                            } else {
                                Log.e("STORAGEAPP", "Failed to open streams")
                            }
                        } catch (e: Exception) {
                            e.printStackTrace()
                            Log.e("STORAGEAPP", "Error saving image: ${e.message}")
                        }
                    } else {
                        Log.e("STORAGEAPP", "Failed to create MediaStore entry")
                    }
                }
            }
        }
    ) {
        Text("Create Image")
    }
}