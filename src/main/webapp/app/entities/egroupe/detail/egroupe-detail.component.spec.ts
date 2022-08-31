import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EgroupeDetailComponent } from './egroupe-detail.component';

describe('Egroupe Management Detail Component', () => {
  let comp: EgroupeDetailComponent;
  let fixture: ComponentFixture<EgroupeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EgroupeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ egroupe: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EgroupeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EgroupeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load egroupe on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.egroupe).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
