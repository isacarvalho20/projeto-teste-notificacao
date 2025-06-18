package br.com.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

public class NotificacaoServiceTest {

    private UsuarioRepository usuarioRepository;
    private EmailService emailService;
    private NotificacaoService notificacaoService;

    @BeforeEach
    public void setup() {
        usuarioRepository = mock(UsuarioRepository.class);
        emailService = mock(EmailService.class);
        notificacaoService = new NotificacaoService(usuarioRepository, emailService);
    }

    @Test
    public void deveEnviarEmailApenasParaUsuariosAtivos() {
        Usuario ativo1 = new Usuario("João", "joao@email.com", true);
        Usuario inativo = new Usuario("Maria", "maria@email.com", false);
        Usuario ativo2 = new Usuario("Ana", "ana@email.com", true);

        when(usuarioRepository.buscarUsuarios()).thenReturn(Arrays.asList(ativo1, inativo, ativo2));

        notificacaoService.notificarUsuarios();

        verify(emailService).enviarEmail(ativo1);
        verify(emailService).enviarEmail(ativo2);
        verify(emailService, never()).enviarEmail(inativo);
    }

    @Test
    public void naoDeveEnviarEmailSeListaEstiverVazia() {
        when(usuarioRepository.buscarUsuarios()).thenReturn(Collections.emptyList());

        notificacaoService.notificarUsuarios();

        verify(emailService, never()).enviarEmail(any());
    }

    @Test
    public void deveLancarExcecaoAoEnviarEmail() {
        Usuario ativo = new Usuario("Lucas", "lucas@email.com", true);
        when(usuarioRepository.buscarUsuarios()).thenReturn(Collections.singletonList(ativo));
        doThrow(new RuntimeException("Falha no envio")).when(emailService).enviarEmail(ativo);

        Executable acao = () -> notificacaoService.notificarUsuarios();

        RuntimeException excecao = assertThrows(RuntimeException.class, acao);
        assertEquals("Falha no envio", excecao.getMessage());
    }

    @Test
    public void deveExecutarTesteSomenteSeHouverUsuarioAtivo() {
        Usuario ativo = new Usuario("Carol", "carol@email.com", true);
        List<Usuario> usuarios = Collections.singletonList(ativo);

        assumeTrue(usuarios.stream().anyMatch(Usuario::isAtivo));

        when(usuarioRepository.buscarUsuarios()).thenReturn(usuarios);

        notificacaoService.notificarUsuarios();

        verify(emailService).enviarEmail(ativo);
    }

    @Test
    public void deveEnviarEmailUmaUnicaVezParaCadaUsuarioAtivo() {
        Usuario ativo1 = new Usuario("Pedro", "pedro@email.com", true);
        Usuario ativo2 = new Usuario("Lia", "lia@email.com", true);

        when(usuarioRepository.buscarUsuarios()).thenReturn(Arrays.asList(ativo1, ativo2));

        notificacaoService.notificarUsuarios();

        verify(emailService, times(1)).enviarEmail(ativo1);
        verify(emailService, times(1)).enviarEmail(ativo2);
    }
}
