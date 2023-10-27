package br.com.gustavo.api;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import br.com.gustavo.api.model.Contato;
import br.com.gustavo.api.model.Pessoa;
import br.com.gustavo.api.repositories.PessoaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.gustavo.api.services.PessoaService;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class PessoaServiceTests
{
    @InjectMocks
    private PessoaService pessoaService;

    @Mock
    private PessoaRepository pessoaRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
    @Test
    public void testEmailValido()
    {
        assertTrue(PessoaService.verificaEmail("email@email.com"));        
        assertTrue(PessoaService.verificaEmail("email123@email.com.br"));
        assertTrue(PessoaService.verificaEmail("email.email@email.co"));
    }

    @Test
    public void testEmailInvalido()
    {
        assertFalse(PessoaService.verificaEmail("email"));
        assertFalse(PessoaService.verificaEmail("email@.com")); 
        assertFalse(PessoaService.verificaEmail("email@.sadsad"));
        assertFalse(PessoaService.verificaEmail("email@sadsad."));
        assertFalse(PessoaService.verificaEmail("@email.com"));
    }

    @Test
    public void testEditarPessoaExistente()
    {
        Long id = 1L;

        Pessoa pessoaExistente = new Pessoa();
        pessoaExistente.setId(id);

        Pessoa pessoaAtualizada = new Pessoa();
        pessoaAtualizada.setId(id);
        pessoaAtualizada.setNome("Nome Atualizado");
        pessoaAtualizada.setCpf("12345678901");
        pessoaAtualizada.setDataNascimento(LocalDate.parse("2000-01-01"));

        when(pessoaRepository.findById(id)).thenReturn(Optional.of(pessoaExistente));
        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoaAtualizada);

        ResponseEntity<?> response = pessoaService.editarPessoa(pessoaAtualizada, id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(pessoaAtualizada, response.getBody());
    }

    @Test
    public void testEditarPessoaNaoExistente()
    {
        Long id = 1L;

        when(pessoaRepository.findById(id)).thenReturn(Optional.empty());

        ResponseEntity<?> response = pessoaService.editarPessoa(new Pessoa(), id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    public void testCadastrarPessoaValida()
    {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Nome");
        pessoa.setCpf("268.690.520-42");
        pessoa.setDataNascimento(LocalDate.parse("1990-01-01"));
        Contato contato = new Contato();
        contato.setNome("Contato");
        contato.setTelefone("123456789");
        contato.setEmail("email@example.com");
        pessoa.setContatos(new ArrayList<>(Collections.singletonList(contato)));

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        ResponseEntity<?> response = pessoaService.cadastrar(pessoa);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(pessoa, response.getBody());
    }

    @Test
    public void testCadastrarPessoaCamposObrigatoriosNulos()
    {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome(null);
        pessoa.setCpf(null);
        pessoa.setDataNascimento(null);
        pessoa.setContatos(null);

        ResponseEntity<?> response = pessoaService.cadastrar(pessoa);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos os campos obrigatórios devem ser preenchidos.", response.getBody());
    }

    @Test
    public void testCadastrarPessoaCpfInvalido()
    {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Nome");
        pessoa.setCpf("12345678910");
        pessoa.setDataNascimento(LocalDate.parse("1990-01-01"));
        Contato contato = new Contato();
        contato.setNome("Contato");
        contato.setTelefone("123456789");
        contato.setEmail("email@example.com");
        pessoa.setContatos(new ArrayList<>(Collections.singletonList(contato)));

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        ResponseEntity<?> response = pessoaService.cadastrar(pessoa);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("CPF inválido.", response.getBody());
    }

    @Test
    public void testCadastrarPessoaDataNascimentoPosterior()
    {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Nome");
        pessoa.setCpf("268.690.520-42");
        pessoa.setDataNascimento(LocalDate.parse("2050-01-01"));
        Contato contato = new Contato();
        contato.setNome("Contato");
        contato.setTelefone("123456789");
        contato.setEmail("email@example.com");
        pessoa.setContatos(new ArrayList<>(Collections.singletonList(contato)));

        when(pessoaRepository.save(any(Pessoa.class))).thenReturn(pessoa);

        ResponseEntity<?> response = pessoaService.cadastrar(pessoa);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Data de nascimento não pode ser uma data futura.", response.getBody());
    }

    @Test
    public void testCadastrarPessoaCampoContatosNulo()
    {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Pessoa");
        pessoa.setCpf("268.690.520-42");
        pessoa.setDataNascimento(LocalDate.parse("1990-01-01"));
        Contato contato = new Contato();
        contato.setNome("");
        contato.setTelefone("");
        contato.setEmail("email@example.com");
        pessoa.setContatos(new ArrayList<>(Collections.singletonList(contato)));

        ResponseEntity<?> response = pessoaService.cadastrar(pessoa);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos os campos de contato são obrigatórios.", response.getBody());
    }

    @Test
    public void testCadastrarPessoaSemContato()
    {
        Pessoa pessoa = new Pessoa();
        pessoa.setNome("Pessoa");
        pessoa.setCpf("268.690.520-42");
        pessoa.setDataNascimento(LocalDate.parse("1990-01-01"));
        pessoa.setContatos(null);

        ResponseEntity<?> response = pessoaService.cadastrar(pessoa);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Todos os campos obrigatórios devem ser preenchidos.", response.getBody());
    }
}