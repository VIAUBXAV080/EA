package hu.bme.aut.android.permissiontest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import hu.bme.aut.android.permissiontest.ui.theme.PermissionTestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PermissionTestTheme {
                Scaffold {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        PhoneIntentPermission()
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PermissionGroup() {
    val multiplePermissionsState = rememberMultiplePermissionsState(
        listOf(
            /*android.Manifest.permission.READ_PHONE_STATE,
            android.Manifest.permission.READ_BASIC_PHONE_STATE,
            android.Manifest.permission.READ_PHONE_NUMBERS,
            android.Manifest.permission.CALL_PHONE,
            "com.android.voicemail.permission.ADD_VOICEMAIL",
            android.Manifest.permission.USE_SIP,
            android.Manifest.permission.ANSWER_PHONE_CALLS,
            android.Manifest.permission.ACCEPT_HANDOVER*/
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION,
        )
    )
    val singlePermissionState = rememberPermissionState(android.Manifest.permission.USE_SIP)
    if (multiplePermissionsState.allPermissionsGranted) {
        // If all permissions are granted, then show screen with the feature enabled
        Text("Multiple permissions Granted! Thank you!")
    } else {
        Text(
            getTextToShowGivenPermissions(
                multiplePermissionsState.revokedPermissions,
                multiplePermissionsState.shouldShowRationale
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { multiplePermissionsState.launchMultiplePermissionRequest() }) {
            Text("Request permissions")
        }
    }

    if (singlePermissionState.status.isGranted) {
        Text("Single permission Granted")
    } else {
        Button(onClick = { singlePermissionState.launchPermissionRequest() }) {
            Text("Request permission")
        }
    }

    val context = LocalContext.current
    Button(
        onClick = {
            val phoneIntent = Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM, Uri.parse("tel:+36344569888"))
            try {
                context.startActivity(phoneIntent)
            } catch (e : Throwable) {
                Log.e("PERMISSION_TEST", e.message, e)

            }
        }
    ) {
        Text("Make a call")
    }
}

@OptIn(ExperimentalPermissionsApi::class)
private fun getTextToShowGivenPermissions(
    permissions: List<PermissionState>,
    shouldShowRationale: Boolean
): String {
    val revokedPermissionsSize = permissions.size
    if (revokedPermissionsSize == 0) return ""

    val textToShow = StringBuilder().apply {
        append("The ")
    }

    for (i in permissions.indices) {
        textToShow.append(permissions[i].permission)
        when {
            revokedPermissionsSize > 1 && i == revokedPermissionsSize - 2 -> {
                textToShow.append(", and ")
            }
            i == revokedPermissionsSize - 1 -> {
                textToShow.append(" ")
            }
            else -> {
                textToShow.append(", ")
            }
        }
    }
    textToShow.append(if (revokedPermissionsSize == 1) "permission is" else "permissions are")
    textToShow.append(
        if (shouldShowRationale) {
            " important. Please grant all of them for the app to function properly."
        } else {
            " denied. The app cannot function without them."
        }
    )
    return textToShow.toString()
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun CameraIntentPermission() {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)
    if (cameraPermissionState.status.isGranted) {
        Text("Camera permission Granted")
    } else {
        val textToShow = if (cameraPermissionState.status.shouldShowRationale) {
            "The camera is important for this app. Please grant the permission."
        } else {
            "Camera not available"
        }

        Text(textToShow)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { cameraPermissionState.launchPermissionRequest() }) {
            Text("Request permission")
        }
    }

    val context = LocalContext.current
    Button(
        onClick = {
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            try {
                context.startActivity(cameraIntent)
            } catch (e : Throwable) {
                Log.e("PERMISSION_TEST", e.message, e)

            }
        }
    ) {
        Text("Take Picture")
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun PhoneIntentPermission() {
    val phonePermissionState = rememberPermissionState(android.Manifest.permission.CALL_PHONE)
    if (phonePermissionState.status.isGranted) {
        Text("Phone permission Granted")
    } else {
        val textToShow = if (phonePermissionState.status.shouldShowRationale) {
            "The phone is important for this app. Please grant the permission."
        } else {
            "Phone not available"
        }

        Text(textToShow)
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { phonePermissionState.launchPermissionRequest() }) {
            Text("Request permission")
        }
    }

    val context = LocalContext.current
    Button(
        onClick = {
            val phoneIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:+36875469555"))
            try {
                context.startActivity(phoneIntent)
            } catch (e : Throwable) {
                Log.e("PERMISSION_TEST", e.message, e)

            }
        }
    ) {
        Text("Call")
    }
}