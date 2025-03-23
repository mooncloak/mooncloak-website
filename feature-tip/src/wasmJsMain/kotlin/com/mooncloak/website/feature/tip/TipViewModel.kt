package com.mooncloak.website.feature.tip

import androidx.compose.runtime.Stable
import com.mooncloak.kodetools.statex.ViewModel
import com.mooncloak.website.feature.tip.model.TipLinks
import kotlinx.coroutines.launch

@Stable
public class TipViewModel public constructor() : ViewModel<TipStateModel>(initialStateValue = TipStateModel()) {

    public fun load() {
        coroutineScope.launch {
            emit { current ->
                current.copy(
                    items = TipLinks.all,
                    isLoading = false
                )
            }
        }
    }
}
