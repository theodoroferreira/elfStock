package br.com.pb.msestoque.domain.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.br.CPF;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "pedidos")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Campo nome não deve estar em branco.")
    private String nome;

    @CPF(message = "CEF inválido")
    private String cef;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Produto> produtos;

    private BigDecimal valorTotal;

    private Long pesoTotal;

    private Long quantidadeTotal;
}
