package br.com.pb.msestoque.domain.dto.request;

import br.com.pb.msestoque.domain.model.Pedido;
import br.com.pb.msestoque.domain.model.Produto;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PedidoRequestDTO {

    private String nome;
    @Min(value = 11, message = "Campo CEF deve conter apenas dígitos.")
    private String cef;
    private List<Produto> produtos;
}
