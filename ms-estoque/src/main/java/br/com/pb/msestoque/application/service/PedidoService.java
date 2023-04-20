package br.com.pb.msestoque.application.service;

import br.com.pb.msestoque.application.ports.in.PedidoUseCase;
import br.com.pb.msestoque.domain.dto.request.PedidoRequestDTO;
import br.com.pb.msestoque.domain.dto.response.PageableDTO;
import br.com.pb.msestoque.domain.dto.response.PedidoDTO;
import br.com.pb.msestoque.domain.dto.response.PedidoProducerDTO;
import br.com.pb.msestoque.domain.model.Pedido;
import br.com.pb.msestoque.domain.model.Produto;
import br.com.pb.msestoque.infrastructure.adapter.out.event.TopicProducer;
import br.com.pb.msestoque.infrastructure.adapter.out.repository.PedidoRepository;
import br.com.pb.msestoque.infrastructure.exception.GenericException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class PedidoService implements PedidoUseCase {

    private final PedidoRepository repository;

    private final ModelMapper modelMapper;

    private final TopicProducer producer;

    private final ObjectMapper objectMapper;

    @Override
    public PedidoDTO create(PedidoRequestDTO request) throws JsonProcessingException {
        Pedido pedido = modelMapper.map(request, Pedido.class);
        this.validateDates(pedido);
        pedido.setValorTotal(calculateTotalValue(pedido));
        pedido.setPesoTotal(calculatePesoTotal(pedido));
        pedido.setQuantidadeTotal(calculateTotalQuantity(pedido));
        repository.save(pedido);
        PedidoProducerDTO pedidoProducer = modelMapper.map(pedido, PedidoProducerDTO.class);
        log.info("## Dados enviados pelo elfo: {}", pedidoProducer.getCef());

        String message = objectMapper.writeValueAsString(pedidoProducer);

        producer.sendMessage(message);
        return modelMapper.map(pedido, PedidoDTO.class);
    }

    @Override
    public PageableDTO findAll(String cef, BigDecimal valorTotal, Pageable pageable) {
        Page<Pedido> page;

        if ((cef == null || cef.trim().length() == 0) && valorTotal == null) {
            page = repository.findAll(pageable);
        } else if ((cef != null && cef.trim().length() > 0) && valorTotal == null) {
            page = repository.findByCef(cef.trim(), pageable);
        } else if (cef == null || cef.trim().length() == 0) {
            page = repository.findByValorTotal(valorTotal, pageable);
        } else {
            page = repository.findByCefAndValorTotal(cef.trim(), valorTotal, pageable);
        }

        if (page.isEmpty()) {
            throw new GenericException(HttpStatus.BAD_REQUEST, "Nenhum pedido encontrado.");
        }

        List<Pedido> pedidos = page.getContent();

        return PageableDTO
                .builder()
                .numberOfElements(page.getNumberOfElements())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .pedidos(pedidos)
                .build();
    }

    @Override
    public PedidoDTO findById(Long id) {
        Pedido pedido = repository
                .findById(id)
                .orElseThrow(() -> new GenericException(HttpStatus.BAD_REQUEST, "Id não encontrado!"));
        return modelMapper.map(pedido, PedidoDTO.class);
    }

    @Override
    public PedidoDTO update(Long id, PedidoRequestDTO request) throws JsonProcessingException {
        Pedido pedido = repository
                .findById(id)
                .orElseThrow(() -> new GenericException(HttpStatus.BAD_REQUEST, "Id não encontrado!"));

        pedido.setNome(request.getNome());
        pedido.setCef(request.getCef());

        PedidoProducerDTO pedidoProducer = modelMapper.map(pedido, PedidoProducerDTO.class);

        log.info("## Dados atualizados pelo elfo: {}", pedidoProducer.getCef());

        String message = objectMapper.writeValueAsString(pedidoProducer);

        producer.sendMessage(message);
        repository.save(pedido);
        return modelMapper.map(pedido, PedidoDTO.class);
    }

    @Override
    public void delete(Long id) {
        repository.findById(id).orElseThrow(() ->
                new GenericException(HttpStatus.BAD_REQUEST, "Id não encontrado!"));
        repository.deleteById(id);
    }

    private BigDecimal calculateTotalValue(Pedido pedido) {
        List<Produto> produtos = pedido.getProdutos();
        BigDecimal valorTotal = new BigDecimal(0);
        for (Produto produto : produtos) {
            BigDecimal quantidade = BigDecimal.valueOf(produto.getQuantidade());
            valorTotal = valorTotal.add(produto.getValor().multiply(quantidade));
        }
        return valorTotal;
    }

    //mudar tipo do peso para double
    private Long calculatePesoTotal(Pedido pedido) {
        List<Produto> produtos = pedido.getProdutos();
        Long pesoTotal = 0L;
        for (Produto produto : produtos) {
            Long peso = produto.getPeso() * produto.getQuantidade();
            pesoTotal+=peso;
        }
        return pesoTotal;
    }

    private Long calculateTotalQuantity(Pedido pedido) {
        List<Produto> produtos = pedido.getProdutos();
        Long quantidadeTotal = 0L;
        for (Produto produto : produtos) {
            quantidadeTotal+=produto.getQuantidade();
        }
        return quantidadeTotal;
    }

    private void validateDates(Pedido pedido) {
        List<Produto> produtos = pedido.getProdutos();
        for (Produto produto : produtos) {
            if (produto.getDataDeValidade().isBefore(LocalDate.now())) {
                throw new GenericException(
                        HttpStatus.BAD_REQUEST,
                        "A data de expiração não pode ser anterior à data atual."
                );
            }
        }
    }

}
