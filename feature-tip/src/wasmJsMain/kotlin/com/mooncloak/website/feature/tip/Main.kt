package com.mooncloak.website.feature.tip

import com.mooncloak.website.feature.shared.runFeature

// RUN: ./gradlew :$moduleName:wasmJsRun (ex: ./gradlew :feature-tip:wasmJsRun)
public fun main() {
    runFeature(TipFeature)
}
