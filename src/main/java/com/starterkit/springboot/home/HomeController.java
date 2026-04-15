package com.starterkit.springboot.home;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.starterkit.springboot.equipamento.Equipamento;
import com.starterkit.springboot.equipamento.EquipamentoRepository;
import com.starterkit.springboot.fornecedor.Fornecedor;
import com.starterkit.springboot.fornecedor.FornecedorRepository;
import com.starterkit.springboot.manutencao.Manutencao;
import com.starterkit.springboot.user.UserRepository;

import java.util.List ;


@Controller
public class HomeController {

    private final EquipamentoRepository EquipRepo;
    private final FornecedorRepository FornRepo;
    private final UserRepository UserRepo;
   

    public HomeController(EquipamentoRepository EquipRepo,
         FornecedorRepository FornRepo,
        UserRepository UserRepo) {

        this.EquipRepo = EquipRepo;
        this.FornRepo = FornRepo;
        this.UserRepo = UserRepo;
       
    }

    @GetMapping("/")
    public String home(Model model) {

        model.addAttribute("titulo", "pagina principal");
        model.addAttribute("cenas", "gestão dos equipamentos Todos");

        model.addAttribute("equipamentos", EquipRepo.findAll());
        model.addAttribute("fornecedores", FornRepo.findAll());
        model.addAttribute("Utilizadores", UserRepo.findAll());
        model.addAttribute("manutentores", UserRepo.findAll());

        return "index";
    }

    @GetMapping("/fornecedores/{id}")
    public String fornecedorPorId(@PathVariable Long id, Model model) {

        Fornecedor fornecedor = FornRepo.findById(id).orElse(null);

        if (fornecedor == null) {
            return "notfound";
        }

        model.addAttribute("titulo", "Detalhe do fornecedor");
        model.addAttribute("fornecedores", fornecedor);
        return "fornecedores";
    }

    @GetMapping("/fornecedores")
    public String fornecedores(Model model) {

        model.addAttribute("titulo", "Página Principal dos Fornecedores");
        model.addAttribute("fornecedores", FornRepo.findAll());

        return "fornecedores";
    }

    @PostMapping("/fornecedores/save")
public String salvarFornecedor(Fornecedor fornecedor) {
    FornRepo.save(fornecedor);
    return "redirect:/"; // volta à lista
}

    @PostMapping("/manutencoes/save")
public String salvarManutencao(Manutencao manutencao) {
    ManuRepo.save(manutencao);
    return "redirect:/"; // volta à lista
}




}
   