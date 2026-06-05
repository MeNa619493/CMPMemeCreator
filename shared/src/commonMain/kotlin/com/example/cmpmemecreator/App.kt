package com.example.cmpmemecreator

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.cmpmemecreator.core.presentation.NavigationRoot
import com.example.cmpmemecreator.core.theme.MemeCreatorTheme

@Composable
@Preview
fun App() {
    MemeCreatorTheme {
        NavigationRoot()
    }
}