package br.edu.tarefas;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Tarefa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    private String descricao;

    @NotNull(message = "A prioridade é obrigatória")
    @Min(value = 1, message = "Prioridade mínima é 1")
    @Max(value = 5, message = "Prioridade máxima é 5")
    private Integer prioridade;

    private boolean concluida;

    public Tarefa() {
    }

    public Tarefa(String titulo, String descricao, Integer prioridade) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Integer getPrioridade() { return prioridade; }
    public void setPrioridade(Integer prioridade) { this.prioridade = prioridade; }
    public boolean isConcluida() { return concluida; }
    public void setConcluida(boolean concluida) { this.concluida = concluida; }
}
