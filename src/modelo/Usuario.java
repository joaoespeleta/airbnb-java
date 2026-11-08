package modelo;

/** Classe base dos usuários da plataforma. */
public abstract class Usuario implements Autenticavel {
    private String nome;
    private String email;
    private String senha;

    public Usuario(String nome, String email, String senha) {
        setNome(nome);
        setEmail(email);
        setSenha(senha);
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public String getSenha() {
        return senha;
    }

    public void setNome(String nome) {
        if (nome == null || nome.trim().length() < 3) {
            throw new IllegalArgumentException("O nome deve ter pelo menos 3 caracteres.");
        }
        this.nome = nome.trim();
    }// nome com pelo menos 3 caracteres; e-mail contendo "@" e "." e não repetido na
     // plataforma; senha com pelo menos 6 caracteres.

    public void setEmail(String email) {
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("E-mail inválido.");
        }
        this.email = email.trim();
    }

    public void setSenha(String senha) {
        if (senha == null || senha.length() < 6) {
            throw new IllegalArgumentException("A senha deve ter pelo menos 6 caracteres.");
        }
        this.senha = senha;
    }

    @Override
    public boolean autenticar(String email, String senha) {
        return this.email.equalsIgnoreCase(email) && this.senha.equals(senha);
    }

    public abstract void imprimirDados();

    @Override
    public String toString() {
        return "Nome: " + nome + " | E-mail: " + email;
    }
}
