package br.com.gustavo.api.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import br.com.gustavo.api.model.Contato;
import br.com.gustavo.api.model.Pessoa;
import br.com.gustavo.api.repositories.PessoaRepository;

@Service
public class PessoaService 
{
    @Autowired
    private PessoaRepository pessoaRepository;

    public ResponseEntity<?> cadastrar(Pessoa pessoa)
    {
        if (pessoa.getContatos() != null) {
            for (Contato contato : pessoa.getContatos()) {
                contato.setPessoa(pessoa);
            }
        }

        if (pessoa.getNome() == null || pessoa.getCpf() == null || pessoa.getDataNascimento() == null || pessoa.getContatos() == null) {
            return new ResponseEntity<>("Todos os campos obrigatórios devem ser preenchidos.", HttpStatus.BAD_REQUEST);
        }
    
        if (!verificaCpf(pessoa.getCpf())) {
            return new ResponseEntity<>("CPF inválido.", HttpStatus.BAD_REQUEST);
        }
    
        if (pessoa.getDataNascimento().isAfter(LocalDate.now())) {
            return new ResponseEntity<>("Data de nascimento não pode ser uma data futura.", HttpStatus.BAD_REQUEST);
        }
    
        if (pessoa.getContatos().isEmpty()) {
            return new ResponseEntity<>("A pessoa deve possuir pelo menos um contato.", HttpStatus.BAD_REQUEST);
        }
    
        for (Contato contato : pessoa.getContatos()) {
            if (contato.getNome().isEmpty() || contato.getTelefone().isEmpty() || contato.getEmail().isEmpty()) {
                return new ResponseEntity<>("Todos os campos de contato são obrigatórios.", HttpStatus.BAD_REQUEST);
            }
    
            if (!verificaEmail(contato.getEmail())) {
                return new ResponseEntity<>("E-mail inválido.", HttpStatus.BAD_REQUEST);
            }
        }

        Pessoa novaPessoa = pessoaRepository.save(pessoa);
        return new ResponseEntity<>(novaPessoa, HttpStatus.CREATED);
    }

    public ResponseEntity<?> editarPessoa(Pessoa pessoaAtualizada, Long id) 
    {
        Optional<Pessoa> pessoaExistente = pessoaRepository.findById(id);
        if (pessoaExistente.isPresent()) {
            Pessoa pessoa = pessoaExistente.get();
            pessoa.setNome(pessoaAtualizada.getNome());
            pessoa.setCpf(pessoaAtualizada.getCpf());
            pessoa.setDataNascimento(pessoaAtualizada.getDataNascimento());
            pessoa.setContatos(pessoaAtualizada.getContatos());
            Pessoa dadosPessoa = pessoaRepository.save(pessoa);
            return new ResponseEntity<>(dadosPessoa, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    public Pessoa salvar(Pessoa pessoa) 
    {
        return pessoaRepository.save(pessoa);
    }

    public Pessoa buscarPorId(Long id) 
    {
        return pessoaRepository.findById(id).orElse(null);
    }

    public List<Pessoa> listar() 
    {
        return pessoaRepository.findAll();
    }

    public static boolean verificaEmail(String email) {
        String regex = "^[A-Za-z0-9+_.-]+@([A-Za-z0-9.-]+)\\.([A-Za-z]{2,4})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(email);
        
        return matcher.matches();
    }

    public static boolean verificaCpf(String cpf) {
        cpf = cpf.replaceAll("[^0-9]", "");

        if (cpf.length() != 11) {
            return false;
        }

        boolean allDigitsEqual = true;
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != cpf.charAt(0)) {
                allDigitsEqual = false;
                break;
            }
        }
        if (allDigitsEqual) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = Character.getNumericValue(cpf.charAt(i));
            sum += digit * (10 - i);
        }
        int firstDigit = 11 - (sum % 11);

        if (firstDigit == 10 || firstDigit == 11) {
            firstDigit = 0;
        }

        if (Character.getNumericValue(cpf.charAt(9)) != firstDigit) {
            return false;
        }

        sum = 0;
        for (int i = 0; i < 10; i++) {
            int digit = Character.getNumericValue(cpf.charAt(i));
            sum += digit * (11 - i);
        }
        int secondDigit = 11 - (sum % 11);

        if (secondDigit == 10 || secondDigit == 11) {
            secondDigit = 0;
        }

        return Character.getNumericValue(cpf.charAt(10)) == secondDigit;
    }
}