package com.jamiltondamasceno.projetonetflixapi.api

import com.jamiltondamasceno.projetonetflixapi.model.FilmeLancamento
import com.jamiltondamasceno.projetonetflixapi.model.FilmeResposta
import com.jamiltondamasceno.projetonetflixapi.model.ImagemResposta
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface FilmeAPI {


    @GET("movie/latest?api_key=${RetrofitService.API_KEY}&language=pt-BR")
    suspend fun recuperarFilmeLancamento():Response<FilmeLancamento>


    /*@GET("movie/popular?api_key=${RetrofitService.API_KEY}&language=pt-BR")*/
    @GET("movie/popular")
    suspend fun recuperarFilmesPopulares(
        @Query("page")pagina: Int = 1
    ):Response<FilmeResposta>

}