package br.com.pb.msestoque.infrastructure.adapter.in.rest;

import br.com.pb.msestoque.application.ports.in.ProdutoUseCase;
import br.com.pb.msestoque.domain.dto.response.PageableProdutoDTO;
import br.com.pb.msestoque.domain.dto.response.ProdutoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RequestMapping("/produtos")
@RestController
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoUseCase produtoService;

    @GetMapping
    public PageableProdutoDTO findAll(@RequestParam(required = false) @Valid String nomeDoProduto,
                                      @RequestParam(required = false) @DateTimeFormat(pattern="dd-MM-yyyy") @Valid LocalDate dataDeValidade,
                                      @RequestParam(required = false) @Valid String categoria,
                                      @SortDefault(sort = "dataDeValidade", direction = Sort.Direction.DESC) Pageable pageable) {
        return produtoService.findAll(nomeDoProduto, categoria, dataDeValidade, pageable);
    }
    
    @PatchMapping("/{uuid}")
    public ResponseEntity<ProdutoDTO> patch(@PathVariable UUID uuid, @RequestBody @Valid ProdutoDTO request) throws JsonProcessingException {
        return ResponseEntity.status(HttpStatus.OK).body(produtoService.patch(uuid, request));
    }
}
