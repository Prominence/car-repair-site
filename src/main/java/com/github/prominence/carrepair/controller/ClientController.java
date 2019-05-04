package com.github.prominence.carrepair.controller;

import com.github.prominence.carrepair.model.Client;
import com.github.prominence.carrepair.service.ClientService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/client")
public class ClientController {

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String index(ModelMap modelMap) {
        Page<Client> clientList = clientService.findAll((Pageable) modelMap.get("pagination"));

        modelMap.addAttribute("clientList", clientList.getContent());
        modelMap.addAttribute("totalPages", clientList.getTotalPages());

        return "client/index";
    }

    @GetMapping(value = "/create")
    public String create(Model model) {
        model.addAttribute("client", new Client());

        return "client/edit";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Optional<Client> clientOptional = clientService.findById(id);
        if (clientOptional.isPresent()) {
            model.addAttribute("client", clientOptional.get());
            return "client/edit";
        } else {
            // TODO: need to show warning
            return "redirect:/client";
        }
    }

    @PostMapping(value = "/update/{id}")
    public String update(@Valid Client client, BindingResult bindingResult, @PathVariable long id) {
        if (bindingResult.hasErrors()) {
            client.setId(id); // why should we do this?
            return "client/edit";
        }

        clientService.save(client);
        return "redirect:/client";
    }

    @PostMapping(value = "/create")
    public String save(@Valid Client client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "client/edit";
        }

        clientService.save(client);
        return "redirect:/client";
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        boolean deleteSuccess = clientService.deleteClientById(id);

        if (deleteSuccess) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
