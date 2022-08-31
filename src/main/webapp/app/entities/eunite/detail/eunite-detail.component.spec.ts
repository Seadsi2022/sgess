import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EuniteDetailComponent } from './eunite-detail.component';

describe('Eunite Management Detail Component', () => {
  let comp: EuniteDetailComponent;
  let fixture: ComponentFixture<EuniteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EuniteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ eunite: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EuniteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EuniteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load eunite on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.eunite).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
