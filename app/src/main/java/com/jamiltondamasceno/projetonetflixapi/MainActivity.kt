package com.jamiltondamasceno.projetonetflixapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jamiltondamasceno.projetonetflixapi.adapter.FilmeAdapter
import com.jamiltondamasceno.projetonetflixapi.api.RetrofitService
import com.jamiltondamasceno.projetonetflixapi.databinding.ActivityMainBinding
import com.jamiltondamasceno.projetonetflixapi.model.FilmeLancamento
import com.jamiltondamasceno.projetonetflixapi.model.FilmeResposta
import com.squareup.picasso.Picasso
import kotlinx.coroutines.*
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private var paginaAtual = 1
    private var job:Job? = null
    private val TAG = "info_filme"
    private val binding by lazy {
        ActivityMainBinding.inflate( layoutInflater )
    }

    private val filmeAPI by lazy {
        RetrofitService.filmeAPI
    }
    var filmeAdapter: FilmeAdapter? = null
    var linearLayoutManager:LinearLayoutManager? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( binding.root )

        filmeAdapter = FilmeAdapter{ filme ->
            val intent= Intent(this,DetalhesActivity::class.java)
            intent.putExtra("filme",filme)
            startActivity(intent)

        }

        binding.rvPopulares.adapter = filmeAdapter

        linearLayoutManager = GridLayoutManager(this,2
        )
        binding.rvPopulares.layoutManager = linearLayoutManager

        binding.rvPopulares.addOnScrollListener( object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                /*super.onScrolled(recyclerView, dx, dy)
                0 1 2...19(último item)

                val totalItens = recyclerView.adapter?.itemCount//20
                val ultimoItem = linearLayoutManager?.findLastVisibleItemPosition()
                //Log.i("onScrolled", "onScrolled: T: $totalItens U: $ultimoItem")
                if ( totalItens != null && ultimoItem != null ){
                    Log.i("onScrolled", "onScrolled: T: ${totalItens-1} U: $ultimoItem")
                    if( totalItens - 1 == ultimoItem ){
                        binding.fabAdicionar.hide()
                    }else{
                        binding.fabAdicionar.show()
                    }
                }

                Log.i("onScrolled", "onScrolled: dx: $dx dy: $dy")
                if( dy > 0 ){//Descendo até o último item (número sempre positivo)
                    binding.fabAdicionar.hide()
                }else{//Subindo até o primeiro item (número sempre net
                    binding.fabAdicionar.show()
                }*/

                //1 quando cehgar no final, pode
                val podeRolarVerticalmente = recyclerView.canScrollVertically(1)

                if (!podeRolarVerticalmente){
                    //carregar proxima pagina
                    recuperarFilmesProximaPagina()

//                    Log.i(TAG, "onScrolled: $podeRolarVerticalmente")
                }
            }
        })
    }

    /*private fun textTitulo(){
        var titulo = ""
        titulo = filmeLancamento?.body()?.title.toString()
    }*/
    private fun carregarDados() {

        CoroutineScope(Dispatchers.IO).launch {
            recuperarFilmesPopulares()
           /* recuperarPosterPrincipal()*/

        }
    }

    private suspend fun recuperarPosterPrincipal(){

    }

    private fun carregarImagemCapa() {

        CoroutineScope(Dispatchers.Main).launch {
            imagemCapa()
        }

    }

    private suspend fun imagemCapa() {
        var filmeLancamento: Response<FilmeLancamento>? = null

        try {
            filmeLancamento = filmeAPI.recuperarFilmeLancamento()
        }catch (e:Exception){
            e.printStackTrace()
            /*Log.i(TAG, "erro ao recuperar filme lancamento: ")*/
        }
        if (filmeLancamento?.body()?.poster_path!= null) {
            if (filmeLancamento.isSuccessful) {

                val id = filmeLancamento.body()?.id.toString()

                val imagem = filmeLancamento.body()?.poster_path
                val tamanhoImagem = "w780"
                val url_base = RetrofitService.BASE_URL_IMAGE

                val url = url_base + tamanhoImagem + imagem

                Picasso.get()
                    .load(url)
                    .into(binding.imgCapa)

                binding.textTituloCapa.text = filmeLancamento.body()?.title


            } else {

                Log.i(TAG, "Erro na requisição  -> codigo do erro: ${filmeLancamento.code()} ")
            }
        }
    }

    private fun recuperarFilmesProximaPagina(){
        if(paginaAtual < 1000){
            paginaAtual++
            recuperarFilmesPopulares(paginaAtual)

        }
    }

    private  fun recuperarFilmesPopulares(pagina:Int = 1){

        job = CoroutineScope(Dispatchers.IO).launch {

            var resposta : Response<FilmeResposta>? = null

            try {
                Log.i(TAG, "pagina atual : $paginaAtual: ")
                resposta = filmeAPI.recuperarFilmesPopulares(pagina)
            }catch (e:Exception){
                e.printStackTrace()
                Log.i(TAG, "Erro ao recuperar filmes populares: ")
            }

            if (resposta!= null){
                if (resposta.isSuccessful){

                    val listaFilmes = resposta.body()?.filmes
                    if (listaFilmes!=null){
                        withContext(Dispatchers.Main){
                            filmeAdapter?.adicionarLista(listaFilmes)
                        }
                    }
                    /*listaFilme?.forEach {filme ->
                        Log.i(TAG, "${filme.id} - ${filme.title}")
                    }*/
                }else{
                    Log.i(TAG, "Erro na requisição  -> codigo do erro: ${resposta.code()} ")
                }
            }

        }


    }

    override fun onStart() {
        super.onStart()
        recuperarFilmesPopulares(1)
        carregarImagemCapa()

    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }


}















