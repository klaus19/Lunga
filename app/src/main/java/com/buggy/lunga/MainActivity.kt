package com.buggy.lunga

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
import com.buggy.kotlinml.runtime.KotlinML
import com.buggy.kotlinml.runtime.KotlinMLModifiers
import com.buggy.lunga.ui.theme.LungaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LungaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TestKotlinMLScreen()
                }
            }
        }
    }
}

@Composable
fun TestKotlinMLScreen() {
    var counter by remember { mutableStateOf(0) }
    var textInput by remember { mutableStateOf("") }

    // 🎯 Testing KotlinML DSL - This should work now!
    KotlinML {
        column(
            modifier = KotlinMLModifiers.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            text(
                text = "🚀 KotlinML DSL Test",
                fontSize = 24,
                fontWeight = FontWeight.Bold
            )

            text(
                text = "If you can see this, the imports are working! ✅",
                fontSize = 16,
                color = Color.Green
            )

            spacer(height = 16.dp)

            card(
                modifier = KotlinMLModifiers.fillMaxWidth(),
                elevation = 8.dp
            ) {
                column(
                    modifier = KotlinMLModifiers.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    text(
                        text = "Counter Test",
                        fontSize = 18,
                        fontWeight = FontWeight.SemiBold
                    )

                    spacer(height = 8.dp)

                    text(
                        text = "Count: $counter",
                        fontSize = 20
                    )

                    spacer(height = 12.dp)

                    row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        button(
                            text = "-",
                            onClick = { counter-- }
                        )

                        button(
                            text = "🔄",
                            onClick = { counter = 0 }
                        )

                        button(
                            text = "+",
                            onClick = { counter++ }
                        )
                    }
                }
            }

            card(
                modifier = KotlinMLModifiers.fillMaxWidth(),
                elevation = 8.dp
            ) {
                column(
                    modifier = KotlinMLModifiers.padding(16.dp)
                ) {
                    text(
                        text = "Text Input Test",
                        fontSize = 18,
                        fontWeight = FontWeight.SemiBold
                    )

                    spacer(height = 8.dp)

                    textField(
                        value = textInput,
                        onValueChange = { textInput = it },
                        placeholder = "Type something...",
                        modifier = KotlinMLModifiers.fillMaxWidth()
                    )

                    spacer(height = 8.dp)

                    if (textInput.isNotEmpty()) {
                        text(
                            text = "You typed: \"$textInput\"",
                        )
                    }
                }
            }

            card(
                modifier = KotlinMLModifiers.fillMaxWidth(),
                elevation = 4.dp
            ) {
                column(
                    modifier = KotlinMLModifiers.padding(16.dp)
                ) {
                    text(
                        text = "✅ Success Indicators:",
                        fontSize = 16,
                        fontWeight = FontWeight.Medium,
                        color = Color.Green
                    )

                    spacer(height = 8.dp)

                    text("• KotlinML DSL imports working")
                    text("• Compose dependencies resolved")
                    text("• Counter buttons responsive")
                    text("• Text input functional")
                    text("• Cards and layouts rendering")

                    spacer(height = 12.dp)

                    text(
                        text = "🎯 Ready for ML Kit integration!",
                        fontSize = 14,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}