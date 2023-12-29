package com.areeb.localnotifier

import android.Manifest
import android.app.Activity
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.MainApp.Companion.CHANNEL_ID
import com.areeb.localnotifier.ui.theme.LocalNotifierTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LocalNotifierTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    AutoNotificationRequest(this, Context.NOTIFICATION_SERVICE)
                }
            }
        }
    }
}

@Composable
private fun AutoNotificationRequest(activity: Activity, notificationString: String) {
    val context = LocalContext.current
    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS,
                ) == PackageManager.PERMISSION_GRANTED
            } else {
                mutableStateOf(true)
            },

        )
    }

    /*
    * Requesting for permission
    * */
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            hasNotificationPermission = it
        },
    )

    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(
            onClick = {
                if (hasNotificationPermission == false) {
                    // requesting permission for  notification permission
                    permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                } else {
                    Toast.makeText(
                        context,
                        hasNotificationPermission.toString(),
                        Toast.LENGTH_SHORT,
                    )
                        .show()

                    getNotificationService(activity = activity, notificationString)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .padding(10.dp),
        ) {
            Text(
                text = "trigger local notification",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 20.sp,
            )
        }
    }
}

fun getNotificationService(activity: Activity, notificationString: String) {
    val notificationManager = activity.getSystemService(notificationString) as NotificationManager
    val notification = NotificationCompat.Builder(activity.applicationContext, CHANNEL_ID)
        .setContentText("my name is areeb ahmad")
        .setContentTitle("hello").setSmallIcon(R.drawable.ic_launcher_foreground).build()
    notificationManager.notify(1, notification)
}
