package org.example.service.falha;

import org.example.Repositorio.EquipamentoRepositorio;
import org.example.Repositorio.FalhaRepository;
import org.example.model.Falha;

import java.sql.SQLException;
import java.util.List;

public class FalhaServiceImpl implements FalhaService{

    FalhaRepository repository = new FalhaRepository();
    EquipamentoRepositorio repositoryEquip = new EquipamentoRepositorio();

    @Override
    public Falha registrarNovaFalha(Falha falha) throws SQLException {

        falha.setStatus("ABERTA");
        falha = repository.cadastrarFalha(falha);

        if(repositoryEquip.buscarEquipamentoPorID(falha.getEquipamentoId()) == null){
            throw new IllegalArgumentException("Equipamento não encontrado!");
        }

        repositoryEquip.AtualizarStatusEquipamento("EM_MANUTENCAO", falha.getEquipamentoId());


        return falha;
    }

    @Override
    public List<Falha> buscarFalhasCriticasAbertas() throws SQLException {

        List<Falha> lista = repository.buscarFalhasCriticasAbertas();

        return lista;
    }
}
