package com.example.cmpmemecreator.di

import com.example.cmpmemecreator.meme_editor.presentation.MemeEditorViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

expect val platformModule: Module

val appModule = module {
    viewModelOf(::MemeEditorViewModel)
    includes(platformModule)
}