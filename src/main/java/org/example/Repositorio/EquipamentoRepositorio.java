package org.example.Repositorio;

import org.example.database.Conexao;
import org.example.model.Equipamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EquipamentoRepositorio {

    public Equipamento cadastrarEquipamento(Equipamento eq)throws SQLException {

        String query = """
                INSERT INTO Equipamento(
                            nome,
                            numeroDeSerie,
                            areaSetor,
                            statusOperacional)
                VALUE(?,?,?,?);
                """;

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){

            stmt.setString(1, eq.getNome());
            stmt.setString(2, eq.getNumeroDeSerie());
            stmt.setString(3, eq.getAreaSetor());
            stmt.setString(4, eq.getStatusOperacional());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();

            if(rs.next()){
                eq.setId(rs.getLong(1));
            }
        }

        return eq;
    };

    public Equipamento buscarEquipamentoPorID(long id)throws SQLException{

        String query = """
                SELECT id
                       ,nome
                       ,numeroDeSerie
                       ,areaSetor
                       ,statusOperacional
                FROM Equipamento WHERE id = ?
                """;

        try (Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setLong(1, id);

            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                return new Equipamento(
                        rs.getLong("id"),
                        rs.getString("nome"),
                        rs.getString("numeroDeSerie"),
                        rs.getString("areaSetor"),
                        rs.getString("statusOperacional")
                );
            }

        }
        return null;
    }

    public void AtualizarStatusEquipamento(String status, long idEquipamento)throws SQLException{

        String query = """
                UPDATE Equipamento set statusOperacional = ? WHERE id = ?
                """;

        try(Connection conn = Conexao.conectar();
        PreparedStatement stmt = conn.prepareStatement(query)){

            stmt.setString(1, status);
            stmt.setLong(2, idEquipamento);
            stmt.executeUpdate();

        }

    }

    public List<Equipamento> buscarEquipamentos() throws SQLException {

        List<Equipamento> equipamentos = new ArrayList<>();

        String query = "SELECT * From Equipamento ";

        try(Connection conn = Conexao.conectar();
            PreparedStatement stmt = conn.prepareStatement(query)){

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                equipamentos.add(
                        new Equipamento(
                                rs.getLong("id"),
                                rs.getString("nome"),
                                rs.getString("numeroDeSerie"),
                                rs.getString("areaSetor"),
                                rs.getString("statusOperacional")
                        )
                );
            }
        }
        return  equipamentos;
    }
}

