package service;

import model.Usuario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import repository.UsuarioRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuario = new Usuario();
        usuario.setId(1);
        usuario.setEmail("teste@teste.com");
        usuario.setSenha("senha123");
        usuario.setPerfil(Usuario.Perfil.visualizador);
        usuario.setAtivo(true);
    }

    @Test
    void loadUserByUsername_DeveRetornarUserDetails_QuandoUsuarioExiste() {
        when(usuarioRepository.findByEmail(usuario.getEmail())).thenReturn(Optional.of(usuario));

        UserDetails userDetails = usuarioService.loadUserByUsername(usuario.getEmail());

        assertNotNull(userDetails);
        assertEquals(usuario.getEmail(), userDetails.getUsername());
        verify(usuarioRepository).findByEmail(usuario.getEmail());
    }

    @Test
    void loadUserByUsername_DeveLancarExcecao_QuandoUsuarioNaoExiste() {
        when(usuarioRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> 
            usuarioService.loadUserByUsername("email@inexistente.com")
        );
    }

    @Test
    void salvar_DeveSalvarUsuario_QuandoNovoUsuario() {
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        Usuario usuarioNovo = new Usuario();
        usuarioNovo.setEmail("teste@teste.com");
        usuarioNovo.setSenha("senha123");
        usuarioNovo.setPerfil(Usuario.Perfil.visualizador);
        usuarioNovo.setAtivo(true);

        Usuario usuarioSalvo = usuarioService.salvar(usuarioNovo);

        assertNotNull(usuarioSalvo);
        assertTrue(usuarioSalvo.getAtivo());
        assertEquals(Usuario.Perfil.visualizador, usuarioSalvo.getPerfil());
        verify(usuarioRepository).save(any(Usuario.class));
        verify(passwordEncoder).encode("senha123");
    }

    @Test
    void salvar_DeveLancarExcecao_QuandoEmailJaExiste() {
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);

        Usuario usuarioNovo = new Usuario();
        usuarioNovo.setEmail("teste@teste.com");
        usuarioNovo.setSenha("senha123");
        usuarioNovo.setPerfil(Usuario.Perfil.visualizador);
        usuarioNovo.setAtivo(true);
        usuarioNovo.setId(null); // id null para simular novo usuário

        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            usuarioService.salvar(usuarioNovo)
        );
        
        assertEquals("Email já cadastrado", exception.getMessage());
    }

    @Test
    void listarTodos_DeveRetornarListaDeUsuarios() {
        List<Usuario> usuarios = Arrays.asList(usuario);
        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> resultado = usuarioService.listarTodos();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(usuarioRepository).findAll();
    }

    @Test
    void buscarPorId_DeveRetornarUsuario_QuandoIdExiste() {
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));

        Usuario resultado = usuarioService.buscarPorId(usuario.getId());

        assertNotNull(resultado);
        assertEquals(usuario.getId(), resultado.getId());
        verify(usuarioRepository).findById(usuario.getId());
    }

    @Test
    void buscarPorId_DeveLancarExcecao_QuandoIdNaoExiste() {
        when(usuarioRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> 
            usuarioService.buscarPorId(999)
        );
    }

    @Test
    void deletar_DeveDeletarUsuario_QuandoIdExiste() {
        when(usuarioRepository.existsById(usuario.getId())).thenReturn(true);
        doNothing().when(usuarioRepository).deleteById(usuario.getId());

        usuarioService.deletar(usuario.getId());

        verify(usuarioRepository).deleteById(usuario.getId());
    }

    @Test
    void deletar_DeveLancarExcecao_QuandoIdNaoExiste() {
        when(usuarioRepository.existsById(anyInt())).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> 
            usuarioService.deletar(999)
        );
    }

    @Test
    void existeEmail_DeveRetornarTrue_QuandoEmailExiste() {
        when(usuarioRepository.existsByEmail(usuario.getEmail())).thenReturn(true);

        boolean resultado = usuarioService.existeEmail(usuario.getEmail());

        assertTrue(resultado);
        verify(usuarioRepository).existsByEmail(usuario.getEmail());
    }
} 