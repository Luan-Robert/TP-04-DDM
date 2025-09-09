package Model

interface UsuarioDao {
    fun adicionarUsuario(usuario: Usuario): Boolean
    fun obterUsuarios(): List<Usuario>
    fun buscarUsuarioPorEmail(email: String): Usuario?
    fun autenticarUsuario(email: String, senha: String): Boolean
    fun emailJaExiste(email: String): Boolean
}