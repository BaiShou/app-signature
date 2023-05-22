package com.arnold.tiny.bean

import java.io.Serializable

data class ImageSlimmingModel(val inputDir: String, val outputDir: String, val filePrefix: String, val rename: String) : Serializable