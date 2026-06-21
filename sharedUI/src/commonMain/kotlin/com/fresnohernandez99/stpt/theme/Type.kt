package com.fresnohernandez99.stpt.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.Font
import speechtospeechtranslator.sharedui.generated.resources.Res
import speechtospeechtranslator.sharedui.generated.resources.sf_pro_display_bold
import speechtospeechtranslator.sharedui.generated.resources.sf_pro_display_medium
import speechtospeechtranslator.sharedui.generated.resources.sf_pro_display_regular
import speechtospeechtranslator.sharedui.generated.resources.sf_pro_display_semibold

@Composable
fun sfProDisplayFontFamily() = FontFamily(
    Font(resource = Res.font.sf_pro_display_regular, weight = FontWeight.Normal),
    Font(resource = Res.font.sf_pro_display_medium, weight = FontWeight.Medium),
    Font(resource = Res.font.sf_pro_display_bold, weight = FontWeight.Bold),
    Font(resource = Res.font.sf_pro_display_semibold, weight = FontWeight.SemiBold)
)

@Composable
fun appTypography(colorScheme: ColorScheme): Typography {
    val sfPro = sfProDisplayFontFamily()
    return Typography(
        // H1 Title
        titleLarge = TextStyle(
            fontFamily = sfPro,
            fontWeight = FontWeight.Bold,
            fontSize = 54.sp,
            color = colorScheme.onBackground,
            letterSpacing = 0.sp
        ),

        // H2
        titleMedium = TextStyle(
            fontFamily = sfPro,
            fontWeight = FontWeight.SemiBold,
            fontSize = 40.sp,
            color = colorScheme.onBackground,
            letterSpacing = 0.sp
        ),

        // H3
        titleSmall = TextStyle(
            fontFamily = sfPro,
            fontWeight = FontWeight.Medium,
            fontSize = 32.sp,
            color = colorScheme.onBackground,
            letterSpacing = 0.sp
        ),

        // H4
        bodyLarge = TextStyle(
            fontFamily = sfPro,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            letterSpacing = 0.sp,
            color = colorScheme.onBackground,
        ),

        // H5
        bodyMedium = TextStyle(
            fontFamily = sfPro,
            fontWeight = FontWeight.Light,
            fontSize = 16.sp,
            letterSpacing = 0.sp,
            color = colorScheme.onBackground,
        ),

        // H6
        bodySmall = TextStyle(
            fontFamily = sfPro,
            fontWeight = FontWeight.Light,
            fontSize = 8.sp,
            letterSpacing = 0.sp,
            color = colorScheme.onBackground,
        ),
    )
}

@Composable
fun appTypographyBig(colorScheme: ColorScheme): Typography {
    val sfPro = sfProDisplayFontFamily()
    return Typography(
        // H1 Title
        titleLarge = TextStyle(
            fontFamily = sfPro,
            fontWeight = FontWeight.Bold,
            fontSize = 60.sp,
            color = colorScheme.onBackground,
            letterSpacing = 0.sp
        ),

        // H2
        titleMedium = TextStyle(
            fontFamily = sfPro,
            fontWeight = FontWeight.SemiBold,
            fontSize = 46.sp,
            color = colorScheme.onBackground,
            letterSpacing = 0.sp
        ),

        // H3
        titleSmall = TextStyle(
            fontFamily = sfPro,
            fontWeight = FontWeight.Medium,
            fontSize = 38.sp,
            color = colorScheme.onBackground,
            letterSpacing = 0.sp
        ),

        // H4
        bodyLarge = TextStyle(
            fontFamily = sfPro,
            fontWeight = FontWeight.Normal,
            fontSize = 30.sp,
            letterSpacing = 0.sp,
            color = colorScheme.onBackground,
        ),

        // H5
        bodyMedium = TextStyle(
            fontFamily = sfPro,
            fontWeight = FontWeight.Light,
            fontSize = 22.sp,
            letterSpacing = 0.sp,
            color = colorScheme.onBackground,
        ),

        // H6
        bodySmall = TextStyle(
            fontFamily = sfPro,
            fontWeight = FontWeight.Light,
            fontSize = 14.sp,
            letterSpacing = 0.sp,
            color = colorScheme.onBackground,
        ),
    )
}
