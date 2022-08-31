import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EquestionnaireDetailComponent } from './equestionnaire-detail.component';

describe('Equestionnaire Management Detail Component', () => {
  let comp: EquestionnaireDetailComponent;
  let fixture: ComponentFixture<EquestionnaireDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EquestionnaireDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ equestionnaire: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EquestionnaireDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EquestionnaireDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load equestionnaire on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.equestionnaire).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
