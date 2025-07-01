package com.buggy.kotlinml.runtime

// 🚀 Core Compose Imports - These should now work
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * DSL Marker to prevent implicit receivers from outer scopes
 */
@DslMarker
annotation class KotlinMLDsl

/**
 * Main DSL scope for building KotlinML UI
 */
@KotlinMLDsl
class KotlinMLScope {
    // Internal list to store all child composables
    internal val children = mutableListOf<@Composable () -> Unit>()

    /**
     * Add a text component
     */
    fun text(
        text: String,
        color: Color = Color.Unspecified,
        fontSize: Int? = null,
        fontWeight: FontWeight? = null,
        textAlign: TextAlign? = null,
        modifier: Modifier = Modifier
    ) {
        children.add {
            Text(
                text = text,
                color = color,
                fontSize = fontSize?.sp ?: LocalTextStyle.current.fontSize,
                fontWeight = fontWeight,
                textAlign = textAlign,
                modifier = modifier
            )
        }
    }

    /**
     * Add a button component
     */
    fun button(
        text: String,
        onClick: () -> Unit,
        enabled: Boolean = true,
        modifier: Modifier = Modifier
    ) {
        children.add {
            Button(
                onClick = onClick,
                enabled = enabled,
                modifier = modifier
            ) {
                Text(text)
            }
        }
    }

    /**
     * Add an outlined button component
     */
    fun outlinedButton(
        text: String,
        onClick: () -> Unit,
        enabled: Boolean = true,
        modifier: Modifier = Modifier
    ) {
        children.add {
            OutlinedButton(
                onClick = onClick,
                enabled = enabled,
                modifier = modifier
            ) {
                Text(text)
            }
        }
    }

    /**
     * Add a text field component
     */
    fun textField(
        value: String,
        onValueChange: (String) -> Unit,
        placeholder: String? = null,
        label: String? = null,
        modifier: Modifier = Modifier
    ) {
        children.add {
            TextField(
                value = value,
                onValueChange = onValueChange,
                placeholder = placeholder?.let { { Text(it) } },
                label = label?.let { { Text(it) } },
                modifier = modifier
            )
        }
    }

    /**
     * Add a column layout
     */
    fun column(
        modifier: Modifier = Modifier,
        verticalArrangement: Arrangement.Vertical = Arrangement.Top,
        horizontalAlignment: Alignment.Horizontal = Alignment.Start,
        content: KotlinMLScope.() -> Unit
    ) {
        val scope = KotlinMLScope()
        scope.content()
        children.add {
            Column(
                modifier = modifier,
                verticalArrangement = verticalArrangement,
                horizontalAlignment = horizontalAlignment
            ) {
                scope.render()
            }
        }
    }

    /**
     * Add a row layout
     */
    fun row(
        modifier: Modifier = Modifier,
        horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
        verticalAlignment: Alignment.Vertical = Alignment.Top,
        content: KotlinMLScope.() -> Unit
    ) {
        val scope = KotlinMLScope()
        scope.content()
        children.add {
            Row(
                modifier = modifier,
                horizontalArrangement = horizontalArrangement,
                verticalAlignment = verticalAlignment
            ) {
                scope.render()
            }
        }
    }

    /**
     * Add a card component
     */
    fun card(
        modifier: Modifier = Modifier,
        elevation: Dp = 4.dp,
        content: KotlinMLScope.() -> Unit
    ) {
        val scope = KotlinMLScope()
        scope.content()
        children.add {
            Card(
                modifier = modifier,
                elevation = CardDefaults.cardElevation(defaultElevation = elevation)
            ) {
                scope.render()
            }
        }
    }

    /**
     * Add a spacer component
     */
    fun spacer(
        height: Dp? = null,
        width: Dp? = null
    ) {
        children.add {
            Spacer(
                modifier = Modifier.then(
                    when {
                        height != null && width != null -> Modifier.size(width, height)
                        height != null -> Modifier.height(height)
                        width != null -> Modifier.width(width)
                        else -> Modifier.size(8.dp) // default spacing
                    }
                )
            )
        }
    }

    /**
     * Add a checkbox component
     */
    fun checkbox(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        enabled: Boolean = true,
        modifier: Modifier = Modifier
    ) {
        children.add {
            Checkbox(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                modifier = modifier
            )
        }
    }

    /**
     * Add a switch component
     */
    fun switch(
        checked: Boolean,
        onCheckedChange: (Boolean) -> Unit,
        enabled: Boolean = true,
        modifier: Modifier = Modifier
    ) {
        children.add {
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                enabled = enabled,
                modifier = modifier
            )
        }
    }

    /**
     * Render all children components
     */
    @Composable
    internal fun render() {
        children.forEach { child ->
            child()
        }
    }
}

/**
 * Main entry point for KotlinML DSL
 */
@Composable
fun KotlinML(content: KotlinMLScope.() -> Unit) {
    val scope = KotlinMLScope()
    scope.content()
    scope.render()
}

/**
 * Utility functions for creating common modifiers
 */
object KotlinMLModifiers {

    fun padding(all: Dp) = Modifier.padding(all)

    fun padding(
        horizontal: Dp = 0.dp,
        vertical: Dp = 0.dp
    ) = Modifier.padding(horizontal = horizontal, vertical = vertical)

    fun padding(
        start: Dp = 0.dp,
        top: Dp = 0.dp,
        end: Dp = 0.dp,
        bottom: Dp = 0.dp
    ) = Modifier.padding(start = start, top = top, end = end, bottom = bottom)

    fun fillMaxWidth() = Modifier.fillMaxWidth()

    fun fillMaxHeight() = Modifier.fillMaxHeight()

    fun fillMaxSize() = Modifier.fillMaxSize()

    fun size(width: Dp, height: Dp) = Modifier.size(width, height)

    fun size(size: Dp) = Modifier.size(size)

}