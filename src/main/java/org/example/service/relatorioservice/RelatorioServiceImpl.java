package org.example.service.relatorioservice;

import org.example.Repositorio.RelatorioRepository;
import org.example.dto.EquipamentoContagemFalhasDTO;
import org.example.dto.FalhaDetalhadaDTO;
import org.example.dto.RelatorioParadaDTO;
import org.example.model.Equipamento;
import org.example.Repositorio.EquipamentoRepositorio;
import org.example.Repositorio.RelatorioRepository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RelatorioServiceImpl implements RelatorioService {

    @Override
    public List<RelatorioParadaDTO> gerarRelatorioTempoParada() throws SQLException {
        var repo = new RelatorioRepository();

        List<RelatorioParadaDTO> lista = new ArrayList<>();
        lista = repo.gerarRelatorioTempoParada();

        lista.forEach(relatorioParadaDTO -> {
            System.out.println("ID equipamento: " + relatorioParadaDTO.getEquipamentoId());
            System.out.println("Nome: "+relatorioParadaDTO.getNomeEquipamento());
            System.out.println("Tempo: "+relatorioParadaDTO.getTotalHorasParadas());
        });
        return lista;
    }
    @Override
    public List<Equipamento> buscarEquipamentosSemFalhasPorPeriodo(LocalDate dataInicio, LocalDate datafim) throws SQLException {
        var eqpRepo = new EquipamentoRepositorio();
        RelatorioRepository relatorioRepo = new RelatorioRepository();

        List<Equipamento> listaTodosEq = eqpRepo.buscarEquipamentos();
        List<Equipamento> listaEqFalha = relatorioRepo.buscarEquipamentosSemFalhasPorPeriodo(dataInicio, datafim);

        List<Equipamento> listaSemFalha = new ArrayList<>();
        for (Equipamento item : listaTodosEq) {
            if (!listaEqFalha.contains(item)) {
                listaSemFalha.add(item);
            }
        }

        return listaSemFalha;
    }
    @Override
    public Optional<FalhaDetalhadaDTO> buscarDetalhesCompletosFalha(long falhaId) throws SQLException{

        return null;
    }
    @Override
    public List<EquipamentoContagemFalhasDTO> gerarRelatorioManutencaoPreventiva(int contagemMinimaFalhas) throws SQLException{

        return null;
    }
}