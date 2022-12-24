package com.jamiltondamasceno.projetonetflixapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.jamiltondamasceno.projetonetflixapi.api.RetrofitService
import com.jamiltondamasceno.projetonetflixapi.databinding.ActivityDetalhesBinding
import com.jamiltondamasceno.projetonetflixapi.model.Filme
import com.squareup.picasso.Picasso

class DetalhesActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityDetalhesBinding.inflate( layoutInflater )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )

        var filme: Filme? = null
        val bundle = intent.extras
        if( bundle != null ){
            filme = bundle.getSerializable("filme") as Filme
            if( filme != null ){

                val nomeImagem = filme.poster_path
                val tamanhoImagem = "w780"
                val url_base = RetrofitService.BASE_URL_IMAGE
                val url = url_base + tamanhoImagem + nomeImagem

                Picasso.get()
                    .load( url )
                    //.placeholder()
                    .into( binding.imgPoster )

                binding.textFilmeTitulo.text = filme.title

                binding.textDetalhes.text = filme.overview

            }
        }

    }
}