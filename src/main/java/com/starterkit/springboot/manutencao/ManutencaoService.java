package com.starterkit.springboot.manutencao;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ManutencaoService {

    private final ManutencaoRepository repo;
    private final Path uploadDir;

    public ManutencaoService(ManutencaoRepository repo,
                             @Value("${app.upload-dir:./uploads}") String uploadDir) {
        this.repo = repo;
        this.uploadDir = Paths.get(uploadDir).toAbsolutePath().normalize().resolve("manutencoes");
    }

    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(uploadDir);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao criar pasta de upload", e);
        }
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void preencherCodigoUnico() {
        List<Manutencao> lista = repo.findAll();

        for (Manutencao m : lista) {
            if (!StringUtils.hasText(m.getCodigoUnico())) {
                m.setCodigoUnico(UUID.randomUUID().toString());
            }
        }

        repo.saveAll(lista);
    }

    public List<Manutencao> listar() {
        return repo.findAll();
    }

    public Manutencao buscarPorId(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Manutencao nao encontrada"));
    }

    public Manutencao criar(ManutencaoRequest req) {
        Manutencao m = new Manutencao();
        mapear(m, req);
        return repo.save(m);
    }

    public Manutencao criarComImagem(ManutencaoForm form) {
        Manutencao m = new Manutencao();
        mapear(m, form);
        m.setImagemPath(salvarImagem(form.getImagem(), null));
        return repo.save(m);
    }

    public Manutencao atualizar(Long id, ManutencaoRequest req) {
        Manutencao m = buscarPorId(id);
        mapear(m, req);
        return repo.save(m);
    }

    public Manutencao atualizarComImagem(Long id, ManutencaoForm form) {
        Manutencao m = buscarPorId(id);
        mapear(m, form);
        m.setImagemPath(salvarImagem(form.getImagem(), m.getImagemPath()));
        return repo.save(m);
    }

    public void deletar(Long id) {
        Manutencao m = buscarPorId(id);
        deletarImagem(m.getImagemPath());
        repo.delete(m);
    }

    private void mapear(Manutencao m, ManutencaoRequest req) {
        m.setCategoria(req.getCategoria());
        m.setDataCompra(req.getDataCompra());
        m.setModelo(req.getModelo());
        m.setMarca(req.getMarca());
        m.setNumeroSerie(req.getNumeroSerie());
        m.setLocal(req.getLocal());
        m.setGarantia(Boolean.TRUE.equals(req.getGarantia()));
        m.setSeguro(Boolean.TRUE.equals(req.getSeguro()));
    }

    private String salvarImagem(MultipartFile file, String atual) {
        if (file == null || file.isEmpty()) return atual;

        String nome = StringUtils.cleanPath(file.getOriginalFilename());
        String ext = getExtensao(nome);

        if (!isExtensaoValida(ext)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Formato inválido");
        }

        String novoNome = UUID.randomUUID() + ext;
        Path destino = uploadDir.resolve(novoNome);

        try (InputStream is = file.getInputStream()) {
            Files.copy(is, destino, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao salvar imagem");
        }

        deletarImagem(atual);

        return "manutencoes/" + novoNome;
    }

    private void deletarImagem(String path) {
        if (!StringUtils.hasText(path)) return;

        Path file = uploadDir.getParent().resolve(path);

        try {
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro ao deletar imagem");
        }
    }

    private String getExtensao(String nome) {
        int i = nome.lastIndexOf('.');
        return (i >= 0) ? nome.substring(i).toLowerCase(Locale.ROOT) : "";
    }

    private boolean isExtensaoValida(String ext) {
        return ext.equals(".png") || ext.equals(".jpg") || ext.equals(".jpeg")
                || ext.equals(".gif") || ext.equals(".webp");
    }

    public Object getByCodigo(String codigo) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getByCodigo'");
    }
}