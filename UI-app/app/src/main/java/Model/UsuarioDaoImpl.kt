package Model

class UsuarioDaoImpl : UsuarioDao {
    companion object {
        private val usuarios = mutableListOf<Usuario>()
    }
    override fun adicionarUsuario(usuario: Usuario): Boolean {
        return if (!emailJaExiste(usuario.email)) {
            usuarios.add(usuario)
            true
        } else {
            false
        }
    }
    override fun obterUsuarios(): List<Usuario> {
        return usuarios.toList()
    }
    override fun buscarUsuarioPorEmail(email: String): Usuario? {
        return usuarios.find { it.email == email }
    }
    override fun autenticarUsuario(email: String, senha: String): Boolean {
        val usuario = buscarUsuarioPorEmail(email)
        return usuario != null && usuario.senha == senha
    }
    override fun emailJaExiste(email: String): Boolean {
        return usuarios.any { it.email == email }
    }
}
