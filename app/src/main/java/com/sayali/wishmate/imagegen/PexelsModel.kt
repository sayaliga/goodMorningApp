package com.sayali.wishmate.pexels

data class PexelsSearchResponse(
    val page: Int,
    val per_page: Int,
    val total_results: Int,
    val next_page: String? = null,
    val prev_page: String? = null,
    val photos: List<PexelsPhoto> = emptyList()
)

data class PexelsPhoto(
    val id: Long,
    val width: Int,
    val height: Int,
    val url: String,
    val photographer: String,
    val photographer_url: String,
    val photographer_id: Long,
    val avg_color: String? = null,
    val src: PexelsSrc,
    val alt: String? = null
)

data class PexelsSrc(
    val original: String,
    val large: String,
    val large2x: String,
    val medium: String,
    val small: String,
    val portrait: String,
    val landscape: String,
    val tiny: String
)
