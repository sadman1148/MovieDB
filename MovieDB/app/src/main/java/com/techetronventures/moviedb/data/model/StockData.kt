package com.techetronventures.moviedb.data.model

import java.util.Date

data class StockData(
    val timestamp: Date,
    val open: Float,
    val high: Float,
    val low: Float,
    val close: Float,
    val volume: Float
)