package com.buggy.lunga

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

// KotlinML imports
import com.buggy.kotlinml.runtime.KotlinML
import com.buggy.kotlinml.runtime.KotlinMLModifiers

class MainActivity : ComponentActivity() {
    private val PrimaryColor = Color(0xFF2196F3)  // Blue
    private val SecondaryColor = Color(0xFF03DAC6) // Teal
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    @Composable
    fun MainScreen() {
        var counter by remember { mutableStateOf(0) }
        var textInput by remember { mutableStateOf("") }
        var isChecked by remember { mutableStateOf(false) }

        KotlinML {
            column(
                modifier = KotlinMLModifiers.fillMaxSize().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                text(
                    text = "🚀 KotlinML Framework",
                    fontSize = 24,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue
                )

                text(
                    text = "React-like DSL for Android + AI Translation",
                    fontSize = 16,
                    color = Color.Gray
                )

                spacer(height = 8.dp)

                // 🎯 NEW: Camera Translation Section
                card(
                    modifier = KotlinMLModifiers.fillMaxWidth(),
                    elevation = 12.dp
                ) {
                    column(
                        modifier = KotlinMLModifiers.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        text(
                            text = "📸 AI Camera Translation",
                            fontSize = 20,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryColor
                        )

                        spacer(height = 12.dp)

                        text(
                            text = "🎯 Step 1: Testing Camera Permissions",
                            fontSize = 16,
                            fontWeight = FontWeight.SemiBold
                        )

                        spacer(height = 8.dp)

                        text(
                            text = "• Camera permission handling\n• Beautiful permission screens\n• Privacy-focused messaging\n• Smooth user experience",
                            fontSize = 14,
                            color = Color.Gray
                        )

                        spacer(height = 16.dp)

                        button(
                            text = "📸 Test Camera Permissions",
                            onClick = {
                                val intent = Intent(this@MainActivity, CameraTranslationActivity::class.java)
                                startActivity(intent)
                            },
                            modifier = KotlinMLModifiers.fillMaxWidth()
                        )

                        spacer(height = 12.dp)

                        card(
                            modifier = KotlinMLModifiers.fillMaxWidth(),
                            elevation = 4.dp
                        ) {
                            column(
                                modifier = KotlinMLModifiers.padding(12.dp)
                            ) {
                                text(
                                    text = "🧪 Test Steps:",
                                    fontSize = 14,
                                    fontWeight = FontWeight.Medium,
                                    color = SecondaryColor
                                )

                                spacer(height = 4.dp)

                                text(
                                    text = "1. Tap 'Test Camera Permissions'\n2. See beautiful permission screen\n3. Grant camera permission\n4. Verify success message",
                                    fontSize = 12,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                // Original KotlinML Demo
                card(
                    modifier = KotlinMLModifiers.fillMaxWidth(),
                    elevation = 8.dp
                ) {
                    column(
                        modifier = KotlinMLModifiers.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        text(
                            text = "🎮 Framework Demo",
                            fontSize = 18,
                            fontWeight = FontWeight.SemiBold
                        )

                        spacer(height = 8.dp)

                        text(
                            text = "Counter: $counter",
                            fontSize = 20,
                            color = if (counter > 0) Color.Green else if (counter < 0) Color.Red else Color.Black
                        )

                        spacer(height = 12.dp)

                        row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            button(
                                text = "➖",
                                onClick = { counter-- }
                            )

                            button(
                                text = "🔄",
                                onClick = { counter = 0 }
                            )

                            button(
                                text = "➕",
                                onClick = { counter++ }
                            )
                        }
                    }
                }

                // Text Input Demo
                card(
                    modifier = KotlinMLModifiers.fillMaxWidth(),
                    elevation = 8.dp
                ) {
                    column(
                        modifier = KotlinMLModifiers.padding(16.dp)
                    ) {
                        text(
                            text = "📝 Text Input Demo",
                            fontSize = 16,
                            fontWeight = FontWeight.SemiBold
                        )

                        spacer(height = 8.dp)

                        textField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            placeholder = "Type something...",
                            modifier = KotlinMLModifiers.fillMaxWidth()
                        )

                        if (textInput.isNotEmpty()) {
                            spacer(height = 8.dp)
                            text(
                                text = "You typed: \"$textInput\"",
                                color = Color.Blue
                            )
                        }
                    }
                }

                // Controls Demo
                card(
                    modifier = KotlinMLModifiers.fillMaxWidth(),
                    elevation = 4.dp
                ) {
                    column(
                        modifier = KotlinMLModifiers.padding(16.dp)
                    ) {
                        row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            checkbox(
                                checked = isChecked,
                                onCheckedChange = { isChecked = it }
                            )

                            spacer(width = 8.dp)

                            text(
                                text = if (isChecked) "✅ Checkbox checked!" else "☐ Check this box"
                            )
                        }

                        spacer(height = 12.dp)

                        text(
                            text = "🎯 Step 1 Progress:",
                            fontSize = 14,
                            fontWeight = FontWeight.Medium,
                            color = Color.Green
                        )

                        spacer(height = 4.dp)

                        text("• KotlinML framework ✅")
                        text("• Camera permission handling 🔄")
                        text("• Camera preview (next)")
                        text("• Text recognition (next)")
                        text("• Translation overlay (next)")
                    }
                }
            }
        }
    }
}