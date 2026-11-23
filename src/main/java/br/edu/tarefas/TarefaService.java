package br.edu.tarefas;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TarefaService {

    private static final Logger log = LoggerFactory.getLogger(TarefaService.class);

    private final TarefaRepository repository;

    public TarefaService(TarefaRepository repository) {
        this.repository = repository;
    }

    public Tarefa criar(Tarefa tarefa) {
        // Regra de negócio: não permitir títulos duplicados
        if (repository.existsByTituloIgnoreCase(tarefa.getTitulo())) {
            log.warn("Tentativa de criar tarefa duplicada");
            throw new RegraDeNegocioException("Já existe uma tarefa com esse título");
        }
        tarefa.setConcluida(false);
        Tarefa salva = repository.save(tarefa);
        log.info("Tarefa criada com id {}", salva.getId());
        return salva;
    }

    public List<Tarefa> listar() {
        return repository.findAll();
    }

    public Tarefa buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TarefaNaoEncontradaException(id));
    }

    public Tarefa atualizar(Long id, Tarefa dados) {
        Tarefa existente = buscarPorId(id);
        existente.setTitulo(dados.getTitulo());
        existente.setDescricao(dados.getDescricao());
        existente.setPrioridade(dados.getPrioridade());
        log.info("Tarefa {} atualizada", id);
        return repository.save(existente);
    }

    public Tarefa concluir(Long id) {
        Tarefa tarefa = buscarPorId(id);
        // Regra de negócio: não concluir uma tarefa já concluída
        if (tarefa.isConcluida()) {
            throw new RegraDeNegocioException("A tarefa já está concluída");
        }
        tarefa.setConcluida(true);
        log.info("Tarefa {} concluída", id);
        return repository.save(tarefa);
    }

    public void excluir(Long id) {
        if (!repository.existsById(id)) {
            throw new TarefaNaoEncontradaException(id);
        }
        repository.deleteById(id);
        log.info("Tarefa {} excluída", id);
    }
}
