package com.github.prominence.carrepair.controller.advice;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@ControllerAdvice
public class PaginationAdvice {

    private static final int DEFAULT_PAGE_SIZE = 10;

    @ModelAttribute("pagination")
    public void addPagination(@RequestParam(required = false) Integer size, @RequestParam(required = false) Integer page, Model model) {
        final int pageValue = page == null ? 0 : page;
        final int sizeValue = size == null ? DEFAULT_PAGE_SIZE : size;
        Pageable pagination = PageRequest.of(pageValue, sizeValue);

        model.addAttribute("pagination", pagination);
        model.addAttribute("page", pageValue);
    }
}
