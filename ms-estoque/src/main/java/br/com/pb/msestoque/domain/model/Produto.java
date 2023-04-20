package br.com.pb.msestoque.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @NotBlank(message = "Campo nome não deve estar em branco.")
    @NotNull(message = "Campo nome não deve estar nulo.")
    private String nomeDoProduto;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @NotNull(message = "Campo data de validade não deve estar nulo.")
    private LocalDate dataDeValidade;

    @Pattern(regexp = "ALIMENTO|SUPRIMENTO", message = "O campo categoria só aceita os valores ALIMENTO e SUPRIMENTO")
    @NotNull(message = "Campo categoria não deve estar nulo.")
    private String categoria;

    @Positive(message = "O valor deve ser maior que zero.")
    @NotNull(message = "Campo valor não deve estar nulo.")
    private BigDecimal valor;

    @Positive(message = "O peso deve ser maior que zero.")
    @NotNull(message = "Campo peso não deve estar nulo.")
    private Long peso;

    @PositiveOrZero(message = "A quantidade deve ser maior ou igual a zero.")
    @NotNull(message = "Campo quantidade não deve estar nulo.")
    private Long quantidade;
}
