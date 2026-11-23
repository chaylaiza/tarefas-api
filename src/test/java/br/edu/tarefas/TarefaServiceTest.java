package br.edu.tarefas;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)   // ativa o Mockito no JUnit 5
class TarefaServiceTest {

    @Mock                              // repositório falso: não acessa o PostgreSQL
    private TarefaRepository repository;

    @InjectMocks                       // cria o Service real e injeta o mock acima
    private TarefaService service;

    // ---------- CENÁRIOS DE SUCESSO ----------

    @Test
    void deveCadastrarTarefaValida() {
        // Arrange: prepara o cenário e configura o comportamento do mock
        Tarefa nova = new Tarefa("Estudar Docker", "Capítulo 11", 3);
        when(repository.existsByTituloIgnoreCase("Estudar Docker")).thenReturn(false);
        when(repository.save(any(Tarefa.class))).thenAnswer(inv -> inv.getArgument(0));

        // Act: executa o método testado
        Tarefa resultado = service.criar(nova);

        // Assert: confere o resultado
        assertEquals("Estudar Docker", resultado.getTitulo());
        assertFalse(resultado.isConcluida());
        // Verifica a interação: o Service deve ter chamado save() exatamente uma vez
        verify(repository, times(1)).save(nova);
    }

    @Test
    void deveAtualizarTarefaExistente() {
        // Arrange
        Tarefa existente = new Tarefa("Antigo", "desc", 1);
        when(repository.findById(1L)).thenReturn(Optional.of(existente));
        when(repository.save(existente)).thenReturn(existente);

        // Act
        Tarefa resultado = service.atualizar(1L, new Tarefa("Novo", "nova desc", 5));

        // Assert
        assertEquals("Novo", resultado.getTitulo());
        assertEquals(5, resultado.getPrioridade());
        verify(repository).save(existente);
    }

    @Test
    void deveExcluirTarefaExistente() {
        // Arrange
        when(repository.existsById(1L)).thenReturn(true);

        // Act
        service.excluir(1L);

        // Assert (verificação de interação)
        verify(repository).deleteById(1L);
    }

    // ---------- CENÁRIOS DE ERRO ----------

    @Test
    void naoDeveCadastrarTituloDuplicado() {
        // Arrange
        when(repository.existsByTituloIgnoreCase("Estudar Docker")).thenReturn(true);

        // Act + Assert: espera a exceção de regra de negócio
        assertThrows(RegraDeNegocioException.class,
                () -> service.criar(new Tarefa("Estudar Docker", "x", 2)));

        // Nada pode ter sido salvo
        verify(repository, never()).save(any());
    }

    @Test
    void deveLancarExcecaoAoBuscarIdInexistente() {
        // Arrange
        when(repository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(TarefaNaoEncontradaException.class, () -> service.buscarPorId(99L));
    }

    @Test
    void naoDeveConcluirTarefaJaConcluida() {
        // Arrange
        Tarefa tarefa = new Tarefa("Revisar", "x", 2);
        tarefa.setConcluida(true);
        when(repository.findById(1L)).thenReturn(Optional.of(tarefa));

        // Act + Assert
        assertThrows(RegraDeNegocioException.class, () -> service.concluir(1L));
        verify(repository, never()).save(any());
    }

    @Test
    void naoDeveExcluirTarefaInexistente() {
        // Arrange
        when(repository.existsById(99L)).thenReturn(false);

        // Act + Assert
        assertThrows(TarefaNaoEncontradaException.class, () -> service.excluir(99L));
        verify(repository, never()).deleteById(any());
    }
}
