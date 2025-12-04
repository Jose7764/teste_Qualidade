package org.example.Repositorio;

import org.example.database.Conexao;
import org.example.model.Falha;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FalhaRepository {

    public Falha cadastrarFalha(Falha falha)throws SQLException{

        String query = """
                INSERT INTO Falha
                (equipamentoId, dataHoraOcorrencia, descricao, criticidade, status, tempoParadaHoras)
                VALUES (?,?,?,?,?,?)
                """;

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){

            stmt.setLong(1, falha.getEquipamentoId());
            LocalDateTime now = LocalDateTime.now();
            stmt.setTimestamp(2, Timestamp.valueOf(now));
            stmt.setString(3, falha.getDescricao());
            stmt.setString(4, falha.getCriticidade());
            stmt.setString(5, falha.getStatus());
            stmt.setBigDecimal(6, falha.getTempoParadaHoras());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()){
                falha.setId(rs.getLong(1));
            }
        }



        return falha;
    }

    public List<Falha> buscarFalhasCriticasAbertas()throws SQLException{

        String query = """
                SELECT equipamentoId, dataHoraOcorrencia, descricao, criticidade, status, tempoParadaHoras
                FROM Falha WHERE criticidade = 'CRITICA' and status = 'ABERTA'
                """ ;

        List<Falha> listaFalhas = new ArrayList<>();
        Falha falha;
        try (Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            ResultSet rs = stmt.executeQuery();

            while (rs.next()){
                listaFalhas.add(falha = new Falha(
                        rs.getLong("equipamentoId"),
                        rs.getTimestamp("dataHoraOcorrencia").toLocalDateTime(),
                        rs.getString("descricao"),
                        rs.getString("criticidade"),
                        rs.getString("status"),
                        rs.getBigDecimal("tempoParadaHoras")
                        ));
            }
        }
        return listaFalhas;
    }

    public void atualizarFalha(String status, long falhaID)throws SQLException{

        String query = """
                UPDATE Falha set status = ? where id = ?;
                """;

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, status);
            stmt.setLong(2, falhaID);
            stmt.executeUpdate();
        }
    }

    public Falha buscarFalhaPorId(Long id)throws SQLException{

        String query = """
                SELECT equipamentoId, dataHoraOcorrencia, descricao, criticidade, status, tempoParadaHoras
                FROM Falha where id = ?; 
                """;

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setLong(1,id);
            ResultSet rs = stmt.executeQuery();

            Falha falha;

            if(rs.next()){
                return falha = new Falha(
                        rs.getLong("equipamentoId"),
                        rs.getTimestamp("dataHoraOcorrencia").toLocalDateTime(),
                        rs.getString("descricao"),
                        rs.getString("criticidade"),
                        rs.getString("status"),
                        rs.getBigDecimal("tempoParadaHoras")
                );
            }
        }

        return null;
    }
}
