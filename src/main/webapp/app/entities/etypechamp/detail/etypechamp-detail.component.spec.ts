import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EtypechampDetailComponent } from './etypechamp-detail.component';

describe('Etypechamp Management Detail Component', () => {
  let comp: EtypechampDetailComponent;
  let fixture: ComponentFixture<EtypechampDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EtypechampDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ etypechamp: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EtypechampDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EtypechampDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load etypechamp on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.etypechamp).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
