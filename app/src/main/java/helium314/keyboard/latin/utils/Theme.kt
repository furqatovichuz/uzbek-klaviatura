// SPDX-License-Identifier: GPL-3.0-only
package helium314.keyboard.latin.utils

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

// Brend ranglari (logotip ko'ki va O'zbekiston bayrog'i yashili). Qurilma mavzusi (kun/tun) bilan almashadi.
// Fon rasmidan olinadigan dinamik ranglar ataylab ishlatilmaydi.
private val lightColors = lightColorScheme(
    primary = Color(0xFF1B5FDB),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFD9E5FF),
    onPrimaryContainer = Color(0xFF001A44),
    secondary = Color(0xFF4F6490),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD8E2FF),
    onSecondaryContainer = Color(0xFF0A1B3C),
    tertiary = Color(0xFF1E8E3E),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFC4EFCB),
    onTertiaryContainer = Color(0xFF00210A),
    background = Color(0xFFF8F9FF),
    onBackground = Color(0xFF181C22),
    surface = Color(0xFFF8F9FF),
    onSurface = Color(0xFF181C22),
    surfaceVariant = Color(0xFFE0E5F2),
    onSurfaceVariant = Color(0xFF424751),
    surfaceTint = Color(0xFF1B5FDB),
    outline = Color(0xFF727784),
    outlineVariant = Color(0xFFC2C7D4),
    surfaceBright = Color(0xFFF8F9FF),
    surfaceDim = Color(0xFFD8DAE2),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF1F3FB),
    surfaceContainer = Color(0xFFECEEF6),
    surfaceContainerHigh = Color(0xFFE6E8F0),
    surfaceContainerHighest = Color(0xFFE0E2EA),
)

private val darkColors = darkColorScheme(
    primary = Color(0xFFAFC6FF),
    onPrimary = Color(0xFF002E6B),
    primaryContainer = Color(0xFF1B4590),
    onPrimaryContainer = Color(0xFFD9E5FF),
    secondary = Color(0xFFB4C6F3),
    onSecondary = Color(0xFF1B2E58),
    secondaryContainer = Color(0xFF33466F),
    onSecondaryContainer = Color(0xFFD8E2FF),
    tertiary = Color(0xFF8AD79B),
    onTertiary = Color(0xFF00391A),
    tertiaryContainer = Color(0xFF0B5229),
    onTertiaryContainer = Color(0xFFC4EFCB),
    background = Color(0xFF101318),
    onBackground = Color(0xFFE1E2EA),
    surface = Color(0xFF101318),
    onSurface = Color(0xFFE1E2EA),
    surfaceVariant = Color(0xFF424751),
    onSurfaceVariant = Color(0xFFC2C7D4),
    surfaceTint = Color(0xFFAFC6FF),
    outline = Color(0xFF8C919E),
    outlineVariant = Color(0xFF424751),
    surfaceBright = Color(0xFF363940),
    surfaceDim = Color(0xFF101318),
    surfaceContainerLowest = Color(0xFF0B0E13),
    surfaceContainerLow = Color(0xFF181C22),
    surfaceContainer = Color(0xFF1C2026),
    surfaceContainerHigh = Color(0xFF272A31),
    surfaceContainerHighest = Color(0xFF32353C),
)

@Composable
fun Theme(dark: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val material3 = Typography()
    MaterialTheme(
        colorScheme = if (dark) darkColors else lightColors,
        typography = Typography(
            titleLarge = material3.titleLarge.copy(fontWeight = FontWeight.Bold),
            titleMedium = material3.titleMedium.copy(fontWeight = FontWeight.Bold),
            titleSmall = material3.titleSmall.copy(fontWeight = FontWeight.Bold)
        ),
        //shapes = Shapes(),
        content = content
    )
}

const val previewDark = true
