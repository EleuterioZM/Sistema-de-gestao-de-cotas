package controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import service.RelatorioService;

@Controller
@RequestMapping("/relatorios")
@RequiredArgsConstructor
public class RelatorioController {
    private final RelatorioService relatorioService;

    @GetMapping
    public String lista() {
        return "relatorio/lista";
    }

    @GetMapping("/cotas/pdf")
    public ResponseEntity<byte[]> downloadCotasPDF() {
        byte[] relatorio = relatorioService.gerarRelatorioCotasPDF();
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-cotas.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(relatorio);
    }

    @GetMapping("/cotas/excel")
    public ResponseEntity<byte[]> downloadCotasExcel() {
        byte[] relatorio = relatorioService.gerarRelatorioCotasExcel();
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-cotas.xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(relatorio);
    }

    @GetMapping("/pagamentos/pdf")
    public ResponseEntity<byte[]> downloadPagamentosPDF() {
        byte[] relatorio = relatorioService.gerarRelatorioPagamentosPDF();
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-pagamentos.pdf")
            .contentType(MediaType.APPLICATION_PDF)
            .body(relatorio);
    }

    @GetMapping("/pagamentos/excel")
    public ResponseEntity<byte[]> downloadPagamentosExcel() {
        byte[] relatorio = relatorioService.gerarRelatorioPagamentosExcel();
        
        return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio-pagamentos.xlsx")
            .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
            .body(relatorio);
    }
}