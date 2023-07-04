package com.arnold

import java.io.File


object PluginFileUtil {
    public fun getPluginFilePath(path: String): String {
        return File(javaClass.getResource(path)?.path ?: "").absolutePath
    }
}