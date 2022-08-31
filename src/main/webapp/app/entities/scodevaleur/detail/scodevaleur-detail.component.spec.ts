import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ScodevaleurDetailComponent } from './scodevaleur-detail.component';

describe('Scodevaleur Management Detail Component', () => {
  let comp: ScodevaleurDetailComponent;
  let fixture: ComponentFixture<ScodevaleurDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ScodevaleurDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ scodevaleur: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ScodevaleurDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ScodevaleurDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load scodevaleur on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.scodevaleur).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
