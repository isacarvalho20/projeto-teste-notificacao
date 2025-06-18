package br.com.example;

import java.util.List;

public class NotificacaoService {

    private UsuarioRepository usuarioRepository;
    private EmailService emailService;

    public NotificacaoService(UsuarioRepository usuarioRepository, EmailService emailService) {
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
    }

    public void notificarUsuarios() {
        List<Usuario> usuarios = usuarioRepository.buscarUsuarios();

        for (Usuario usuario : usuarios) {
            if (usuario.isAtivo()) {
                emailService.enviarEmail(usuario);
            }
        }
    }
}
