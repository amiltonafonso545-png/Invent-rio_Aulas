package com.starterkit.springboot.manutencao;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/api/manutencoes")
public class ManutencaoController {

    private final ManutencaoRepository repo;

    public ManutencaoController(ManutencaoRepository repo) {
        this.repo = repo;
    }

    // CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Manutencao create(@RequestBody Manutencao m) {
        return repo.save(m);
    }

    // LIST
    @GetMapping
    public List<Manutencao> list() {
        return repo.findAll();
    }

    // GET BY ID
    @GetMapping("/{id}")
    public Manutencao get(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Manutencao nao encontrada"
                ));
    }

    // UPDATE
    @PutMapping("/{id}")
    public Manutencao update(@PathVariable Long id, @RequestBody Manutencao mUpdate) {

        Manutencao m = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Manutencao nao encontrada"
                ));

        // Atualiza apenas campos existentes
        if (mUpdate.getCategoria() != null)
            m.setCategoria(mUpdate.getCategoria());

        if (mUpdate.getModelo() != null)
            m.setModelo(mUpdate.getModelo());

        if (mUpdate.getMarca() != null)
            m.setMarca(mUpdate.getMarca());

        if (mUpdate.getLocal() != null)
            m.setLocal(mUpdate.getLocal());

        if (mUpdate.getDataCompra() != null)
            m.setDataCompra(mUpdate.getDataCompra());

        if (mUpdate.getNumeroSerie() != null)
            m.setNumeroSerie(mUpdate.getNumeroSerie());

        if (mUpdate.getGarantia() != null)
            m.setGarantia(mUpdate.getGarantia());

        if (mUpdate.getSeguro() != null)
            m.setSeguro(mUpdate.getSeguro());

        if (mUpdate.getImagemPath() != null)
            m.setImagemPath(mUpdate.getImagemPath());

        return repo.save(m);
    }

    // DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {

        if (!repo.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Manutencao nao encontrada"
            );
        }

        repo.deleteById(id);
    }
}