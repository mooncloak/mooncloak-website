@file:Suppress("MemberVisibilityCanBePrivate", "ConstPropertyName")

interface BuildConstants {

    object Android {

        const val compileSdkVersion = 35
        const val minSdkVersion = 23
        const val targetSdkVersion = 35
    }

    companion object : BuildConstants
}
