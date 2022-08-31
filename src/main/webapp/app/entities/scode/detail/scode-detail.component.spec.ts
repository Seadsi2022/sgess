import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ScodeDetailComponent } from './scode-detail.component';

describe('Scode Management Detail Component', () => {
  let comp: ScodeDetailComponent;
  let fixture: ComponentFixture<ScodeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ScodeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ scode: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ScodeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ScodeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load scode on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.scode).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
