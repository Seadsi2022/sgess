import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EcampagneFormService } from './ecampagne-form.service';
import { EcampagneService } from '../service/ecampagne.service';
import { IEcampagne } from '../ecampagne.model';

import { EcampagneUpdateComponent } from './ecampagne-update.component';

describe('Ecampagne Management Update Component', () => {
  let comp: EcampagneUpdateComponent;
  let fixture: ComponentFixture<EcampagneUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let ecampagneFormService: EcampagneFormService;
  let ecampagneService: EcampagneService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EcampagneUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(EcampagneUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EcampagneUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    ecampagneFormService = TestBed.inject(EcampagneFormService);
    ecampagneService = TestBed.inject(EcampagneService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const ecampagne: IEcampagne = { id: 456 };

      activatedRoute.data = of({ ecampagne });
      comp.ngOnInit();

      expect(comp.ecampagne).toEqual(ecampagne);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEcampagne>>();
      const ecampagne = { id: 123 };
      jest.spyOn(ecampagneFormService, 'getEcampagne').mockReturnValue(ecampagne);
      jest.spyOn(ecampagneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ecampagne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ecampagne }));
      saveSubject.complete();

      // THEN
      expect(ecampagneFormService.getEcampagne).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(ecampagneService.update).toHaveBeenCalledWith(expect.objectContaining(ecampagne));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEcampagne>>();
      const ecampagne = { id: 123 };
      jest.spyOn(ecampagneFormService, 'getEcampagne').mockReturnValue({ id: null });
      jest.spyOn(ecampagneService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ecampagne: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: ecampagne }));
      saveSubject.complete();

      // THEN
      expect(ecampagneFormService.getEcampagne).toHaveBeenCalled();
      expect(ecampagneService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEcampagne>>();
      const ecampagne = { id: 123 };
      jest.spyOn(ecampagneService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ ecampagne });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(ecampagneService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
