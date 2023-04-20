package br.com.pb.mshistorico.framework.adapter.in.event;

import br.com.pb.mshistorico.application.service.HistoricoService;
import br.com.pb.mshistorico.domain.model.Historico;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class TopicConsumer {

    private final HistoricoService service;

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "${spring.kafka.topic.order-history}", groupId = "${spring.kafka.consumer.group-id}")
    public void getTopicConsumer(String message) throws JsonProcessingException {
        log.info("Mensagem Histórico {}", message);

        Historico request = objectMapper.readValue(message, Historico.class);

        service.update(request.getIdPedido(), request);

        log.info("Pedido salvo na base com sucesso: {}", request);
    }
}
