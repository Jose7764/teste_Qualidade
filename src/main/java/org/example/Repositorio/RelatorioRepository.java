package org.example.Repositorio;

import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;
import org.example.model.Equipamento;
import org.example.database.Conexao;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RelatorioRepository{

    public List<RelatorioParadaDTO> gerarRelatorioTempoParada() throws SQLException {
        String query = """
                SELECT e.id, e.nome, f.tempoParadaHoras
                FROM Equipamento e
                JOIN Falha f ON e.id = f.equipamentoId;
                """;

        List<RelatorioParadaDTO> lista = new ArrayList<>();
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong("id");
                String nome = rs.getString("nome");
                Double tempo = rs.getDouble("tempoParadaHoras");

                var relatorio = new RelatorioParadaDTO(id, nome, tempo);
                lista.add(relatorio);
            }
        }
        return lista;
    }

    public List<Equipamento> buscarEquipamentosSemFalhasPorPeriodo(LocalDate dataInicio, LocalDate datafim) throws SQLException {
        String query = """
                SELECT e.id, 
                       e.nome, 
                       e.numeroDeSerie, 
                       e.areaSetor, 
                       e.statusOperacional
                FROM Equipamento e
                JOIN Falha f ON f.equipamentoId = e.id
                JOIN AcaoCorretiva a ON a.falhaId = f.id
                WHERE a.dataHoraInicio = ?
                AND a.dataHoraFim = ?
                """;

        List<Equipamento> lista = new ArrayList<>();
        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setDate(1, Date.valueOf(dataInicio));
            stmt.setDate(2, Date.valueOf(datafim));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Long id = rs.getLong("id");
                String nome = rs.getString("nome");
                String nSerie = rs.getString("numeroDeSerie");
                String area = rs.getString("areaSetor");
                String status = rs.getString("statusOperacional");

                var eq = new Equipamento(id, nome, nSerie, area, status);
                lista.add(eq);
            }
        }
        return lista;
    }

    public Optional<FalhaDetalhadaDTO> buscarDetalhesCompletosFalha(long falhaId) throws SQLException {
        String query = """
                    SELECT 
                        f.id AS falha_id,
                        f.descricao AS falha_descricao,
                        f.dataHoraOcorrencia,
                        f.criticidade,
                        f.status,
                        f.tempoParadaHoras,
                
                        e.id AS equipamento_id,
                        e.nome AS equipamento_nome,
                        e.numeroDeSerie,
                        e.areaSetor,
                        e.statusOperacional,
                
                        a.id AS acao_id,
                        a.descricaoAcao,
                        a.responsavel,
                        a.dataHoraInicio,
                        a.dataHoraFim
                
                    FROM Falha f
                    INNER JOIN Equipamento e ON f.equipamentoId = e.id
                    LEFT JOIN AcaoCorretiva a ON a.falhaId = f.id
                    WHERE f.id = ?
                """;

        try (Connection conn = Conexao.conectar();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setLong(1, falhaId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    Long id = rs.getLong("falha_id");
                    String desc = rs.getString("falha_descricao");
                    LocalDateTime dataOcorrencia = rs.getTimestamp("dataHoraOcorrencia").toLocalDateTime();
                    String criticidade = rs.getString("criticidade");
                    String status = rs.getString("status");
                    BigDecimal tempoParada = rs.getBigDecimal("tempoParadaHoras");

                    Long equipamentoId = rs.getLong("equipamento_id");
                    String nomeEquipamento = rs.getString("equipamento_nome");
                    String numeroSerie = rs.getString("numeroDeSerie");
                    String areaSetor = rs.getString("areaSetor");
                    String statusOperacional = rs.getString("statusOperacional");

                    Long acaoId = rs.getLong("acao_id");
                    String descricaoAcao = rs.getString("descricaoAcao");
                    String responsavel = rs.getString("responsavel");
                    Timestamp inicio = rs.getTimestamp("dataHoraInicio");
                    Timestamp fim = rs.getTimestamp("dataHoraFim");

                }
            }
        }

        return Optional.empty();
    }
}