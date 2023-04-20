package br.com.pb.mshistorico.application.ports.in;

import br.com.pb.mshistorico.domain.dto.PageableDTO;
import br.com.pb.mshistorico.domain.model.Historico;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface HistoricoUseCase {

    void save(Historico historico);

    PageableDTO findAll(String cef, LocalDate eventDate, Pageable pageable);

    void update(Long idPedido, Historico request);
}
