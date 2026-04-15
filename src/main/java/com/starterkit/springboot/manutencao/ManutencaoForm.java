package com.starterkit.springboot.manutencao;

import org.springframework.web.multipart.MultipartFile;

public class ManutencaoForm extends ManutencaoRequest {

    private MultipartFile imagem;
    private String adminApiKey;

    public MultipartFile getImagem() {
        return imagem;
    }

    public void setImagem(MultipartFile imagem) {
        this.imagem = imagem;
    }

    public String getAdminApiKey() {
        return adminApiKey;
    }

    public void setAdminApiKey(String adminApiKey) {
        this.adminApiKey = adminApiKey;
    }
}