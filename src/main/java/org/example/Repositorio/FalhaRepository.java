package org.example.Repositorio;

import org.example.model.Falha;

import java.sql.SQLException;

public class FalhaRepository {

    public Falha cadastrarFalha()throws SQLException{

        String query = """
                INSERT INTO Falha
                (equipamentoId, dataHoraOcorrencia, descricao, criticidade, status, tempoParadaHoras)
                VALUES (?,?,?,?,?,?)
                """;


        return null;
    }
}
