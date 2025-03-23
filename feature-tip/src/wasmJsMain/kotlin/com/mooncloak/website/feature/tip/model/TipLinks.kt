package com.mooncloak.website.feature.tip.model

import com.mooncloak.website.feature.tip.*
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Suppress("MemberVisibilityCanBePrivate")
public object TipLinks {

    public val MoonlitCoffee: TipLinkItem = TipLinkItem(
        url = "https://donate.stripe.com/9AQ7vP08BbYJ0vu7ss",
        price = "$1",
        title = { stringResource(Res.string.tip_link_item_title_moonlit_coffee) },
        description = { stringResource(Res.string.tip_link_item_description_moonlit_coffee) },
        icon = { painterResource(Res.drawable.coffee) }
    )
    public val LunarBeer: TipLinkItem = TipLinkItem(
        url = "https://donate.stripe.com/14k2bv08Bd2Na64aEF",
        price = "$5",
        title = { stringResource(Res.string.tip_link_item_title_lunar_beer) },
        description = { stringResource(Res.string.tip_link_item_description_lunar_beer) },
        icon = { painterResource(Res.drawable.beer) }
    )
    public val CosmicCocktail: TipLinkItem = TipLinkItem(
        url = "https://donate.stripe.com/bIYbM5aNf1k5dig4gi",
        price = "$10",
        title = { stringResource(Res.string.tip_link_item_title_cosmic_cocktail) },
        description = { stringResource(Res.string.tip_link_item_description_cosmic_cocktail) },
        icon = { painterResource(Res.drawable.cocktail) }
    )
    public val StellarMix: TipLinkItem = TipLinkItem(
        url = "https://donate.stripe.com/6oE7vP1cF4whfqo147",
        price = "Custom",
        title = { stringResource(Res.string.tip_link_item_title_stellar_mix) },
        description = { stringResource(Res.string.tip_link_item_description_stellar_mix) },
        icon = { painterResource(Res.drawable.blender) }
    )

    public val all: List<TipLinkItem> = listOf(
        MoonlitCoffee,
        LunarBeer,
        CosmicCocktail,
        StellarMix
    )
}
