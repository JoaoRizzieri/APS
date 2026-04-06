package com.aps.apsarquitetura.controller;

import com.aps.apsarquitetura.dto.OrdemServicoRequestDTO;
import com.aps.apsarquitetura.dto.OrdemServicoResponseDTO;
import com.aps.apsarquitetura.model.OrdemServicoModel;
import com.aps.apsarquitetura.service.OrdemServicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordens")
public class OrdemServicoController {

    @Autowired
    private OrdemServicoService service;


    @PostMapping
    public ResponseEntity<OrdemServicoResponseDTO> criarOrdemServico(@RequestBody OrdemServicoRequestDTO ordem) {
        OrdemServicoResponseDTO criada = service.criarOrdemServico(ordem);
        return ResponseEntity.status(HttpStatus.CREATED).body(criada);
    }


    @GetMapping
    public ResponseEntity<List<OrdemServicoModel>> listarTodos() {
        List<OrdemServicoModel> ordens = service.litarTodos();
        return ResponseEntity.ok(ordens);
    }


    @GetMapping("/status/{status}")
    public ResponseEntity<List<OrdemServicoModel>> listarPorStatus(@PathVariable String status) {
        return ResponseEntity.ok(service.listarPorStatus(status));
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrdemServicoModel> listarPorId(@PathVariable Long id) {
        OrdemServicoModel ordem = service.listarPorId(id);
        return ResponseEntity.ok(ordem);
    }


    @PutMapping("/{id}")
    public ResponseEntity<OrdemServicoModel> atualizar(
            @PathVariable Long id,
            @RequestBody  OrdemServicoModel ordem) {
        OrdemServicoModel atualizada = service.atualizar(id, ordem);
        return ResponseEntity.ok(atualizada);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }


//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> forcedeletar(@PathVariable Long id) {
//        service.forcedeletar(id);
//        return ResponseEntity.noContent().build();
//    }
}