import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EvariableDetailComponent } from './evariable-detail.component';

describe('Evariable Management Detail Component', () => {
  let comp: EvariableDetailComponent;
  let fixture: ComponentFixture<EvariableDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EvariableDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ evariable: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EvariableDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EvariableDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load evariable on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.evariable).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
