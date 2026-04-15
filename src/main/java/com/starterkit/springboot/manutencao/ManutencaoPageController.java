package com.starterkit.springboot.manutencao;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/manutencoes")
public class ManutencaoPageController {

    private final ManutencaoService service;

    @Value("${security.api-key.admin:}")
    private String adminKey;

    public ManutencaoPageController(ManutencaoService service) {
        this.service = service;
    }

    @GetMapping
    public String lista(Model model) {
        model.addAttribute("titulo", "Gestao de Manutencoes");
        model.addAttribute("manutencoes", service.listar());
        model.addAttribute("pageScript", "/js/manutencoes.js");
        return "manutencoes/lista";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("titulo", "Nova Manutencao");
        model.addAttribute("manutencaoForm", new ManutencaoForm());
        model.addAttribute("modo", "novo");
        model.addAttribute("pageScript", "/js/manutencoes.js");
        return "manutencoes/form";
    }

    @GetMapping("/{id}/editar")
    public String editar(@PathVariable Long id, Model model) {
        Manutencao m = service.buscarPorId(id);

        ManutencaoForm form = new ManutencaoForm();
        copyToForm(m, form);

        model.addAttribute("titulo", "Editar Manutencao");
        model.addAttribute("manutencao", m);
        model.addAttribute("manutencaoForm", form);
        model.addAttribute("modo", "editar");
        model.addAttribute("pageScript", "/js/manutencoes.js");

        return "manutencoes/form";
    }

    @GetMapping("/{id}")
    public String detalhe(@PathVariable Long id, Model model) {
        model.addAttribute("titulo", "Ficha da Manutencao");
        model.addAttribute("manutencao", service.buscarPorId(id));
        return "manutencoes/detalhe";
    }

    @GetMapping("/codigo/{codigo}")
    public String detalhePorCodigo(@PathVariable String codigo, Model model) {
        model.addAttribute("titulo", "Ficha da Manutencao");
        model.addAttribute("manutencao", service.getByCodigo(codigo));
        return "manutencoes/detalhe";
    }

    @GetMapping("/scan")
    public String scan(Model model) {
        model.addAttribute("titulo", "Ler QR Code");
        return "manutencoes/scan";
    }

    @PostMapping
    public String criar(@Valid ManutencaoForm form, BindingResult result, Model model) {

        if (!isValidAdminKey(form.getAdminApiKey())) {
            result.rejectValue("adminApiKey", "invalid", "Chave inválida");
        }

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Nova Manutencao");
            model.addAttribute("modo", "novo");
            return "manutencoes/form";
        }

        service.criarComImagem(form);

        return "redirect:/manutencoes?status=created";
    }

    @PostMapping("/{id}")
    public String atualizar(@PathVariable Long id,
                            @Valid ManutencaoForm form,
                            BindingResult result,
                            Model model) {

        Manutencao m = service.buscarPorId(id);

        if (!isValidAdminKey(form.getAdminApiKey())) {
            result.rejectValue("adminApiKey", "invalid", "Chave inválida");
        }

        if (result.hasErrors()) {
            model.addAttribute("titulo", "Editar Manutencao");
            model.addAttribute("modo", "editar");
            model.addAttribute("manutencao", m);
            return "manutencoes/form";
        }

        service.atualizarComImagem(id, form);

        return "redirect:/manutencoes?status=updated";
    }

    private boolean isValidAdminKey(String key) {
        return adminKey != null && !adminKey.isEmpty() && adminKey.equals(key);
    }

    private void copyToForm(Manutencao m, ManutencaoForm f) {
        f.setCategoria(m.getCategoria());
        f.setDataCompra(m.getDataCompra());
        f.setModelo(m.getModelo());
        f.setMarca(m.getMarca());
        f.setNumeroSerie(m.getNumeroSerie());
        f.setLocal(m.getLocal());
        f.setGarantia(m.getGarantia());
        f.setSeguro(m.getSeguro());
    }
}