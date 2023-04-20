package br.com.pb.msestoque.application.ports.in;

import br.com.pb.msestoque.domain.dto.response.PageableDTO;
import br.com.pb.msestoque.domain.dto.response.PageableProdutoDTO;
import br.com.pb.msestoque.domain.dto.response.ProdutoDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.UUID;

public interface ProdutoUseCase {

    PageableProdutoDTO findAll(String nomeDoProduto, String categoria, LocalDate dataDeValidade, Pageable pageable);
    ProdutoDTO patch(UUID id, ProdutoDTO request) throws JsonProcessingException;
}
