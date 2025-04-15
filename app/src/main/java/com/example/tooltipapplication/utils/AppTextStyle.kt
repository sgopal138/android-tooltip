package com.example.tooltipapplication.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val headingStyle: TextStyle
    @Composable get() = appTextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)

val bodyStyle: TextStyle
    @Composable get() = appTextStyle(fontSize = 14.sp)

val captionStyle: TextStyle
    @Composable get() = appTextStyle(fontSize = 12.sp, color = Color.Gray)




