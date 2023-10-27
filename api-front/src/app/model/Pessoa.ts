import { Contato } from "./Contato";

export class Pessoa{
    id: number = 0;
    nome: string = '';
    cpf: string = '';
    dataNascimento: string = '';
    contatos: Contato[] = [];

    adicionarContato() {
        const novoContato = new Contato();
        novoContato.nome = this.nome; // Copia o nome da pessoa para o novo contato
        novoContato.telefone = ''; // Limpa os outros campos do novo contato, se necessário
        novoContato.email = ''; // Limpa os outros campos do novo contato, se necessário
        this.contatos.push(novoContato);
      }

    removerContato(index: number) {
        this.contatos.splice(index, 1);
    }    
}

