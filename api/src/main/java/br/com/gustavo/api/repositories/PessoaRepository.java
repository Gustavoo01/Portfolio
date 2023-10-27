package br.com.gustavo.api.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import br.com.gustavo.api.model.Pessoa;

public interface PessoaRepository extends CrudRepository<Pessoa, Long>
{
    List<Pessoa> findAll();
}