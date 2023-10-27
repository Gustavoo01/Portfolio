import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CadastroContatoComponent } from './cadastro-contato.component';

describe('CadastroContatoComponent', () => {
  let component: CadastroContatoComponent;
  let fixture: ComponentFixture<CadastroContatoComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [CadastroContatoComponent]
    });
    fixture = TestBed.createComponent(CadastroContatoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
