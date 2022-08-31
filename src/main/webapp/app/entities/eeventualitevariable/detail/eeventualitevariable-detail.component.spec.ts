import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EeventualitevariableDetailComponent } from './eeventualitevariable-detail.component';

describe('Eeventualitevariable Management Detail Component', () => {
  let comp: EeventualitevariableDetailComponent;
  let fixture: ComponentFixture<EeventualitevariableDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EeventualitevariableDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ eeventualitevariable: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EeventualitevariableDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EeventualitevariableDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load eeventualitevariable on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.eeventualitevariable).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
