import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EvaleurattributDetailComponent } from './evaleurattribut-detail.component';

describe('Evaleurattribut Management Detail Component', () => {
  let comp: EvaleurattributDetailComponent;
  let fixture: ComponentFixture<EvaleurattributDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EvaleurattributDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ evaleurattribut: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EvaleurattributDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EvaleurattributDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load evaleurattribut on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.evaleurattribut).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
