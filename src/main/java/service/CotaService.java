package service;

import lombok.RequiredArgsConstructor;
import model.Cota;
import org.springframework.stereotype.Service;
import repository.CotaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CotaService {
    private final CotaRepository cotaRepository;

    public List<Cota> listarTodos() {
        return cotaRepository.findAll();
    }

    public Cota buscarPorId(Long id) {
        return cotaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cota não encontrada"));
    }

    public Cota salvar(Cota cota) {
        return cotaRepository.save(cota);
    }

    public void deletar(Long id) {
        cotaRepository.deleteById(id);
    }
}