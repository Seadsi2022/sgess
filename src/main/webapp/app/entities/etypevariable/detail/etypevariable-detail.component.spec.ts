import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EtypevariableDetailComponent } from './etypevariable-detail.component';

describe('Etypevariable Management Detail Component', () => {
  let comp: EtypevariableDetailComponent;
  let fixture: ComponentFixture<EtypevariableDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EtypevariableDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ etypevariable: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EtypevariableDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EtypevariableDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load etypevariable on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.etypevariable).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
