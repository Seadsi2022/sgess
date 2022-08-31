import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SstructureDetailComponent } from './sstructure-detail.component';

describe('Sstructure Management Detail Component', () => {
  let comp: SstructureDetailComponent;
  let fixture: ComponentFixture<SstructureDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SstructureDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ sstructure: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SstructureDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SstructureDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load sstructure on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.sstructure).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
