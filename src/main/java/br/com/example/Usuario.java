package br.com.example;

public class Usuario {
    private String nome;
    private String email;
    private boolean ativo;

    public Usuario(String nome, String email, boolean ativo) {
        this.nome = nome;
        this.email = email;
        this.ativo = ativo;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public boolean isAtivo() {
        return ativo;
    }

    @Override
    public String toString() {
        return nome + " <" + email + "> (ativo=" + ativo + ")";
    }
}
