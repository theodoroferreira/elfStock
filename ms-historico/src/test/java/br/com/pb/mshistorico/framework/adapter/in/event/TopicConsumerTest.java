package br.com.pb.mshistorico.framework.adapter.in.event;

import br.com.pb.mshistorico.application.service.HistoricoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@EmbeddedKafka(controlledShutdown = true)
@DirtiesContext
class TopicConsumerTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockBean
    private HistoricoService historyService;

    @Autowired
    private ObjectMapper objectMapper;

    private final String TOPIC = "${spring.kafka.topic.estoque-historico}";


}