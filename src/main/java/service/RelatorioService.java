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
import model.Usuario;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repository.PagamentoRepository;
import repository.UsuarioRepository;
import repository.CotaRepository;

import java.io.ByteArrayOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RelatorioService {
    private final CotaService cotaService;
    private final PagamentoService pagamentoService;

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CotaRepository cotaRepository;

    public byte[] gerarRelatorioPagamentos(LocalDateTime dataInicio, LocalDateTime dataFim) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Título
            Paragraph titulo = new Paragraph("Relatório de Pagamentos")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold();
            document.add(titulo);

            // Período
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            Paragraph periodo = new Paragraph(
                    String.format("Período: %s a %s",
                            dataInicio.format(formatter),
                            dataFim.format(formatter)))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(12);
            document.add(periodo);

            // Tabela de pagamentos
            Table table = new Table(UnitValue.createPercentArray(new float[]{10, 20, 20, 15, 15, 20}))
                    .useAllAvailableWidth();

            // Cabeçalho
            table.addHeaderCell(new Cell().add(new Paragraph("ID")));
            table.addHeaderCell(new Cell().add(new Paragraph("Usuário")));
            table.addHeaderCell(new Cell().add(new Paragraph("Cota")));
            table.addHeaderCell(new Cell().add(new Paragraph("Data")));
            table.addHeaderCell(new Cell().add(new Paragraph("Valor")));
            table.addHeaderCell(new Cell().add(new Paragraph("Status")));

            // Dados
            List<Pagamento> pagamentos = pagamentoRepository.findByDataPagamentoBetween(dataInicio, dataFim);
            for (Pagamento pagamento : pagamentos) {
                table.addCell(new Cell().add(new Paragraph(pagamento.getId().toString())));
                table.addCell(new Cell().add(new Paragraph(pagamento.getUsuario().getNome())));
                table.addCell(new Cell().add(new Paragraph(pagamento.getCota().getNome())));
                table.addCell(new Cell().add(new Paragraph(pagamento.getDataPagamento().format(formatter))));
                table.addCell(new Cell().add(new Paragraph(String.format("R$ %.2f", pagamento.getValor()))));
                table.addCell(new Cell().add(new Paragraph(pagamento.getStatus().toString())));
            }

            document.add(table);

            // Totais
            double totalPago = pagamentos.stream()
                    .filter(p -> p.getStatus() == Pagamento.Status.CONFIRMADO)
                    .mapToDouble(Pagamento::getValor)
                    .sum();

            double totalPendente = pagamentos.stream()
                    .filter(p -> p.getStatus() == Pagamento.Status.PENDENTE)
                    .mapToDouble(Pagamento::getValor)
                    .sum();

            Paragraph totais = new Paragraph()
                    .add(new Paragraph(String.format("Total Pago: R$ %.2f", totalPago)))
                    .add(new Paragraph(String.format("Total Pendente: R$ %.2f", totalPendente)))
                    .setTextAlignment(TextAlignment.RIGHT)
                    .setFontSize(12);
            document.add(totais);

            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
    }

    public byte[] gerarRelatorioUsuarios() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Título
            Paragraph titulo = new Paragraph("Relatório de Usuários")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold();
            document.add(titulo);

            // Tabela de usuários
            Table table = new Table(UnitValue.createPercentArray(new float[]{20, 20, 20, 20, 20}))
                    .useAllAvailableWidth();

            // Cabeçalho
            table.addHeaderCell(new Cell().add(new Paragraph("Nome")));
            table.addHeaderCell(new Cell().add(new Paragraph("Email")));
            table.addHeaderCell(new Cell().add(new Paragraph("Total Pago")));
            table.addHeaderCell(new Cell().add(new Paragraph("Total Pendente")));
            table.addHeaderCell(new Cell().add(new Paragraph("Status")));

            // Dados
            List<Usuario> usuarios = usuarioRepository.findAll();
            for (Usuario usuario : usuarios) {
                List<Pagamento> pagamentos = pagamentoRepository.findByUsuario(usuario);
                
                double totalPago = pagamentos.stream()
                        .filter(p -> p.getStatus() == Pagamento.Status.CONFIRMADO)
                        .mapToDouble(Pagamento::getValor)
                        .sum();

                double totalPendente = pagamentos.stream()
                        .filter(p -> p.getStatus() == Pagamento.Status.PENDENTE)
                        .mapToDouble(Pagamento::getValor)
                        .sum();

                String status = totalPendente > 0 ? "Pendente" : "Em dia";

                table.addCell(new Cell().add(new Paragraph(usuario.getNome())));
                table.addCell(new Cell().add(new Paragraph(usuario.getEmail())));
                table.addCell(new Cell().add(new Paragraph(String.format("R$ %.2f", totalPago))));
                table.addCell(new Cell().add(new Paragraph(String.format("R$ %.2f", totalPendente))));
                table.addCell(new Cell().add(new Paragraph(status)));
            }

            document.add(table);
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
    }

    public byte[] gerarRelatorioCotas() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            // Título
            Paragraph titulo = new Paragraph("Relatório de Cotas")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(20)
                    .setBold();
            document.add(titulo);

            // Tabela de cotas
            Table table = new Table(UnitValue.createPercentArray(new float[]{20, 20, 20, 20, 20}))
                    .useAllAvailableWidth();

            // Cabeçalho
            table.addHeaderCell(new Cell().add(new Paragraph("Nome")));
            table.addHeaderCell(new Cell().add(new Paragraph("Valor")));
            table.addHeaderCell(new Cell().add(new Paragraph("Total Pago")));
            table.addHeaderCell(new Cell().add(new Paragraph("Total Pendente")));
            table.addHeaderCell(new Cell().add(new Paragraph("Status")));

            // Dados
            List<Cota> cotas = cotaRepository.findAll();
            for (Cota cota : cotas) {
                List<Pagamento> pagamentos = pagamentoRepository.findByCota(cota);
                
                double totalPago = pagamentos.stream()
                        .filter(p -> p.getStatus() == Pagamento.Status.CONFIRMADO)
                        .mapToDouble(Pagamento::getValor)
                        .sum();

                double totalPendente = pagamentos.stream()
                        .filter(p -> p.getStatus() == Pagamento.Status.PENDENTE)
                        .mapToDouble(Pagamento::getValor)
                        .sum();

                String status = totalPendente > 0 ? "Pendente" : "Em dia";

                table.addCell(new Cell().add(new Paragraph(cota.getNome())));
                table.addCell(new Cell().add(new Paragraph(String.format("R$ %.2f", cota.getValor()))));
                table.addCell(new Cell().add(new Paragraph(String.format("R$ %.2f", totalPago))));
                table.addCell(new Cell().add(new Paragraph(String.format("R$ %.2f", totalPendente))));
                table.addCell(new Cell().add(new Paragraph(status)));
            }

            document.add(table);
            document.close();
            return baos.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar relatório", e);
        }
    }
}