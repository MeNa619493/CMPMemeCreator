package com.example.cmpmemecreator.di

import com.example.cmpmemecreator.meme_editor.data.CacheToStorageStrategy
import com.example.cmpmemecreator.meme_editor.data.PlatformMemeExporter
import com.example.cmpmemecreator.meme_editor.domain.MemeExporter
import com.example.cmpmemecreator.meme_editor.domain.SaveToStorageStrategy
import com.example.cmpmemecreator.meme_editor.presentation.utils.PlatformShareSheet
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

actual val platformModule = module {
    factoryOf(::PlatformMemeExporter) bind MemeExporter::class
    factoryOf(::CacheToStorageStrategy) bind SaveToStorageStrategy::class
    factoryOf(::PlatformShareSheet)
}