import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FichaPartidoComponent } from './ficha-partido.component';

describe('FichaPartidoComponent', () => {
  let component: FichaPartidoComponent;
  let fixture: ComponentFixture<FichaPartidoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FichaPartidoComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(FichaPartidoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
