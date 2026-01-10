package com.kavi.pbc.web.data.quote

import kotlinx.serialization.Serializable

@Serializable
data class DailyQuote(
    val dailyQuoteList: List<Quote>,
    val date: Long
)