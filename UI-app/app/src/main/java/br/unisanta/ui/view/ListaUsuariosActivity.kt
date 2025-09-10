package br.unisanta.ui.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.unisanta.ui.R
import br.unisanta.ui.adapter.UsuarioAdapter
import br.unisanta.ui.model.UsuarioDaoImpl

class ListaUsuariosActivity : AppCompatActivity() {

    private val usuarioDao = UsuarioDaoImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_usuarios)

        val recyclerViewUsuarios = findViewById<RecyclerView>(R.id.rv_usuarios)

        recyclerViewUsuarios.layoutManager = LinearLayoutManager(this)

        val usuarios = usuarioDao.obterUsuarios()

        val adapter = UsuarioAdapter(usuarios)
        recyclerViewUsuarios.adapter = adapter
    }
}