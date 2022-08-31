import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EvaleurvariableDetailComponent } from './evaleurvariable-detail.component';

describe('Evaleurvariable Management Detail Component', () => {
  let comp: EvaleurvariableDetailComponent;
  let fixture: ComponentFixture<EvaleurvariableDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EvaleurvariableDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ evaleurvariable: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EvaleurvariableDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EvaleurvariableDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load evaleurvariable on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.evaleurvariable).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
