package br.com.example;

public class EmailService {
    public void enviarEmail(Usuario usuario) {
        System.out.println("Enviando e-mail para: " + usuario.getEmail());
    }
}
