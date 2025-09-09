package br.unisanta.ui

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import Model.UsuarioDaoImpl

class ListaUsuariosActivity : AppCompatActivity() {

    private lateinit var listViewUsuarios: ListView
    private val usuarioDao: UsuarioDaoImpl = UsuarioDaoImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_usuarios)

        listViewUsuarios = findViewById(R.id.listViewUsuarios)

        val usuarios = usuarioDao.obterUsuarios()
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, usuarios.map { it.email })
        listViewUsuarios.adapter = adapter
    }
}

