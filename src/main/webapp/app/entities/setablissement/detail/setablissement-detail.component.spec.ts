import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { SetablissementDetailComponent } from './setablissement-detail.component';

describe('Setablissement Management Detail Component', () => {
  let comp: SetablissementDetailComponent;
  let fixture: ComponentFixture<SetablissementDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [SetablissementDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ setablissement: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(SetablissementDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(SetablissementDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load setablissement on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.setablissement).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
