package br.edu.tarefas;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TarefaController.class)   // sobe só a camada web
class TarefaControllerTest {

    @Autowired
    private MockMvc mockMvc;           // simula requisições HTTP sem servidor real

    @MockBean
    private TarefaService service;     // Service falso

    private static final String JSON_VALIDO =
            "{\"titulo\":\"Estudar\",\"descricao\":\"Aula 10\",\"prioridade\":3}";

    @Test
    void deveListarTarefas() throws Exception {
        when(service.listar()).thenReturn(List.of(
                new Tarefa("A", "d", 1), new Tarefa("B", "d", 2)));

        mockMvc.perform(get("/tarefas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].titulo").value("A"));
    }

    @Test
    void deveCriarTarefaERetornar201() throws Exception {
        when(service.criar(any(Tarefa.class))).thenReturn(new Tarefa("Estudar", "Aula 10", 3));

        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_VALIDO))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Estudar"));
    }

    @Test
    void deveRetornar400QuandoTituloVazio() throws Exception {
        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"\",\"prioridade\":3}"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(service);
    }

    @Test
    void deveRetornar400QuandoPrioridadeInvalida() throws Exception {
        mockMvc.perform(post("/tarefas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"X\",\"prioridade\":9}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deveRetornar404QuandoTarefaNaoExiste() throws Exception {
        when(service.buscarPorId(99L)).thenThrow(new TarefaNaoEncontradaException(99L));

        mockMvc.perform(get("/tarefas/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveAtualizarTarefa() throws Exception {
        when(service.atualizar(eq(1L), any(Tarefa.class))).thenReturn(new Tarefa("Estudar", "Aula 10", 3));

        mockMvc.perform(put("/tarefas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(JSON_VALIDO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.prioridade").value(3));
    }

    @Test
    void deveExcluirTarefaERetornar204() throws Exception {
        mockMvc.perform(delete("/tarefas/1"))
                .andExpect(status().isNoContent());

        verify(service).excluir(1L);
    }

    @Test
    void deveRetornar409QuandoRegraDeNegocioViolada() throws Exception {
        when(service.concluir(1L)).thenThrow(new RegraDeNegocioException("A tarefa já está concluída"));

        mockMvc.perform(patch("/tarefas/1/concluir"))
                .andExpect(status().isConflict());
    }
}
