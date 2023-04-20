package br.com.pb.msestoque.domain.dto.response;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProdutoDTO {

    private String nomeDoProduto;
    private LocalDate dataDeValidade;
    private String categoria;
    private BigDecimal valor;
    private Long peso;
    private Long quantidade;
}
