package com.github.prominence.carrepair.controller;

import com.github.prominence.carrepair.model.Client;
import com.github.prominence.carrepair.service.ClientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
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

    private static final Logger logger = LogManager.getLogger(ClientController.class);

    private ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping
    public String index(@PageableDefault Pageable pageable, ModelMap modelMap) {
        Page<Client> clientList = clientService.findAll(pageable);
        logger.trace("Request to open list page for Clients. Returning {} page.", () -> pageable.getPageNumber() + 1);

        modelMap.addAttribute("clientList", clientList.getContent());
        modelMap.addAttribute("totalPages", clientList.getTotalPages());

        return "client/index";
    }

    @GetMapping(value = "/create")
    public String create(Model model) {
        logger.trace("Request to open create page for Client.");
        model.addAttribute("client", new Client());

        return "client/edit";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        logger.trace("Request to open edit page for Client[{}].", () -> id);
        Optional<Client> clientOptional = clientService.findById(id);
        if (clientOptional.isPresent()) {
            model.addAttribute("client", clientOptional.get());
            return "client/edit";
        } else {
            return "redirect:/client";
        }
    }

    @PostMapping(value = "/update/{id}")
    public String update(@Valid Client client, BindingResult bindingResult, @PathVariable long id) {
        logger.trace("Request to save {}.", () -> client);
        if (bindingResult.hasErrors()) {
            logger.trace("{} has validation {} errors and won't be saved.", () -> client, bindingResult::getErrorCount);
            client.setId(id); // why should we do this?
            return "client/edit";
        }

        clientService.save(client);
        return "redirect:/client";
    }

    @PostMapping(value = "/create")
    public String save(@Valid Client client, BindingResult bindingResult) {
        logger.trace("Request to create {}.", () -> client);
        if (bindingResult.hasErrors()) {
            logger.trace("{} has validation {} errors and won't be created.", () -> client, bindingResult::getErrorCount);
            return "client/edit";
        }

        clientService.save(client);
        return "redirect:/client";
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        logger.trace("Request to delete Client[{}].", () -> id);
        boolean deleteSuccess = clientService.deleteClientById(id);
        if (deleteSuccess) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
