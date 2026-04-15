package com.starterkit.springboot.manutencao;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "manutencoes")
public class Manutencao {

    @Id
    @GeneratedValue(generator = "manutencao-id-gen")
    @GenericGenerator(name = "manutencao-id-gen", strategy = "increment")
    private Long id;

    @NotBlank
    @Size(max = 25)
    @Column(nullable = false, length = 25)
    private String categoria;

    @Column(name = "data_compra")
    private LocalDate dataCompra;

    @NotBlank
    @Size(max = 25)
    @Column(nullable = false, length = 25)
    private String modelo;

    @NotBlank
    @Size(max = 25)
    @Column(nullable = false, length = 25)
    private String marca;

    @Column(name = "numero_serie")
    private Long numeroSerie;

    @NotBlank
    @Size(max = 25)
    @Column(nullable = false, length = 25)
    private String local;

    @Column(nullable = false)
    private Boolean garantia = true;

    @Column(nullable = false)
    private Boolean seguro = false;

    @Column(name = "codigo_unico", unique = true, length = 36, updatable = false)
    private String codigoUnico;

    @Column(name = "imagem_path", length = 255)
    private String imagemPath;

    // Gera automaticamente antes de salvar
    @PrePersist
    public void ensureCodigoUnico() {
        if (codigoUnico == null || codigoUnico.trim().isEmpty()) {
            codigoUnico = UUID.randomUUID().toString();
        }
    }

    // ===== GETTERS E SETTERS =====

    public Long getId() {
        return id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDate getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDate dataCompra) {
        this.dataCompra = dataCompra;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public Long getNumeroSerie() {
        return numeroSerie;
    }

    public void setNumeroSerie(Long numeroSerie) {
        this.numeroSerie = numeroSerie;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public Boolean getGarantia() {
        return garantia;
    }

    public void setGarantia(Boolean garantia) {
        this.garantia = garantia;
    }

    public Boolean getSeguro() {
        return seguro;
    }

    public void setSeguro(Boolean seguro) {
        this.seguro = seguro;
    }

    public String getCodigoUnico() {
        return codigoUnico;
    }

    public String getImagemPath() {
        return imagemPath;
    }

    public void setImagemPath(String imagemPath) {
        this.imagemPath = imagemPath;
    }

    public void setCodigoUnico(String string) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setCodigoUnico'");
    }
}