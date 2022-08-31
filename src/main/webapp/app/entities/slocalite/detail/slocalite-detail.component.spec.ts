import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SlocaliteDetailComponent } from './slocalite-detail.component';

describe('Slocalite Management Detail Component', () => {
  let comp: SlocaliteDetailComponent;
  let fixture: ComponentFixture<SlocaliteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SlocaliteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ slocalite: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SlocaliteDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SlocaliteDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load slocalite on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.slocalite).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
