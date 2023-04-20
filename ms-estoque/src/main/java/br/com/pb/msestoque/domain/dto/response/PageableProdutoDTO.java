package br.com.pb.msestoque.domain.dto.response;

import br.com.pb.msestoque.domain.model.Pedido;
import br.com.pb.msestoque.domain.model.Produto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class PageableProdutoDTO {

    private Integer numberOfElements;
    private Long totalElements;
    private Integer totalPages;
    private List<Produto> produtos;
}
