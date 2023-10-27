package br.com.gustavo.api.services;

import org.springframework.stereotype.Service;

import br.com.gustavo.api.model.Contato;
import br.com.gustavo.api.repositories.ContatoRepository;

@Service
public class ContatoService 
{
    private final ContatoRepository contatoRepository;

    public ContatoService(ContatoRepository contatoRepository) 
    {
        this.contatoRepository = contatoRepository;
    }
}