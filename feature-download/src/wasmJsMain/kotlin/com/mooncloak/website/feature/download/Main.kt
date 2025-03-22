package com.mooncloak.website.feature.download

import com.mooncloak.website.feature.shared.runFeature

// RUN: ./gradlew :$moduleName:wasmJsRun (ex: ./gradlew :feature-download:wasmJsRun)
public fun main() {
    runFeature(DownloadFeature)
}
