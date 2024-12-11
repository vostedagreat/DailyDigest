package com.example.dailydigest.modules

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dailydigest.ui.theme.BlueLight

@Composable
fun AppButtonInv(
    modifier: Modifier = Modifier.padding(vertical = 10.dp).fillMaxWidth(),
    colors : ButtonColors = ButtonColors(
        containerColor = Color.White,
        contentColor = BlueLight,
        disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
        disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer
    ),
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Button(
        colors = colors,
        onClick = onClick,
        modifier = modifier.height(54.dp),
        content = { content() },
        border = BorderStroke(2.dp, BlueLight)
    )
}

