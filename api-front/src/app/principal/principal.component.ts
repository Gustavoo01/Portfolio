import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Pessoa } from '../model/Pessoa';
import { PessoaService } from '../services/pessoa.service';
import { Contato } from '../model/Contato';
import { HttpErrorResponse } from '@angular/common/http';

@Component({
  selector: 'app-principal',
  templateUrl: './principal.component.html',
  styleUrls: ['./principal.component.css']
})
export class PrincipalComponent {
  pessoa = new Pessoa();
  btnCadastro:boolean = true;
  telaCadastro:boolean = false;
  tabela:boolean = true;
  pessoas:Pessoa[] = [];
  contato: Contato = new Contato();
  contatos: Contato[] = [];

  constructor(private servico:PessoaService){}

  selecionar():void{
    this.servico.selecionar().subscribe(retorno => this.pessoas = retorno);
  }

  adicionarContato() {
    this.pessoa.contatos.push(this.contato);
    this.contato = new Contato();
  }

  removerContato(index: number) {
    this.pessoa.contatos.splice(index, 1);
  }
  
  cadastrar():void{
    this.pessoa.cpf = this.formatarCPF(this.pessoa.cpf);
    this.servico.cadastrar(this.pessoa)
    .subscribe(retorno => {
      this.pessoas.push(retorno);
      this.pessoa = new Pessoa();
      alert('Pessoa Cadastrada com sucesso!');
    },
    (error: HttpErrorResponse) => {
      alert(error.error);
    })
  }

  selecionarPessoa(posicao:number):void
  {
    this.pessoa = this.pessoas[posicao];
    this.telaCadastro = true;
    this.btnCadastro = false;
    this.tabela = false;
  }

  editar():void{
    this.servico.editar(this.pessoa)
    .subscribe(retorno => {
      let posicao = this.pessoas.findIndex(obj => {
        return obj.id == retorno.id;
      });

      this.pessoas[posicao] = retorno;

      this.pessoa = new Pessoa();

      this.btnCadastro = true;
      this.tabela = true;

      alert('Pessoa alterada com sucesso!');
    },
    (error: HttpErrorResponse) => {
      alert(error.error);
    })
    this.telaCadastro = false;
  }

  editarPessoa() {
    this.servico.editar(this.pessoa)
    .subscribe(
      response => {
        alert('Cliente alterado!' + response);
      },
      error => {
        alert('Erro ao alterar' + error);
      }
    );
  }

  remover():void{
    this.servico.remover(this.pessoa.id)
    .subscribe(retorno => {
      let posicao = this.pessoas.findIndex(obj => {
        return obj.id == this.pessoa.id;
      });

      this.pessoas.splice(posicao, 1);

      this.pessoa = new Pessoa();

      this.btnCadastro = true;
      this.tabela = true;

      alert('Pessoa removida com sucesso!');
    })
    this.telaCadastro = false;
  }

  botaoCadastro():void{
    this.telaCadastro = true;
    this.tabela = false;
  }

  cancelar():void{
    this.pessoa = new Pessoa();
    this.btnCadastro = true;
    this.tabela = true;
    this.telaCadastro = false;
  }

  cancelarContato():void{
    this.contato = new Contato();
    this.btnCadastro = true;
    this.tabela = true;
  }

  formatarCPF(cpf: string): string {
    cpf = cpf.replace(/\D/g, '');
  
    return cpf.replace(/(\d{3})(\d{3})(\d{3})(\d{2})/, '$1.$2.$3-$4');
  }

  ngOnInit(){
    this.adicionarContato();
    this.selecionar();
  }
}