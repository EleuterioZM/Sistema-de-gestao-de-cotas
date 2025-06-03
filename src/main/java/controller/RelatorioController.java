package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import service.RelatorioService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
@RequestMapping("/relatorios")
public class RelatorioController {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/cotas")
    public ResponseEntity<ByteArrayResource> gerarRelatorioCotas() {
        byte[] relatorio = relatorioService.gerarRelatorioCotas();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_cotas.pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(relatorio));
    }

    @GetMapping("/usuarios")
    public ResponseEntity<ByteArrayResource> gerarRelatorioUsuarios() {
        byte[] relatorio = relatorioService.gerarRelatorioUsuarios();
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_usuarios.pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(relatorio));
    }

    @GetMapping("/pagamentos")
    public ResponseEntity<ByteArrayResource> gerarRelatorioPagamentos(
            @RequestParam("dataInicio") String dataInicio,
            @RequestParam("dataFim") String dataFim) {
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime inicio = LocalDateTime.parse(dataInicio, formatter);
        LocalDateTime fim = LocalDateTime.parse(dataFim, formatter);
        
        byte[] relatorio = relatorioService.gerarRelatorioPagamentos(inicio, fim);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=relatorio_pagamentos.pdf");
        
        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(relatorio));
    }
}