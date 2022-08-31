import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EgroupevariableDetailComponent } from './egroupevariable-detail.component';

describe('Egroupevariable Management Detail Component', () => {
  let comp: EgroupevariableDetailComponent;
  let fixture: ComponentFixture<EgroupevariableDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EgroupevariableDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ egroupevariable: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EgroupevariableDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EgroupevariableDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load egroupevariable on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.egroupevariable).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
