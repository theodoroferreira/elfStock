package br.com.pb.msestoque.domain.dto.response;

import br.com.pb.msestoque.domain.model.Produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoDTO {

    private String nome;
    private String cef;
    private List<Produto> produtos;
    private BigDecimal valorTotal;
    private Long pesoTotal;
    private Long quantidadeTotal;
}
