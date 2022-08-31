import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EeventualiteFormService } from './eeventualite-form.service';
import { EeventualiteService } from '../service/eeventualite.service';
import { IEeventualite } from '../eeventualite.model';

import { EeventualiteUpdateComponent } from './eeventualite-update.component';

describe('Eeventualite Management Update Component', () => {
  let comp: EeventualiteUpdateComponent;
  let fixture: ComponentFixture<EeventualiteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eeventualiteFormService: EeventualiteFormService;
  let eeventualiteService: EeventualiteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EeventualiteUpdateComponent],
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
      .overrideTemplate(EeventualiteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EeventualiteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eeventualiteFormService = TestBed.inject(EeventualiteFormService);
    eeventualiteService = TestBed.inject(EeventualiteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const eeventualite: IEeventualite = { id: 456 };

      activatedRoute.data = of({ eeventualite });
      comp.ngOnInit();

      expect(comp.eeventualite).toEqual(eeventualite);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEeventualite>>();
      const eeventualite = { id: 123 };
      jest.spyOn(eeventualiteFormService, 'getEeventualite').mockReturnValue(eeventualite);
      jest.spyOn(eeventualiteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eeventualite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eeventualite }));
      saveSubject.complete();

      // THEN
      expect(eeventualiteFormService.getEeventualite).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(eeventualiteService.update).toHaveBeenCalledWith(expect.objectContaining(eeventualite));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEeventualite>>();
      const eeventualite = { id: 123 };
      jest.spyOn(eeventualiteFormService, 'getEeventualite').mockReturnValue({ id: null });
      jest.spyOn(eeventualiteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eeventualite: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eeventualite }));
      saveSubject.complete();

      // THEN
      expect(eeventualiteFormService.getEeventualite).toHaveBeenCalled();
      expect(eeventualiteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEeventualite>>();
      const eeventualite = { id: 123 };
      jest.spyOn(eeventualiteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eeventualite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eeventualiteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
