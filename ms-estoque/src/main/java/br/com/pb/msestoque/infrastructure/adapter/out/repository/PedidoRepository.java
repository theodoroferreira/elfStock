package br.com.pb.msestoque.infrastructure.adapter.out.repository;

import br.com.pb.msestoque.domain.model.Pedido;
import br.com.pb.msestoque.domain.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Page<Pedido> findAll(Pageable pageable);

    Page<Pedido> findByCef(String cef, Pageable pageable);

    Page<Pedido> findByValorTotal(BigDecimal valorTotal, Pageable pageable);

    Pedido findByProdutosContains(Produto produto);

    Page<Pedido> findByCefAndValorTotal(String cef, BigDecimal valorTotal, Pageable pageable);
}
