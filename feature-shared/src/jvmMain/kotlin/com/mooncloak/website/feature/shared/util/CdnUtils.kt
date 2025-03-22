package com.mooncloak.website.feature.shared.util

import com.mooncloak.website.feature.shared.BASE_CDN_APP_PATH
import com.mooncloak.website.feature.shared.BASE_CDN_PATH
import com.mooncloak.website.feature.shared.Feature

@Suppress("NOTHING_TO_INLINE")
internal inline fun cdnPath(path: String): String =
    if (path.startsWith('/')) {
        "${Feature.BASE_CDN_PATH}$path"
    } else {
        "${Feature.BASE_CDN_PATH}/$path"
    }

@Suppress("NOTHING_TO_INLINE")
internal inline fun cdnAppPath(path: String): String =
    if (path.startsWith('/')) {
        "${Feature.BASE_CDN_APP_PATH}$path"
    } else {
        "${Feature.BASE_CDN_APP_PATH}/$path"
    }
