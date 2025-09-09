package br.unisanta.ui

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import Model.Usuario
import Model.UsuarioDaoImpl

class MainActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextSenha: EditText
    private lateinit var buttonCadastrar: Button
    private lateinit var buttonLogin: Button
    private lateinit var textViewEsqueceuSenha: TextView

    private val usuarioDao: UsuarioDaoImpl = UsuarioDaoImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val mainLayout = findViewById<View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainLayout) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Inicialização das Views
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextSenha = findViewById(R.id.editTextSenha)
        buttonCadastrar = findViewById(R.id.buttonCadastrar)
        buttonLogin = findViewById(R.id.buttonLogin)
        textViewEsqueceuSenha = findViewById(R.id.textViewEsqueceuSenha)

        // Botões sempre visíveis
        buttonLogin.visibility = View.VISIBLE
        buttonCadastrar.visibility = View.VISIBLE

        // Listener botão Cadastrar
        buttonCadastrar.setOnClickListener { realizarCadastro() }

        // Listener botão Login
        buttonLogin.setOnClickListener { realizarLogin() }

        // Esqueceu senha
        textViewEsqueceuSenha.setOnClickListener { mostrarDialogEsqueceuSenha() }
    }

    private fun realizarCadastro() {
        val email = editTextEmail.text.toString().trim()
        val senha = editTextSenha.text.toString().trim()

        if (!validarCamposCadastro(email, senha)) return

        val usuario = Usuario("", email, senha) // Nome removido
        if (usuarioDao.adicionarUsuario(usuario)) {
            Toast.makeText(this, getString(R.string.register_success), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, getString(R.string.email_already_exists), Toast.LENGTH_SHORT).show()
        }
    }

    private fun realizarLogin() {
        val email = editTextEmail.text.toString().trim()
        val senha = editTextSenha.text.toString().trim()

        if (!validarCamposLogin(email, senha)) return

        if (usuarioDao.autenticarUsuario(email, senha)) {
            Toast.makeText(this, getString(R.string.login_success), Toast.LENGTH_SHORT).show()
            val intent = Intent(this, ListaUsuariosActivity::class.java)
            startActivity(intent)
        } else {
            val usuario = usuarioDao.buscarUsuarioPorEmail(email)
            if (usuario == null) {
                Toast.makeText(this, getString(R.string.user_not_found), Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, getString(R.string.wrong_password), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validarCamposLogin(email: String, senha: String): Boolean {
        if (email.isEmpty()) {
            editTextEmail.error = getString(R.string.email_required)
            editTextEmail.requestFocus()
            return false
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = getString(R.string.invalid_email)
            editTextEmail.requestFocus()
            return false
        }
        if (senha.isEmpty()) {
            editTextSenha.error = getString(R.string.password_required)
            editTextSenha.requestFocus()
            return false
        }
        return true
    }

    private fun validarCamposCadastro(email: String, senha: String): Boolean {
        return validarCamposLogin(email, senha)
    }

    private fun mostrarDialogEsqueceuSenha() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.forgot_password_title))
            .setMessage(getString(R.string.contact_admin))
            .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
            .create()
            .show()
    }
}
