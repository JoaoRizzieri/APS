package com.aps.apsarquitetura.service;

import com.aps.apsarquitetura.dto.OrdemServicoRequestDTO;
import com.aps.apsarquitetura.dto.OrdemServicoResponseDTO;
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

    public OrdemServicoResponseDTO criarOrdemServico(OrdemServicoRequestDTO dto) {

        // validações primeiro
        if (dto.getDescricao() == null || dto.getDescricao().isBlank()) {
            throw new RuntimeException("descrição não pode ser vazia");
        }

        if (dto.getCliente() == null || dto.getCliente().isBlank()) {
            throw new RuntimeException("nome do cliente não pode ser vazio");
        }

        // verifica duplicidade
        boolean jaExiste = repository.existsByNomeIgnoreCaseAndDescricaoIgnoreCaseAndClienteIgnoreCase(
                dto.getNome(),
                dto.getDescricao(),
                dto.getCliente()
        );

        if (jaExiste) {
            throw new RuntimeException("ordem já existente");
        }

        // 1. converte DTO → Model
        OrdemServicoModel model = new OrdemServicoModel();
        model.setNome(dto.getNome());
        model.setDescricao(dto.getDescricao());
        model.setCliente(dto.getCliente());

        // sistema define esses valores, não o usuário
        model.setStatus("ABERTA");
        model.setDataCriacao(LocalDate.now());

        // 2. salva o Model no banco
        OrdemServicoModel salvo = repository.save(model);

        // 3. converte Model → ResponseDTO
        OrdemServicoResponseDTO response = new OrdemServicoResponseDTO();
        response.setNome(salvo.getNome());
        response.setDescricao(salvo.getDescricao());
        response.setCliente(salvo.getCliente());
        response.setStatus(salvo.getStatus());
        response.setDataCriacao(salvo.getDataCriacao());

        return response;
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
