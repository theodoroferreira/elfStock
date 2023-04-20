package br.com.pb.msestoque.infrastructure.adapter.out.repository;

import br.com.pb.msestoque.domain.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    Page<Produto> findAll(Pageable pageable);

    Page<Produto> findByNomeDoProduto(String trim, Pageable pageable);

    Page<Produto> findByCategoria(String categoria, Pageable pageable);

    Page<Produto> findByDataDeValidade(LocalDate dataDeValidade, Pageable pageable);

    Produto findByUuid(UUID uuid);

    Page<Produto> findByNomeDoProdutoAndCategoriaAndDataDeValidade(String nomeDoProduto, String categoria, LocalDate dataDeValidade, Pageable pageable);
}
