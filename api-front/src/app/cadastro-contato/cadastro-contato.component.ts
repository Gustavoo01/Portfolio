import { Component, EventEmitter, Input, Output } from '@angular/core';
import { Pessoa } from '../model/Pessoa';
import { Contato } from '../model/Contato';

@Component({
  selector: 'app-cadastro-contato',
  templateUrl: './cadastro-contato.component.html',
  styleUrls: ['./cadastro-contato.component.css']
})
export class CadastroContatoComponent {
  pessoa = new Pessoa();
  contatos: Contato[] = [];
  @Input() i!: number;
  @Input() contato: any;
  @Output() removerContatoEvent = new EventEmitter<number>();
  
  adicionarContato() {
    this.pessoa.contatos.push(this.contato);
    this.contato = new Contato();
  }

  removerContato(index: number) {
    this.removerContatoEvent.emit(index);
  }
}