package br.com.pb.mshistorico.framework.adapter.in.rest;

import br.com.pb.mshistorico.application.service.HistoricoService;
import br.com.pb.mshistorico.domain.dto.PageableDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = HistoricoController.class)
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class HistoricoControllerTest {

    private static final String URL = "/history";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HistoricoService service;

    @Test
    void findAll() throws Exception {
        PageableDTO pageableDTO = new PageableDTO();
        when(service.findAll(any(), any(), any())).thenReturn(pageableDTO);
        MvcResult result = mockMvc
                .perform(
                        MockMvcRequestBuilders
                                .get(URL)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andReturn();

        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
}