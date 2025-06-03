package service;

import model.Notificacao;
import model.Entidade;
import model.Cota;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.NotificacaoRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificacaoServiceTest {

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @InjectMocks
    private NotificacaoService notificacaoService;

    private Notificacao notificacao;
    private Entidade entidade;
    private Cota cota;

    @BeforeEach
    void setUp() {
        entidade = new Entidade();
        entidade.setId(1);
        entidade.setNome("Entidade Teste");

        cota = new Cota();
        cota.setId(1);
        cota.setNome("Cota Teste");
        cota.setEntidade(entidade);
        cota.setDataVencimento(LocalDateTime.now().plusDays(30));

        notificacao = new Notificacao();
        notificacao.setId(1);
        notificacao.setEntidade(entidade);
        notificacao.setTitulo("Teste");
        notificacao.setMensagem("Mensagem de teste");
        notificacao.setTipo(Notificacao.Tipo.informativo);
        notificacao.setLida(false);
        notificacao.setDataEnvio(LocalDateTime.now());
    }

    @Test
    void listarTodos_DeveRetornarListaDeNotificacoes() {
        List<Notificacao> notificacoes = Arrays.asList(notificacao);
        when(notificacaoRepository.findAll()).thenReturn(notificacoes);

        List<Notificacao> resultado = notificacaoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(notificacaoRepository).findAll();
    }

    @Test
    void listarPorEntidade_DeveRetornarNotificacoesDaEntidade() {
        List<Notificacao> notificacoes = Arrays.asList(notificacao);
        when(notificacaoRepository.findByEntidadeId(entidade.getId())).thenReturn(notificacoes);

        List<Notificacao> resultado = notificacaoService.listarPorEntidade(entidade.getId());

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(notificacaoRepository).findByEntidadeId(entidade.getId());
    }

    @Test
    void listarNaoLidas_DeveRetornarNotificacoesNaoLidas() {
        List<Notificacao> notificacoes = Arrays.asList(notificacao);
        when(notificacaoRepository.findByLidaFalse()).thenReturn(notificacoes);

        List<Notificacao> resultado = notificacaoService.listarNaoLidas();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(notificacaoRepository).findByLidaFalse();
    }

    @Test
    void listarNaoLidasPorEntidade_DeveRetornarNotificacoesNaoLidasDaEntidade() {
        List<Notificacao> notificacoes = Arrays.asList(notificacao);
        when(notificacaoRepository.findByEntidadeIdAndLidaFalse(entidade.getId())).thenReturn(notificacoes);

        List<Notificacao> resultado = notificacaoService.listarNaoLidasPorEntidade(entidade.getId());

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(notificacaoRepository).findByEntidadeIdAndLidaFalse(entidade.getId());
    }

    @Test
    void buscarPorId_DeveRetornarNotificacao_QuandoIdExiste() {
        when(notificacaoRepository.findById(notificacao.getId())).thenReturn(Optional.of(notificacao));

        Notificacao resultado = notificacaoService.buscarPorId(notificacao.getId());

        assertNotNull(resultado);
        assertEquals(notificacao.getId(), resultado.getId());
        verify(notificacaoRepository).findById(notificacao.getId());
    }

    @Test
    void buscarPorId_DeveLancarExcecao_QuandoIdNaoExiste() {
        when(notificacaoRepository.findById(any(Integer.class))).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
            notificacaoService.buscarPorId(999)
        );
    }

    @Test
    void salvar_DeveSalvarNotificacao() {
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(notificacao);

        Notificacao notificacaoSalva = notificacaoService.salvar(notificacao);

        assertNotNull(notificacaoSalva);
        assertEquals(notificacao.getTitulo(), notificacaoSalva.getTitulo());
        verify(notificacaoRepository).save(notificacao);
    }

    @Test
    void marcarComoLida_DeveMarcarNotificacaoComoLida() {
        when(notificacaoRepository.findById(notificacao.getId())).thenReturn(Optional.of(notificacao));
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(notificacao);

        notificacaoService.marcarComoLida(notificacao.getId());

        assertTrue(notificacao.getLida());
        verify(notificacaoRepository).save(notificacao);
    }

    @Test
    void marcarTodasComoLidas_DeveMarcarTodasNotificacoesComoLidas() {
        List<Notificacao> notificacoes = Arrays.asList(notificacao);
        when(notificacaoRepository.findByEntidadeIdAndLidaFalse(entidade.getId())).thenReturn(notificacoes);
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(notificacao);

        notificacaoService.marcarTodasComoLidas(entidade.getId());

        assertTrue(notificacao.getLida());
        verify(notificacaoRepository).save(notificacao);
    }

    @Test
    void deletar_DeveDeletarNotificacao_QuandoIdExiste() {
        doNothing().when(notificacaoRepository).deleteById(notificacao.getId());

        notificacaoService.deletar(notificacao.getId());

        verify(notificacaoRepository).deleteById(notificacao.getId());
    }

    @Test
    void notificarCotaVencida_DeveCriarNotificacaoDeAlerta() {
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(notificacao);

        notificacaoService.notificarCotaVencida(cota);

        verify(notificacaoRepository).save(any(Notificacao.class));
    }

    @Test
    void notificarCotaProximaVencimento_DeveCriarNotificacaoInformativa() {
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(notificacao);

        notificacaoService.notificarCotaProximaVencimento(cota);

        verify(notificacaoRepository).save(any(Notificacao.class));
    }

    @Test
    void notificarPagamentoConfirmado_DeveCriarNotificacaoDeSistema() {
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(notificacao);

        notificacaoService.notificarPagamentoConfirmado(cota);

        verify(notificacaoRepository).save(any(Notificacao.class));
    }
} 