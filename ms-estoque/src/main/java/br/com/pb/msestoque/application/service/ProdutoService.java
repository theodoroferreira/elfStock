package br.com.pb.msestoque.application.service;

import br.com.pb.msestoque.application.ports.in.ProdutoUseCase;
import br.com.pb.msestoque.domain.dto.response.PageableProdutoDTO;
import br.com.pb.msestoque.domain.dto.response.PedidoProducerDTO;
import br.com.pb.msestoque.domain.dto.response.ProdutoDTO;
import br.com.pb.msestoque.domain.model.Pedido;
import br.com.pb.msestoque.domain.model.Produto;
import br.com.pb.msestoque.infrastructure.adapter.out.event.TopicProducer;
import br.com.pb.msestoque.infrastructure.adapter.out.repository.PedidoRepository;
import br.com.pb.msestoque.infrastructure.adapter.out.repository.ProdutoRepository;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProdutoService implements ProdutoUseCase {

    private final ProdutoRepository repository;

    private final PedidoRepository pedidoRepository;

    private final ModelMapper modelMapper;

    private final TopicProducer producer;

    private final ObjectMapper objectMapper;

    @Override
    public PageableProdutoDTO findAll(String nomeDoProduto, String categoria, LocalDate dataDeValidade, Pageable pageable) {
        Page<Produto> page;

//        if (nomeDoProduto == null || nomeDoProduto.trim().length() == 0) {
//            page = repository.findAll(pageable);
//        } else {
//            page = repository.findByNomeDoProduto((nomeDoProduto.trim()), pageable);
//            if (page.isEmpty()) {
//                throw new GenericException(HttpStatus.BAD_REQUEST, "Nenhum produto encontrado com esse nome.");
//            }
//        }
//
//        if (categoria == null || categoria.trim().length() == 0) {
//            page = repository.findAll(pageable);
//        } else {
//            page = repository.findByCategoria(categoria.trim(), pageable);
//            if (page.isEmpty()) {
//                throw new GenericException(HttpStatus.BAD_REQUEST, "Nenhum produto encontrado com essa categoria.");
//            }
//        }
//
//        if (dataDeValidade == null) {
//            page = repository.findAll(pageable);
//        } else {
//            page = repository.findByDataDeValidade(dataDeValidade, pageable);
//            if (page.isEmpty()) {
//                throw new GenericException(HttpStatus.BAD_REQUEST, "Nenhum produto encontrado com essa data de validade.");
//            }
//        }

        if ((nomeDoProduto == null || nomeDoProduto.trim().length() == 0)
                && (categoria == null || categoria.trim().length() == 0)
                && dataDeValidade == null) {
            page = repository.findAll(pageable);
        } else if ((nomeDoProduto != null && nomeDoProduto.trim().length() > 0)
                && (categoria == null || categoria.trim().length() == 0)
                && dataDeValidade == null) {
            page = repository.findByNomeDoProduto(nomeDoProduto.trim(), pageable);
        } else if ((nomeDoProduto == null || nomeDoProduto.trim().length() == 0)
                && (categoria != null && categoria.trim().length() > 0)
                && dataDeValidade == null) {
            page = repository.findByCategoria(categoria.trim(), pageable);
        } else if ((nomeDoProduto == null || nomeDoProduto.trim().length() == 0) && (categoria == null || categoria.trim().length() == 0)) {
            page = repository.findByDataDeValidade(dataDeValidade, pageable);
        } else {
            page = repository.findByNomeDoProdutoAndCategoriaAndDataDeValidade(
                    nomeDoProduto.trim(),
                    categoria.trim(),
                    dataDeValidade,
                    pageable);
        }

        if (page.isEmpty()) {
            throw new GenericException(HttpStatus.BAD_REQUEST, "Nenhum produto encontrado.");
        }

        List<Produto> produtos = page.getContent();

        return PageableProdutoDTO
                .builder()
                .numberOfElements(page.getNumberOfElements())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .produtos(produtos)
                .build();
    }

    @Override
    public ProdutoDTO patch(UUID uuid, ProdutoDTO request) throws JsonProcessingException {
        Produto produto = repository
            .findByUuid(uuid);

        if (produto==null) {
            throw new GenericException(HttpStatus.BAD_REQUEST, "Id não encontrado!");
        }

        this.validateDates(produto);

        if (request.getCategoria() != null) {
            produto.setCategoria(request.getCategoria());
        }
        if (request.getValor() != null) {
            produto.setValor(request.getValor());
        }

        repository.save(produto);
        updatePedidoTotalValue(produto);
        return modelMapper.map(produto, ProdutoDTO.class);
    }

    private BigDecimal calculateTotalValue(Pedido pedido) {
        List<Produto> produtos = pedido.getProdutos();
        return produtos.stream()
                .map(p -> p.getValor().multiply(BigDecimal.valueOf(p.getQuantidade())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void updatePedidoTotalValue(Produto produto) throws JsonProcessingException {
        Pedido pedido = pedidoRepository.findByProdutosContains(produto);
        pedido.setValorTotal(calculateTotalValue(pedido));
        pedidoRepository.save(pedido);

        log.info("## Atualizando valor total do pedido: {}", pedido.getValorTotal());

        PedidoProducerDTO pedidoProducer = modelMapper.map(pedido, PedidoProducerDTO.class);
        String message = objectMapper.writeValueAsString(pedidoProducer);

        producer.sendMessage(message);
    }

    private void validateDates(Produto produto) {
            if (produto.getDataDeValidade().isBefore(LocalDate.now())) {
                throw new GenericException(
                        HttpStatus.BAD_REQUEST,
                        "A data de validade não pode ser anterior à data atual."
                );
        }
    }
}
