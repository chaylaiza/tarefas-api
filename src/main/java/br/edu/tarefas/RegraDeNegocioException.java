package br.edu.tarefas;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
