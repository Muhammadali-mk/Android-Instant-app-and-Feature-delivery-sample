package uz.sample.featuredeliveryapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Green40,
    secondary = PurpleGrey80,
    tertiary = Pink80,
    onSurface = Gray,
    onBackground = Color.White
)

private val LightColorScheme = lightColorScheme(
    primary = Green40,
    secondary = PurpleGrey40,
    tertiary = Pink40,
    onSurface = Gray,
    onBackground = Color.White
)

@Composable
fun FeatureDeliveryAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}