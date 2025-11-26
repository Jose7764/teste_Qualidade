package org.example.service.equipamento;

import org.example.Repositorio.EquipamentoRepositorio;
import org.example.model.Equipamento;

import java.sql.SQLException;

public class EquipamentoServiceImpl implements EquipamentoService{

    EquipamentoRepositorio repository = new EquipamentoRepositorio();

    @Override
    public Equipamento criarEquipamento(Equipamento equipamento) throws SQLException {

        equipamento.setStatusOperacional("OPERACIONAL");
        equipamento = repository.cadastrarEquipamento(equipamento);

        if(equipamento.getId() == null){
            throw new RuntimeException("Ocorreu um Erro");
        }

        return equipamento;
    }

    @Override
    public Equipamento buscarEquipamentoPorId(Long id) throws SQLException {
        Equipamento equipamento = repository.buscarEquipamentoPorID(id);

        if(equipamento == null){
            throw new RuntimeException("Equipamento não encontrado!");
        }

        return equipamento;
    }
}
