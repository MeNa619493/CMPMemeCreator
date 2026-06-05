package com.example.cmpmemecreator.core.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.cmpmemecreator.meme_editor.presentation.MemeEditorRoot
import com.example.cmpmemecreator.meme_gallery.presentation.MemeGalleryScreen

@Composable
fun NavigationRoot() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Route.MemeGallery
    ) {
        composable<Route.MemeGallery> {
            MemeGalleryScreen(
                onMemeTemplateSelected = { memeTemplate ->
                    navController.navigate(Route.MemeEditor(memeTemplate.id))
                }
            )
        }
        composable<Route.MemeEditor> {
            val templateId = it.toRoute<Route.MemeEditor>().templateId
            val template = remember(templateId) {
                memeTemplates.first { memeTemplate ->
                    memeTemplate.id == templateId
                }
            }
            MemeEditorRoot(
                template = template,
                onGoBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}