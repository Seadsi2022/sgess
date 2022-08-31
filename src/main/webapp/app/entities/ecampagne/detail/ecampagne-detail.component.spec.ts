import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EcampagneDetailComponent } from './ecampagne-detail.component';

describe('Ecampagne Management Detail Component', () => {
  let comp: EcampagneDetailComponent;
  let fixture: ComponentFixture<EcampagneDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [EcampagneDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ ecampagne: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(EcampagneDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(EcampagneDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load ecampagne on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.ecampagne).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
