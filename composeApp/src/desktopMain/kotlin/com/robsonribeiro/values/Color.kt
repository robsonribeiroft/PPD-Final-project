package com.robsonribeiro.values

import androidx.compose.ui.graphics.Color
import kotlin.math.max

fun Color.darken(factor: Float): Color {
    // Ensure the factor is positive to avoid invalid calculations
    // If factor is 0, it would lead to division by zero. If factor < 1, it lightens.
    // We assume 'darken' means factor >= 1.
    val actualFactor = if (factor <= 0f) 1.0f else factor

    // Extract ARGB components as floats (0.0f to 1.0f)
    val red = this.red
    val green = this.green
    val blue = this.blue
    val alpha = this.alpha

    // Apply the darkening factor by dividing each component
    val newRed = red / actualFactor
    val newGreen = green / actualFactor
    val newBlue = blue / actualFactor

    // Clamp the new component values between 0.0f and 1.0f
    // Use max(0.0f, ...) to ensure values don't go below 0
    val clampedRed = max(0.0f, newRed)
    val clampedGreen = max(0.0f, newGreen)
    val clampedBlue = max(0.0f, newBlue)

    // Return a new Color with the darkened RGB and original alpha
    return Color(clampedRed, clampedGreen, clampedBlue, alpha)
}

fun Color.alpha(factor: Float) = this.copy(alpha=factor)

// Extension properties for common colors, making them accessible directly on the Color type.
val Color.Companion.BlackRich get() = Color(0xFF041619)
val Color.Companion.BlueCeltic get() = Color(0xFF3870B2)
val Color.Companion.BluePrussian get() = Color(0xFF083042)
val Color.Companion.BlueRoyal get() = Color(0xFF0A2463)
val Color.Companion.BlueRoyalDarker get() = Color(0xFF081D4E)
val Color.Companion.GreenEmerald get() = Color(0xFF37BF6E)
val Color.Companion.RedPantone get() = Color(0xFFE63946)
val Color.Companion.RedPantoneDarker get() = Color(0xFFCB1928)
val Color.Companion.BaseBackground get() = Color(0xFFe8eaed)
val Color.Companion.White get() = Color(0xFFFFFFFF)
val Color.Companion.BackgroundGradient: List<Color>
    get() = listOf(
        BlueRoyalDarker, BlueRoyalDarker,
        BlackRich, BlackRich, BlackRich,
        BlueRoyalDarker, BlueRoyalDarker,
        BlackRich, BlackRich,
        BlueRoyalDarker, BlueRoyalDarker,
        BlackRich, BlackRich
    )

/**
 * Object holding common color resources for the application, specifically gradients.
 * Individual colors are now defined as extension properties on the [Color] class.
 */
object ColorResources {
    val background_gradient = listOf(
        Color.BlueRoyalDarker, Color.BlueRoyalDarker,
        Color.BlackRich, Color.BlackRich, Color.BlackRich,
        Color.BlueRoyalDarker, Color.BlueRoyalDarker,
        Color.BlackRich, Color.BlackRich,
        Color.BlueRoyalDarker, Color.BlueRoyalDarker,
        Color.BlackRich, Color.BlackRich
    )

    val defeat_gradient = listOf(
        Color.RedPantone.darken(1.3f), // Using the new darken extension
        Color.RedPantone,
        Color.RedPantone.darken(0.3f) // This will effectively lighten slightly if factor < 1
    )

    val win_gradient = listOf(
        Color.BlueCeltic.darken(1.3f), // Using the new darken extension
        Color.BlueCeltic,
        Color.BlueCeltic.darken(0.3f) // This will effectively lighten slightly if factor < 1
    )
}