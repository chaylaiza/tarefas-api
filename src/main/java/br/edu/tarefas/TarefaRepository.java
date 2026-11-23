package br.edu.tarefas;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    boolean existsByTituloIgnoreCase(String titulo);
}
