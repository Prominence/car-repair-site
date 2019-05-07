package com.github.prominence.carrepair.controller.api;

import com.github.prominence.carrepair.model.Client;
import com.github.prominence.carrepair.model.Mechanic;
import com.github.prominence.carrepair.service.ClientService;
import com.github.prominence.carrepair.service.MechanicService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/autocomplete")
public class AutocompleteApiController {

    private ClientService clientService;
    private MechanicService mechanicService;

    public AutocompleteApiController(ClientService clientService, MechanicService mechanicService) {
        this.clientService = clientService;
        this.mechanicService = mechanicService;
    }

    @GetMapping(value = "/client", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Client> clientAutocomplete(@RequestParam String query) {
        return clientService.searchByInitials(query);
    }

    @GetMapping(value = "/mechanic", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Mechanic> mechanicAutocomplete(@RequestParam String query) {
        return mechanicService.searchByInitials(query);
    }
}
