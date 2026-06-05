@file:OptIn(ExperimentalUuidApi::class)

package com.example.cmpmemecreator.meme_editor.presentation

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntSize
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cmpmemecreator.core.presentation.MemeTemplate
import com.example.cmpmemecreator.meme_editor.domain.MemeExporter
import com.example.cmpmemecreator.meme_editor.domain.SaveToStorageStrategy
import com.example.cmpmemecreator.meme_editor.presentation.utils.PlatformShareSheet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getDrawableResourceBytes
import org.jetbrains.compose.resources.getSystemResourceEnvironment
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class MemeEditorViewModel(
    private val memeExporter: MemeExporter,
    private val storageStrategy: SaveToStorageStrategy,
    private val shareSheet: PlatformShareSheet
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(MemeEditorState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = MemeEditorState()
        )

    fun onAction(action: MemeEditorAction) {
        when (action) {
            is MemeEditorAction.OnAddTextClick -> addText()
            is MemeEditorAction.OnConfirmLeaveWithoutSaving -> confirmLeave()
            is MemeEditorAction.OnContainerSizeChange -> updateContainerSize(action.size)
            is MemeEditorAction.OnDeleteMemeTextClick -> deleteMemeText(action.id)
            is MemeEditorAction.OnDismissLeaveWithoutSaving -> dismissConfirmLeaveDialog()
            is MemeEditorAction.OnEditMemeText -> editMemeText(action.id)
            is MemeEditorAction.OnGoBackClick -> attemptToGoBack()
            is MemeEditorAction.OnMemeTextChange -> updateMemeText(action.id, action.text)
            is MemeEditorAction.OnSaveMemeClick -> saveMeme(action.memeTemplate)
            is MemeEditorAction.OnSelectMemeText -> selectMemeText(action.id)
            is MemeEditorAction.OnTapOutsideSelectedText -> unselectMemeText()
            is MemeEditorAction.OnMemeTextTransformChange -> {
                memeTextTransformChange(
                    id = action.id,
                    offset = action.offset,
                    rotation = action.rotation,
                    scale = action.scale
                )
            }
        }
    }

    private fun updateContainerSize(size: IntSize) {
        _state.update {
            it.copy(
                templateSize = size
            )
        }
    }

    private fun editMemeText(id: String) {
        _state.update {
            it.copy(
                textBoxInteractionState = TextBoxInteractionState.Editing(id)
            )
        }
    }

    private fun updateMemeText(id: String, text: String) {
        _state.update {
            it.copy(
                memeTexts = it.memeTexts.map { memeText ->
                    if (memeText.id == id) {
                        memeText.copy(text = text)
                    } else memeText
                }
            )
        }
    }

    private fun selectMemeText(id: String) {
        _state.update {
            it.copy(
                textBoxInteractionState = TextBoxInteractionState.Selected(id)
            )
        }
    }

    private fun deleteMemeText(id: String) {
        _state.update {
            it.copy(
                memeTexts = it.memeTexts.filter { memeText ->
                    memeText.id != id
                }
            )
        }
    }

    private fun memeTextTransformChange(
        id: String,
        offset: Offset,
        rotation: Float,
        scale: Float
    ) {
        _state.update {
            val (width, height) = it.templateSize
            it.copy(
                memeTexts = it.memeTexts.map { memeText ->
                    if (memeText.id == id) {
                        memeText.copy(
                            offsetRatioX = offset.x / width,
                            offsetRatioY = offset.y / height,
                            rotation = rotation,
                            scale = scale
                        )
                    } else memeText
                }
            )
        }
    }

    private fun addText() {
        val id = Uuid.random().toString()

        val memeText = MemeText(
            id = id,
            text = "TAP TO EDIT",
            offsetRatioX = 0.25f,
            offsetRatioY = 0.25f,
        )

        _state.update {
            it.copy(
                memeTexts = it.memeTexts + memeText,
                textBoxInteractionState = TextBoxInteractionState.Selected(id)
            )
        }
    }

    private fun unselectMemeText() {
        _state.update {
            it.copy(
                textBoxInteractionState = TextBoxInteractionState.None
            )
        }
    }

    private fun attemptToGoBack() {
        if (state.value.memeTexts.isEmpty()) {
            _state.update {
                it.copy(
                    hasLeftEditor = true
                )
            }
        } else {
            _state.update {
                it.copy(
                    isLeavingWithoutSaving = true
                )
            }
        }
    }

    private fun dismissConfirmLeaveDialog() {
        _state.update {
            it.copy(
                isLeavingWithoutSaving = false
            )
        }
    }

    private fun confirmLeave() {
        _state.update {
            it.copy(
                hasLeftEditor = true
            )
        }
    }

    private fun saveMeme (memeTemplate: MemeTemplate) {
        viewModelScope.launch {
            memeExporter
                .exportMeme(
                    backgroundImageBytes = getDrawableResourceBytes(
                        environment = getSystemResourceEnvironment(),
                        resource = memeTemplate.drawable
                    ),
                    memeTexts = state.value.memeTexts,
                    templateSize = state.value.templateSize,
                    saveToStorageStrategy = storageStrategy
                )
                .onSuccess {
                    shareSheet.shareFile(it)
                }
                .onFailure {
                    it.printStackTrace()
                }
        }
    }
}