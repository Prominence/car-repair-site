package com.github.prominence.carrepair.controller;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.model.Mechanic;
import com.github.prominence.carrepair.service.MechanicService;
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
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/mechanic")
public class MechanicController {

    private MechanicService mechanicService;

    public MechanicController(MechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }

    @GetMapping
    public String index(@PageableDefault Pageable pageable, ModelMap modelMap) {
        Page<Mechanic> clientList = mechanicService.findAll(pageable);

        modelMap.addAttribute("mechanicList", clientList.getContent());
        modelMap.addAttribute("totalPages", clientList.getTotalPages());

        return "mechanic/index";
    }

    @GetMapping(value = "/create")
    public String create(Model model) {
        model.addAttribute("mechanic", new Mechanic());

        return "mechanic/edit";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        Optional<Mechanic> clientOptional = mechanicService.findById(id);
        if (clientOptional.isPresent()) {
            model.addAttribute("mechanic", clientOptional.get());
            return "mechanic/edit";
        } else {
            // TODO: need to show warning
            return "redirect:/mechanic";
        }
    }

    @PostMapping(value = "/update/{id}")
    public String update(@Valid Mechanic client, BindingResult bindingResult, @PathVariable long id) {
        if (bindingResult.hasErrors()) {
            client.setId(id); // why should we do this?
            return "mechanic/edit";
        }

        mechanicService.save(client);
        return "redirect:/mechanic";
    }

    @PostMapping(value = "/create")
    public String save(@Valid Mechanic client, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "mechanic/edit";
        }

        mechanicService.save(client);
        return "redirect:/mechanic";
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        boolean deleteSuccess = mechanicService.deleteMechanicById(id);

        if (deleteSuccess) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/statistics/{id}")
    public String statistics(@PathVariable Long id, Model model) {
        Map<OrderStatus, Integer> mechanicStatistics = mechanicService.getOrderStatistics(id);
        model.addAttribute("statistics", mechanicStatistics);

        return "mechanic/statistics";
    }

}
