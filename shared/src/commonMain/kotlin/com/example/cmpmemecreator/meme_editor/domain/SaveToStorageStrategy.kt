package com.example.cmpmemecreator.meme_editor.domain

interface SaveToStorageStrategy {
    fun getFilePath(fileName: String): String
}