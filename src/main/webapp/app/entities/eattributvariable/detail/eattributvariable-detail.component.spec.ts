import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EattributvariableDetailComponent } from './eattributvariable-detail.component';

describe('Eattributvariable Management Detail Component', () => {
  let comp: EattributvariableDetailComponent;
  let fixture: ComponentFixture<EattributvariableDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EattributvariableDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ eattributvariable: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EattributvariableDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EattributvariableDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load eattributvariable on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.eattributvariable).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
