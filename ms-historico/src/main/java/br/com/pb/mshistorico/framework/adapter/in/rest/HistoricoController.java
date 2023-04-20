package br.com.pb.mshistorico.framework.adapter.in.rest;

import br.com.pb.mshistorico.application.service.HistoricoService;
import br.com.pb.mshistorico.domain.dto.PageableDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/historico")
public class HistoricoController {

    private final HistoricoService service;

    @GetMapping
    public PageableDTO findAll(@RequestParam(required = false) String cef, @RequestParam(required = false) @DateTimeFormat(pattern="dd-MM-yyyy") LocalDate eventDate, @SortDefault(sort = "eventDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.findAll(cef, eventDate, pageable);
    }
}
