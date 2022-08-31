import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EuniteFormService } from './eunite-form.service';
import { EuniteService } from '../service/eunite.service';
import { IEunite } from '../eunite.model';

import { EuniteUpdateComponent } from './eunite-update.component';

describe('Eunite Management Update Component', () => {
  let comp: EuniteUpdateComponent;
  let fixture: ComponentFixture<EuniteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let euniteFormService: EuniteFormService;
  let euniteService: EuniteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EuniteUpdateComponent],
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
      .overrideTemplate(EuniteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EuniteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    euniteFormService = TestBed.inject(EuniteFormService);
    euniteService = TestBed.inject(EuniteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Eunite query and add missing value', () => {
      const eunite: IEunite = { id: 456 };
      const unitebase: IEunite = { id: 38429 };
      eunite.unitebase = unitebase;

      const euniteCollection: IEunite[] = [{ id: 69175 }];
      jest.spyOn(euniteService, 'query').mockReturnValue(of(new HttpResponse({ body: euniteCollection })));
      const additionalEunites = [unitebase];
      const expectedCollection: IEunite[] = [...additionalEunites, ...euniteCollection];
      jest.spyOn(euniteService, 'addEuniteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eunite });
      comp.ngOnInit();

      expect(euniteService.query).toHaveBeenCalled();
      expect(euniteService.addEuniteToCollectionIfMissing).toHaveBeenCalledWith(
        euniteCollection,
        ...additionalEunites.map(expect.objectContaining)
      );
      expect(comp.eunitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const eunite: IEunite = { id: 456 };
      const unitebase: IEunite = { id: 67962 };
      eunite.unitebase = unitebase;

      activatedRoute.data = of({ eunite });
      comp.ngOnInit();

      expect(comp.eunitesSharedCollection).toContain(unitebase);
      expect(comp.eunite).toEqual(eunite);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEunite>>();
      const eunite = { id: 123 };
      jest.spyOn(euniteFormService, 'getEunite').mockReturnValue(eunite);
      jest.spyOn(euniteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eunite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eunite }));
      saveSubject.complete();

      // THEN
      expect(euniteFormService.getEunite).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(euniteService.update).toHaveBeenCalledWith(expect.objectContaining(eunite));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEunite>>();
      const eunite = { id: 123 };
      jest.spyOn(euniteFormService, 'getEunite').mockReturnValue({ id: null });
      jest.spyOn(euniteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eunite: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eunite }));
      saveSubject.complete();

      // THEN
      expect(euniteFormService.getEunite).toHaveBeenCalled();
      expect(euniteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEunite>>();
      const eunite = { id: 123 };
      jest.spyOn(euniteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eunite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(euniteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEunite', () => {
      it('Should forward to euniteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(euniteService, 'compareEunite');
        comp.compareEunite(entity, entity2);
        expect(euniteService.compareEunite).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
