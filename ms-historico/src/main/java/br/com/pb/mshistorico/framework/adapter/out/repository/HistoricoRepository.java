package br.com.pb.mshistorico.framework.adapter.out.repository;

import br.com.pb.mshistorico.domain.model.Historico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface HistoricoRepository extends MongoRepository<Historico, String> {

    Historico save(Historico request);

    Page<Historico> findByCef(String cef, Pageable pageable);

    Page<Historico> findByEventDate(LocalDate eventDate, Pageable pageable);

    Historico findByIdPedido(Long idPedido);

    Page<Historico> findByCefAndEventDate(String cef, LocalDate eventDate, Pageable pageable);
}
