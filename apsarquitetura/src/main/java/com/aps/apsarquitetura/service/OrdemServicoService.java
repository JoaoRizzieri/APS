package com.aps.apsarquitetura.service;

import com.aps.apsarquitetura.model.OrdemServicoModel;
import com.aps.apsarquitetura.repossitory.OrdemServicoRepoitory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrdemServicoService {
    @Autowired
    private OrdemServicoRepoitory repository;

    public OrdemServicoModel criarOrdemServico(OrdemServicoModel ordemServicoModel) {

        boolean jaExiste = repository.existsByNomeIgnoreCaseAndDescricaoIgnoreCaseAndClienteIgnoreCase(
                ordemServicoModel.getNome(),
                ordemServicoModel.getDescricao(),
                ordemServicoModel.getCliente());
        if (jaExiste) {
            throw new RuntimeException("ordem ja existente");
        }

        if (ordemServicoModel.getDescricao() == null) {
            throw new RuntimeException("descricao não pode ser vazia");
        }

        if (ordemServicoModel.getCliente() == null) {
            throw new RuntimeException("nome do cliente não pode ser vazio");
        }
        ordemServicoModel.setStatus("ABERTA");
        ordemServicoModel.setDataCriacao(LocalDate.now());

        return repository.save(ordemServicoModel);
    }

    public OrdemServicoModel atualizar(Long id, OrdemServicoModel ordemServicoModel) {
        OrdemServicoModel ordemExistente = repository.findById(id).orElseThrow(() -> new RuntimeException("Ordem de serviço não encontrada com id: " + id));

        if (ordemExistente.getStatus().equals("CONCLUIDA")) {
            throw new RuntimeException("Não é possível alterar uma OS concluída");
        }

        ordemExistente.setDescricao(ordemServicoModel.getDescricao());
        ordemExistente.setStatus(ordemServicoModel.getStatus());

        return repository.save(ordemExistente);
    }

    public OrdemServicoModel listarPorId(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Ordem de serviço não encontrada com id: " + id));
    }

    public List<OrdemServicoModel> litarTodos() {
        return repository.findAll();
    }

    public List<OrdemServicoModel> listarPorStatus(String status) {
        return repository.findByStatusIgnoreCase(status);
    }


    public void forcedeletar(Long id){
        repository.deleteById(id);
    }


    public void deletar(Long id) {
        OrdemServicoModel ordem = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ordem não encontrada"));

        if (ordem.getStatus().equals("EM_ANDAMENTO")) {
            throw new RuntimeException("Não é possível deletar uma OS em andamento");
        }

        repository.deleteById(id);
    }


}
