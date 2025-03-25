package com.mooncloak.website.feature.billing

import com.mooncloak.kodetools.statex.ViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

public class BillingViewModel public constructor(

) : ViewModel<BillingStateModel>(
    initialStateValue = BillingStateModel()
) {

    private val mutex = Mutex(locked = false)

    public fun load() {
        coroutineScope.launch {
            mutex.withLock {

            }
        }
    }
}
