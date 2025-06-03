package service;

import model.Pagamento;
import model.Usuario;
import model.Cota;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import repository.PagamentoRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock
    private PagamentoRepository pagamentoRepository;

    @InjectMocks
    private PagamentoService pagamentoService;

    private Pagamento pagamento;
    private Usuario usuario;
    private Cota cota;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail("usuario@teste");

        cota = new Cota();
        cota.setId(1);
        cota.setNome("Cota Teste");

        pagamento = new Pagamento();
        pagamento.setId(1L);
        pagamento.setUsuario(usuario);
        pagamento.setCota(cota);
        pagamento.setDataPagamento(LocalDateTime.now());
        pagamento.setValor(150.0);
        pagamento.setStatus(Pagamento.Status.CONFIRMADO);
        pagamento.setObservacao("Pagamento confirmado");
    }

    @Test
    void listarTodos_DeveRetornarListaDePagamentos() {
        List<Pagamento> pagamentos = Arrays.asList(pagamento);
        when(pagamentoRepository.findAll()).thenReturn(pagamentos);

        List<Pagamento> resultado = pagamentoService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(pagamentoRepository).findAll();
    }

    @Test
    void buscarPorId_DeveRetornarPagamento_QuandoIdExiste() {
        when(pagamentoRepository.findById(pagamento.getId())).thenReturn(Optional.of(pagamento));

        Pagamento resultado = pagamentoService.buscarPorId(pagamento.getId());

        assertNotNull(resultado);
        assertEquals(999L, resultado.getId());
        verify(pagamentoRepository).findById(pagamento.getId());
    }

    @Test
    void buscarPorId_DeveLancarExcecao_QuandoIdNaoExiste() {
        when(pagamentoRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () ->
            pagamentoService.buscarPorId(999L)
        );
    }

    @Test
    void salvar_DeveSalvarPagamento() {
        when(pagamentoRepository.save(any(Pagamento.class))).thenReturn(pagamento);

        Pagamento pagamentoSalvo = pagamentoService.salvar(pagamento);

        assertNotNull(pagamentoSalvo);
        assertEquals(pagamento.getValor(), pagamentoSalvo.getValor());
        verify(pagamentoRepository).save(pagamento);
    }

    @Test
    void deletar_DeveDeletarPagamento_QuandoIdExiste() {
        doNothing().when(pagamentoRepository).deleteById(pagamento.getId());

        pagamentoService.deletar(pagamento.getId());

        verify(pagamentoRepository).deleteById(pagamento.getId());
    }
} 