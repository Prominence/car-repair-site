package com.github.prominence.carrepair.controller;

import com.github.prominence.carrepair.enums.OrderStatus;
import com.github.prominence.carrepair.model.Mechanic;
import com.github.prominence.carrepair.service.MechanicService;
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
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/mechanic")
public class MechanicController {
    private static final Logger logger = LogManager.getLogger(MechanicController.class);

    private MechanicService mechanicService;

    public MechanicController(MechanicService mechanicService) {
        this.mechanicService = mechanicService;
    }

    @GetMapping
    public String index(@PageableDefault Pageable pageable, ModelMap modelMap) {
        Page<Mechanic> mechanicList = mechanicService.findAll(pageable);
        logger.trace("Request to open list page for Mechanics. Returning {} page.", () -> pageable.getPageNumber() + 1);

        modelMap.addAttribute("mechanicList", mechanicList.getContent());
        modelMap.addAttribute("totalPages", mechanicList.getTotalPages());

        return "mechanic/index";
    }

    @GetMapping(value = "/create")
    public String create(Model model) {
        logger.trace("Request to open create page for Mechanic.");
        model.addAttribute("mechanic", new Mechanic());

        return "mechanic/edit";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        logger.trace("Request to open edit page for Mechanic[{}].", () -> id);
        Optional<Mechanic> mechanicOptional = mechanicService.findById(id);
        if (mechanicOptional.isPresent()) {
            model.addAttribute("mechanic", mechanicOptional.get());
            return "mechanic/edit";
        } else {
            return "redirect:/mechanic";
        }
    }

    @PostMapping(value = "/update/{id}")
    public String update(@Valid Mechanic mechanic, BindingResult bindingResult, @PathVariable long id) {
        logger.trace("Request to save {}.", () -> mechanic);
        if (bindingResult.hasErrors()) {
            logger.trace("{} has validation {} errors and won't be saved.", () -> mechanic, bindingResult::getErrorCount);
            mechanic.setId(id); // why should we do this?
            return "mechanic/edit";
        }

        mechanicService.save(mechanic);
        return "redirect:/mechanic";
    }

    @PostMapping(value = "/create")
    public String save(@Valid Mechanic mechanic, BindingResult bindingResult) {
        logger.trace("Request to create {}.", () -> mechanic);
        if (bindingResult.hasErrors()) {
            logger.trace("{} has validation {} errors and won't be created.", () -> mechanic, bindingResult::getErrorCount);
            return "mechanic/edit";
        }

        mechanicService.save(mechanic);
        return "redirect:/mechanic";
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity delete(@PathVariable Long id) {
        logger.trace("Request to delete Mechanic[{}].", () -> id);
        boolean deleteSuccess = mechanicService.deleteMechanicById(id);
        if (deleteSuccess) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/statistics/{id}")
    public String statistics(@PathVariable Long id, Model model) {
        logger.trace("Request to open statistics for Mechanic[{}].", () -> id);
        Map<OrderStatus, Integer> mechanicStatistics = mechanicService.getOrderStatistics(id);
        model.addAttribute("statistics", mechanicStatistics);

        return "mechanic/statistics";
    }

}
