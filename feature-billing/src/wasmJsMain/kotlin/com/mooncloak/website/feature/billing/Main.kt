package com.mooncloak.website.feature.billing

import com.mooncloak.website.feature.shared.runFeature

// RUN: ./gradlew :$moduleName:wasmJsRun (ex: ./gradlew :feature-billing:wasmJsRun)
public fun main() {
    runFeature(BillingFeature)
}
