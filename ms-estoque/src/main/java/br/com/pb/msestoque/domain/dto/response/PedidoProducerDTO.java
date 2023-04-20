package br.com.pb.msestoque.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoProducerDTO {

    private Long idPedido;
    private String cef;
    private BigDecimal valorTotal;
}
