import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ActaArbitroComponent } from './acta-arbitro.component';

describe('ActaArbitroComponent', () => {
  let component: ActaArbitroComponent;
  let fixture: ComponentFixture<ActaArbitroComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ActaArbitroComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(ActaArbitroComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
