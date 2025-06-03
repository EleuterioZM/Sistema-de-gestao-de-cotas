package service;

import model.Cota;
import model.Entidade;
import model.Notificacao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.CotaRepository;
import repository.EntidadeRepository;
import repository.NotificacaoRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CotaServiceTest {

    @Mock
    private CotaRepository cotaRepository;

    @Mock
    private EntidadeRepository entidadeRepository;

    @Mock
    private NotificacaoRepository notificacaoRepository;

    @InjectMocks
    private CotaService cotaService;

    private Cota cota;
    private Entidade entidade;

    @BeforeEach
    void setUp() {
        entidade = new Entidade();
        entidade.setId(1);
        entidade.setNome("Entidade Teste");

        cota = new Cota();
        cota.setId(1);
        cota.setNome("Cota Teste");
        cota.setDescricao("Descrição da Cota Teste");
        cota.setValor(100.0);
        cota.setDataVencimento(LocalDateTime.now().plusDays(30));
        cota.setStatus(Cota.Status.pendente);
        cota.setEntidade(entidade);
    }

    @Test
    void listarTodos_DeveRetornarListaDeCotas() {
        List<Cota> cotas = Arrays.asList(cota);
        when(cotaRepository.findAll()).thenReturn(cotas);

        List<Cota> resultado = cotaService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(cotaRepository).findAll();
    }

    @Test
    void buscarPorId_DeveRetornarCota_QuandoIdExiste() {
        when(cotaRepository.findById(cota.getId())).thenReturn(Optional.of(cota));

        Cota resultado = cotaService.buscarPorId(cota.getId());

        assertNotNull(resultado);
        assertEquals(cota.getId(), resultado.getId());
        verify(cotaRepository).findById(cota.getId());
    }

    @Test
    void buscarPorId_DeveLancarExcecao_QuandoIdNaoExiste() {
        when(cotaRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            cotaService.buscarPorId(999)
        );
    }

    @Test
    void salvar_DeveSalvarCota_QuandoNovaCota() {
        Cota novaCota = new Cota();
        novaCota.setNome("Nova Cota");
        novaCota.setDescricao("Descrição da Nova Cota");
        novaCota.setValor(200.0);
        novaCota.setDataVencimento(LocalDateTime.now().plusDays(30));
        novaCota.setStatus(Cota.Status.pendente);
        novaCota.setEntidade(entidade);

        when(cotaRepository.save(any(Cota.class))).thenReturn(novaCota);
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(new Notificacao());

        Cota cotaSalva = cotaService.salvar(novaCota);

        assertNotNull(cotaSalva);
        assertEquals("Nova Cota", cotaSalva.getNome());
        verify(cotaRepository).save(any(Cota.class));
        verify(notificacaoRepository).save(any(Notificacao.class));
    }

    @Test
    void salvar_DeveCriarEntidade_QuandoEntidadeNaoExiste() {
        Entidade novaEntidade = new Entidade();
        novaEntidade.setNome("Nova Entidade");

        Cota novaCota = new Cota();
        novaCota.setNome("Nova Cota");
        novaCota.setEntidade(novaEntidade);

        when(entidadeRepository.save(any(Entidade.class))).thenReturn(novaEntidade);
        when(cotaRepository.save(any(Cota.class))).thenReturn(novaCota);
        when(notificacaoRepository.save(any(Notificacao.class))).thenReturn(new Notificacao());

        Cota cotaSalva = cotaService.salvar(novaCota);

        assertNotNull(cotaSalva);
        verify(entidadeRepository).save(any(Entidade.class));
        verify(cotaRepository).save(any(Cota.class));
        verify(notificacaoRepository).save(any(Notificacao.class));
    }

    @Test
    void deletar_DeveDeletarCota_QuandoIdExiste() {
        doNothing().when(cotaRepository).deleteById(cota.getId());

        cotaService.deletar(cota.getId());

        verify(cotaRepository).deleteById(cota.getId());
    }
} 