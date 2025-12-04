package org.example.service.acaocorretiva;

import org.example.Repositorio.AcaoCorretivaRepository;
import org.example.Repositorio.EquipamentoRepositorio;
import org.example.Repositorio.FalhaRepository;
import org.example.model.AcaoCorretiva;
import org.example.model.Falha;

import java.sql.SQLException;

public class AcaoCorretivaServiceImpl implements AcaoCorretivaService{

    AcaoCorretivaRepository repository = new AcaoCorretivaRepository();
    FalhaRepository falhaRepository = new FalhaRepository();
    EquipamentoRepositorio equipRepository = new EquipamentoRepositorio();

    @Override
    public AcaoCorretiva registrarConclusaoDeAcao(AcaoCorretiva acao) throws SQLException {

        Falha falha = falhaRepository.buscarFalhaPorId(acao.getFalhaId());

        if(falha == null){
            throw new RuntimeException("Falha não encontrada!");
        }

        acao = repository.registrarAcaoCorretiva(acao);
        falhaRepository.atualizarFalha("RESOLVIDA", acao.getFalhaId());

        equipRepository.AtualizarStatusEquipamento("OPERACIONAL", falha.getEquipamentoId());

        return acao;
    }
}
