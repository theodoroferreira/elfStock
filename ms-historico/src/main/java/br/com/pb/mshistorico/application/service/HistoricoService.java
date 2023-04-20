package br.com.pb.mshistorico.application.service;

import br.com.pb.mshistorico.application.ports.in.HistoricoUseCase;
import br.com.pb.mshistorico.domain.dto.PageableDTO;
import br.com.pb.mshistorico.domain.model.Historico;
import br.com.pb.mshistorico.framework.adapter.out.repository.HistoricoRepository;
import br.com.pb.mshistorico.framework.exception.GenericException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoricoService implements HistoricoUseCase {

    private final HistoricoRepository repository;

    @Override
    public void save(Historico request) {
        request.setEventDate(LocalDate.now());
        repository.save(request);
    }

    @Override
    public PageableDTO findAll(String cef, LocalDate eventDate, Pageable pageable) {
        Page<Historico> page;

        if ((cef == null || cef.trim().length() == 0) && eventDate == null) {
            page = repository.findAll(pageable);
        } else if ((cef != null && cef.trim().length() > 0) && eventDate == null) {
            page = repository.findByCef(cef.trim(), pageable);
        } else if (cef == null || cef.trim().length() == 0) {
            page = repository.findByEventDate(eventDate, pageable);
        } else {
            page = repository.findByCefAndEventDate(cef.trim(), eventDate, pageable);
        }

        if (page.isEmpty()) {
            throw new GenericException(HttpStatus.BAD_REQUEST, "Nenhum pedido encontrado.");
        }

        List<Historico> historico = page.getContent();

        return PageableDTO
                .builder()
                .numberOfElements(page.getNumberOfElements())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .historicoList(historico)
                .build();
    }

    public void update(Long idPedido, Historico request) {
        Historico historico = repository.findByIdPedido(idPedido);
        if (historico==null) {
            request.setEventDate(LocalDate.now());
            repository.save(request);
        } else {
            historico.setEventDate(LocalDate.now());
            historico.setCef(request.getCef());
            historico.setValorTotal(request.getValorTotal());
            repository.save(historico);
        }
    }
}
