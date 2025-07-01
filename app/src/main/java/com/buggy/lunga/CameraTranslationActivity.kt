package com.buggy.lunga

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
class CameraTranslationActivity : ComponentActivity() {

    private val PrimaryColor = Color(0xFF2196F3)  // Blue
    private val SecondaryColor = Color(0xFF03DAC6) // Teal
    private val GrayColor = Color(0xFF666666)      // Gray

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    CameraTranslationScreen()
                }
            }
        }
    }

    @Composable
    fun CameraTranslationScreen() {
        val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

        when (val permissionStatus = cameraPermissionState.status) {
            is PermissionStatus.Granted -> {
                CameraGrantedScreen()
            }

            is PermissionStatus.Denied -> {
                if (permissionStatus.shouldShowRationale) {
                    PermissionRationaleScreen {
                        cameraPermissionState.launchPermissionRequest()
                    }
                } else {
                    PermissionRequestScreen {
                        cameraPermissionState.launchPermissionRequest()
                    }
                }
            }
        }
    }

    @Composable
    fun PermissionRequestScreen(onRequestPermission: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📸 Camera Translation",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Icon(
                imageVector = Icons.Filled.PhotoCamera,
                contentDescription = "Camera",
                modifier = Modifier.size(80.dp),
                tint = PrimaryColor
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Transform text with AI",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Point your camera at any text to instantly translate it into your preferred language.",
                fontSize = 16.sp,
                color = GrayColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "✨ Features:",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryColor
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    FeatureItem("🔍", "Real-time text detection")
                    FeatureItem("🌍", "50+ language support")
                    FeatureItem("⚡", "Instant translation overlay")
                    FeatureItem("💾", "Save favorite translations")
                    FeatureItem("📚", "Build your vocabulary")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onRequestPermission,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "📸 Grant Camera Permission")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { finish() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "← Back to Main")
            }
        }
    }

    @Composable
    fun PermissionRationaleScreen(onRequestPermission: () -> Unit) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "📸 Camera Access Needed",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "We need camera access to detect and translate text in real-time.",
                fontSize = 16.sp,
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Your privacy is important - we never save or share your camera images.",
                fontSize = 14.sp,
                color = GrayColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onRequestPermission,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Grant Permission")
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = { finish() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Not Now")
            }
        }
    }

    @Composable
    fun CameraGrantedScreen() {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✅ Camera Permission Granted!",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Green,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "🚀 Step 1 Complete",
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = PrimaryColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Next: Implementing camera preview...",
                fontSize = 16.sp,
                color = GrayColor
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { finish() }
            ) {
                Text(text = "← Back to Main")
            }
        }
    }

    @Composable
    private fun FeatureItem(icon: String, description: String) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = icon, fontSize = 16.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = description, fontSize = 14.sp, color = Color.Black)
        }
    }
}