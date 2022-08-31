import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EeventualiteDetailComponent } from './eeventualite-detail.component';

describe('Eeventualite Management Detail Component', () => {
  let comp: EeventualiteDetailComponent;
  let fixture: ComponentFixture<EeventualiteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EeventualiteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ eeventualite: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EeventualiteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EeventualiteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load eeventualite on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.eeventualite).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
