package br.com.pb.msestoque.application.ports.in;

import br.com.pb.msestoque.domain.dto.request.PedidoRequestDTO;
import br.com.pb.msestoque.domain.dto.response.PedidoDTO;
import br.com.pb.msestoque.domain.dto.response.PageableDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

public interface PedidoUseCase {
    PedidoDTO create(PedidoRequestDTO request) throws JsonProcessingException;

    PageableDTO findAll(String cpf, BigDecimal totalValue, Pageable pageable);

    PedidoDTO findById(Long id);

    PedidoDTO update(Long id, PedidoRequestDTO request) throws JsonProcessingException;

    void delete(Long id);
}
