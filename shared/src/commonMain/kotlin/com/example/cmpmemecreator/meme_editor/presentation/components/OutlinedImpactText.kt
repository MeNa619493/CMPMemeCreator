package com.example.cmpmemecreator.meme_editor.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.cmpmemecreator.core.theme.MemeCreatorTheme
import com.example.cmpmemecreator.meme_editor.presentation.utils.rememberFillTextStyle
import com.example.cmpmemecreator.meme_editor.presentation.utils.rememberStrokeTextStyle

@Composable
fun OutlinedImpactText(
    text: String,
    strokeTextStyle: TextStyle,
    fillTextStyle: TextStyle,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier) {
        Text(
            text = text,
            style = strokeTextStyle
        )
        Text(
            text = text,
            style = fillTextStyle
        )
    }
}

@Composable
@Preview
fun OutlinedImpactTextPreview() {
    MemeCreatorTheme {
        OutlinedImpactText(
            text = "HELLO WORLD!",
            strokeTextStyle = rememberStrokeTextStyle(),
            fillTextStyle = rememberFillTextStyle()
        )
    }
}