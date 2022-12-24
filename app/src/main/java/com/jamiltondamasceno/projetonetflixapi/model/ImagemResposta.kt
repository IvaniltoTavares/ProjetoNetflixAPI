package com.jamiltondamasceno.projetonetflixapi.model

data class ImagemResposta(
    val backdrops: List<Backdrop>,
    val id: Int,
    val posters: List<Poster>
)