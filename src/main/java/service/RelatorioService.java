package service;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;
import lombok.RequiredArgsConstructor;
import model.Cota;
import model.Pagamento;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RelatorioService {
    private final CotaService cotaService;
    private final PagamentoService pagamentoService;

    public byte[] gerarRelatorioCotasPDF() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Título
            Paragraph title = new Paragraph("Relatório de Cotas")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18)
                .setBold();
            document.add(title);

            // Data de geração
            Paragraph date = new Paragraph("Gerado em: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(10);
            document.add(date);

            // Tabela
            Table table = new Table(UnitValue.createPercentArray(5)).useAllAvailableWidth();

            // Cabeçalho
            String[] headers = {"ID", "Nome", "Entidade", "Valor", "Status"};
            for (String header : headers) {
                table.addHeaderCell(new Cell().add(new Paragraph(header).setBold()));
            }

            // Dados
            List<Cota> cotas = cotaService.listarTodos();
            for (Cota cota : cotas) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(cota.getId()))));
                table.addCell(new Cell().add(new Paragraph(cota.getNome())));
                table.addCell(new Cell().add(new Paragraph(cota.getEntidade().getNome())));
                table.addCell(new Cell().add(new Paragraph(String.format("R$ %.2f", cota.getValor()))));
                table.addCell(new Cell().add(new Paragraph(cota.getStatus().toString())));
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório PDF", e);
        }

        return out.toByteArray();
    }

    public byte[] gerarRelatorioPagamentosPDF() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        
        try {
            PdfWriter writer = new PdfWriter(out);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Título
            Paragraph title = new Paragraph("Relatório de Pagamentos")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18)
                .setBold();
            document.add(title);

            // Data de geração
            Paragraph date = new Paragraph("Gerado em: " + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")))
                .setTextAlignment(TextAlignment.RIGHT)
                .setFontSize(10);
            document.add(date);

            // Tabela
            Table table = new Table(UnitValue.createPercentArray(6)).useAllAvailableWidth();

            // Cabeçalho
            String[] headers = {"ID", "Cota", "Data", "Valor", "Método", "Status"};
            for (String header : headers) {
                table.addHeaderCell(new Cell().add(new Paragraph(header).setBold()));
            }

            // Dados
            List<Pagamento> pagamentos = pagamentoService.listarTodos();
            for (Pagamento pagamento : pagamentos) {
                table.addCell(new Cell().add(new Paragraph(String.valueOf(pagamento.getId()))));
                table.addCell(new Cell().add(new Paragraph(pagamento.getCota().getNome())));
                table.addCell(new Cell().add(new Paragraph(pagamento.getDataPagamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")))));
                table.addCell(new Cell().add(new Paragraph(String.format("R$ %.2f", pagamento.getValorPago()))));
                table.addCell(new Cell().add(new Paragraph(pagamento.getMetodoPagamento().toString())));
                table.addCell(new Cell().add(new Paragraph(pagamento.getEstado().toString())));
            }

            document.add(table);
            document.close();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório PDF", e);
        }

        return out.toByteArray();
    }

    public byte[] gerarRelatorioCotasExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Cotas");

            // Estilo para cabeçalho
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Cabeçalho
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Nome", "Entidade", "Valor", "Status"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Dados
            List<Cota> cotas = cotaService.listarTodos();
            int rowNum = 1;
            for (Cota cota : cotas) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(cota.getId());
                row.createCell(1).setCellValue(cota.getNome());
                row.createCell(2).setCellValue(cota.getEntidade().getNome());
                row.createCell(3).setCellValue(cota.getValor());
                row.createCell(4).setCellValue(cota.getStatus().toString());
            }

            // Ajustar largura das colunas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório Excel", e);
        }
    }

    public byte[] gerarRelatorioPagamentosExcel() {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Pagamentos");

            // Estilo para cabeçalho
            CellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Cabeçalho
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Cota", "Data", "Valor", "Método", "Status"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Dados
            List<Pagamento> pagamentos = pagamentoService.listarTodos();
            int rowNum = 1;
            for (Pagamento pagamento : pagamentos) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(pagamento.getId());
                row.createCell(1).setCellValue(pagamento.getCota().getNome());
                row.createCell(2).setCellValue(pagamento.getDataPagamento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
                row.createCell(3).setCellValue(pagamento.getValorPago());
                row.createCell(4).setCellValue(pagamento.getMetodoPagamento().toString());
                row.createCell(5).setCellValue(pagamento.getEstado().toString());
            }

            // Ajustar largura das colunas
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório Excel", e);
        }
    }
}