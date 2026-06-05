package com.example.cmpmemecreator.meme_editor.data

import android.content.Context
import com.example.cmpmemecreator.meme_editor.domain.SaveToStorageStrategy
import java.io.File

actual class CacheToStorageStrategy(
    private val context: Context
) : SaveToStorageStrategy {
    actual override fun getFilePath(fileName: String): String {
        return File(context.cacheDir, fileName).absolutePath
    }
}