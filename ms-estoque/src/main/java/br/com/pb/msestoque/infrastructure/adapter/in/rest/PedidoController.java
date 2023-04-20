package br.com.pb.msestoque.infrastructure.adapter.in.rest;

import br.com.pb.msestoque.application.ports.in.PedidoUseCase;
import br.com.pb.msestoque.domain.dto.request.PedidoRequestDTO;
import br.com.pb.msestoque.domain.dto.response.PedidoDTO;
import br.com.pb.msestoque.domain.dto.response.PageableDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RequestMapping(value = "/pedidos")
@RequiredArgsConstructor
@RestController
public class PedidoController {

    private final PedidoUseCase orderService;
    @PostMapping
    public ResponseEntity<PedidoDTO> create(@RequestBody @Valid PedidoRequestDTO request) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.create(request));
    }

    @GetMapping
    public PageableDTO findAll(@RequestParam(required = false) String cef, @RequestParam(required = false) BigDecimal totalValue, Pageable pageable) {
        return orderService.findAll(cef, totalValue, pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PedidoDTO> update(@PathVariable Long id, @RequestBody @Valid PedidoRequestDTO request) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.OK).body(orderService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<PedidoDTO> delete(@PathVariable Long id) {
        orderService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
