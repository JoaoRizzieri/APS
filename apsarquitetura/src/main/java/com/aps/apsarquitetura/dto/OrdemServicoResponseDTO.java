package com.aps.apsarquitetura.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class OrdemServicoResponseDTO {
    private String nome;
    private String status;
    private String descricao;
    private LocalDate dataCriacao;
    private String cliente;

}
