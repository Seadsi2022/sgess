import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EattributDetailComponent } from './eattribut-detail.component';

describe('Eattribut Management Detail Component', () => {
  let comp: EattributDetailComponent;
  let fixture: ComponentFixture<EattributDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EattributDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ eattribut: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EattributDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EattributDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load eattribut on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.eattribut).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
