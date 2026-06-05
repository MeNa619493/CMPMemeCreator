@file:OptIn(ExperimentalComposeUiApi::class)

package com.example.cmpmemecreator.meme_editor.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.adaptive.currentWindowSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cmpmemecreator.shared.generated.resources.Res
import cmpmemecreator.shared.generated.resources.cancel
import cmpmemecreator.shared.generated.resources.leave
import cmpmemecreator.shared.generated.resources.leave_editor_message
import cmpmemecreator.shared.generated.resources.leave_editor_title
import cmpmemecreator.shared.generated.resources.meme_template_01
import com.example.cmpmemecreator.core.presentation.MemeTemplate
import com.example.cmpmemecreator.core.theme.MemeCreatorTheme
import com.example.cmpmemecreator.meme_editor.presentation.components.BottomBar
import com.example.cmpmemecreator.meme_editor.presentation.components.ConfirmationDialog
import com.example.cmpmemecreator.meme_editor.presentation.components.ConfirmationDialogConfig
import com.example.cmpmemecreator.meme_editor.presentation.components.DraggableContainer
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MemeEditorRoot(
    template: MemeTemplate,
    onGoBack: () -> Unit,
    viewModel: MemeEditorViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state.hasLeftEditor) {
        if (state.hasLeftEditor) {
            onGoBack()
        }
    }

    MemeEditorScreen(
        template = template,
        state = state,
        onAction = viewModel::onAction
    )
}

@Composable
fun MemeEditorScreen(
    template: MemeTemplate,
    state: MemeEditorState,
    onAction: (MemeEditorAction) -> Unit,
) {
    BackHandler(
        enabled = !state.isLeavingWithoutSaving
    ) {
        onAction(MemeEditorAction.OnGoBackClick)
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures {
                    onAction(MemeEditorAction.OnTapOutsideSelectedText)
                }
            },
        bottomBar = {
            BottomBar(
                onAddTextClick = {
                    onAction(MemeEditorAction.OnAddTextClick)
                },
                onSaveClick = {
                    onAction(MemeEditorAction.OnSaveMemeClick(template))
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            val windowSize = currentWindowSize()
            Box {
                Image(
                    painter = painterResource(template.drawable),
                    contentDescription = null,
                    modifier = Modifier
                        .then(
                            if (windowSize.width > windowSize.height) {
                                Modifier.fillMaxHeight()
                            } else {
                                Modifier.fillMaxWidth()
                            }
                        )
                        .onSizeChanged {
                            onAction(MemeEditorAction.OnContainerSizeChange(it))
                        },
                    contentScale = if (windowSize.width > windowSize.height) {
                        ContentScale.FillHeight
                    } else {
                        ContentScale.FillWidth
                    }
                )

                DraggableContainer(
                    children = state.memeTexts,
                    textBoxInteractionState = state.textBoxInteractionState,
                    modifier = Modifier
                        .matchParentSize(),
                    onChildTransformChanged = { id, offset, rotation, scale ->
                        onAction(
                            MemeEditorAction.OnMemeTextTransformChange(
                                id = id,
                                offset = offset,
                                rotation = rotation,
                                scale = scale
                            )
                        )
                    },
                    onChildClick = { id ->
                        onAction(MemeEditorAction.OnSelectMemeText(id))
                    },
                    onChildDoubleClick = { id ->
                        onAction(MemeEditorAction.OnEditMemeText(id))
                    },
                    onChildTextChange = { id, text ->
                        onAction(MemeEditorAction.OnMemeTextChange(id, text))
                    },
                    onChildDeleteClick = { id ->
                        onAction(MemeEditorAction.OnDeleteMemeTextClick(id))
                    }
                )
            }

            IconButton(
                onClick = {
                    onAction(MemeEditorAction.OnGoBackClick)
                },
                modifier = Modifier
                    .align(Alignment.TopStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }
    }

    if (state.isLeavingWithoutSaving) {
        ConfirmationDialog(
            config = ConfirmationDialogConfig(
                title = stringResource(Res.string.leave_editor_title),
                message = stringResource(Res.string.leave_editor_message),
                confirmButtonText = stringResource(Res.string.leave),
                cancelButtonText = stringResource(Res.string.cancel),
                confirmButtonColor = MaterialTheme.colorScheme.secondary
            ),
            onConfirm = {
                onAction(MemeEditorAction.OnConfirmLeaveWithoutSaving)
            },
            onDismiss = {
                onAction(MemeEditorAction.OnDismissLeaveWithoutSaving)
            }
        )
    }
}

@Preview
@Composable
private fun Preview() {
    MemeCreatorTheme {
        MemeEditorScreen(
            template = MemeTemplate(
                id = "meme_template_01",
                drawable = Res.drawable.meme_template_01
            ),
            state = MemeEditorState(),
            onAction = {}
        )
    }
}