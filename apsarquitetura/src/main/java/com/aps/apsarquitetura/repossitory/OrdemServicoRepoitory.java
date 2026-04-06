package com.aps.apsarquitetura.repossitory;

import com.aps.apsarquitetura.model.OrdemServicoModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface OrdemServicoRepoitory extends JpaRepository<OrdemServicoModel, Long> {

    boolean existsByNomeIgnoreCaseAndDescricaoIgnoreCaseAndClienteIgnoreCase(String nome, String descricao, String cliente);

    List<OrdemServicoModel> findByStatusIgnoreCase(String status);

}
