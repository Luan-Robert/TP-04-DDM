package br.unisanta.ui.view

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import br.unisanta.ui.R
import br.unisanta.ui.model.Usuario
import br.unisanta.ui.model.UsuarioDaoImpl

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val usuarioDao = UsuarioDaoImpl()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 1. Inicialização das views
        val editTextEmail = findViewById<EditText>(R.id.editTextEmail)
        val editTextSenha = findViewById<EditText>(R.id.editTextSenha)
        val buttonCadastrar = findViewById<Button>(R.id.buttonCadastrar)
        val buttonLogin = findViewById<Button>(R.id.buttonLogin)
        val textViewEsqueceuSenha = findViewById<TextView>(R.id.textViewEsqueceuSenha)

        // 2. Listener do botão Cadastrar
        buttonCadastrar.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val senha = editTextSenha.text.toString().trim()

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.error = getString(R.string.invalid_email)
                editTextEmail.requestFocus()
                return@setOnClickListener
            }
            if (senha.isEmpty()) {
                editTextSenha.error = getString(R.string.password_required)
                editTextSenha.requestFocus()
                return@setOnClickListener
            }

            val novoUsuario = Usuario("", email, senha)
            if (usuarioDao.adicionarUsuario(novoUsuario)) {
                // Alerta de Sucesso no Cadastro
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.register_success_title)) // Título de sucesso
                    .setMessage(getString(R.string.register_success))
                    .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                        editTextEmail.text.clear()
                        editTextSenha.text.clear()
                    }
                    .create()
                    .show()
            } else {
                // Alerta de Erro no Cadastro
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.error_title)) // Título de erro
                    .setMessage(getString(R.string.email_already_exists))
                    .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            }
        }

        // 3. Listener do botão Login
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val senha = editTextSenha.text.toString().trim()

            // Validação
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                editTextEmail.error = getString(R.string.invalid_email)
                editTextEmail.requestFocus()
                return@setOnClickListener
            }
            if (senha.isEmpty()) {
                editTextSenha.error = getString(R.string.password_required)
                editTextSenha.requestFocus()
                return@setOnClickListener
            }

            // Lógica de login
            if (usuarioDao.autenticarUsuario(email, senha)) {
                // Alerta de Sucesso no Login
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.login_success_title)) // Título de sucesso
                    .setMessage(getString(R.string.login_success))
                    .setPositiveButton(getString(R.string.ok)) { dialog, _ ->
                        dialog.dismiss()
                        val intent = Intent(this, ListaUsuariosActivity::class.java)
                        startActivity(intent)
                    }
                    .create()
                    .show()
            } else {
                // Alerta de Erro no Login
                AlertDialog.Builder(this)
                    .setTitle(getString(R.string.error_title)) // Título de erro
                    .setMessage(getString(R.string.login_failed))
                    .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
                    .create()
                    .show()
            }
        }

        // 4. Listener para "Esqueceu a senha?"
        textViewEsqueceuSenha.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.forgot_password_title))
                .setMessage(getString(R.string.contact_admin))
                .setPositiveButton(getString(R.string.ok)) { dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }
}