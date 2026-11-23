package br.edu.tarefas;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    private final TarefaService service;

    public TarefaController(TarefaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tarefa criar(@Valid @RequestBody Tarefa tarefa) {
        return service.criar(tarefa);
    }

    @GetMapping
    public List<Tarefa> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public Tarefa buscar(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Tarefa atualizar(@PathVariable Long id, @Valid @RequestBody Tarefa tarefa) {
        return service.atualizar(id, tarefa);
    }

    @PatchMapping("/{id}/concluir")
    public Tarefa concluir(@PathVariable Long id) {
        return service.concluir(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void excluir(@PathVariable Long id) {
        service.excluir(id);
    }
}
