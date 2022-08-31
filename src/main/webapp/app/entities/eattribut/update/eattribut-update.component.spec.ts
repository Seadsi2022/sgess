import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EattributFormService } from './eattribut-form.service';
import { EattributService } from '../service/eattribut.service';
import { IEattribut } from '../eattribut.model';
import { IEvaleurattribut } from 'app/entities/evaleurattribut/evaleurattribut.model';
import { EvaleurattributService } from 'app/entities/evaleurattribut/service/evaleurattribut.service';

import { EattributUpdateComponent } from './eattribut-update.component';

describe('Eattribut Management Update Component', () => {
  let comp: EattributUpdateComponent;
  let fixture: ComponentFixture<EattributUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eattributFormService: EattributFormService;
  let eattributService: EattributService;
  let evaleurattributService: EvaleurattributService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EattributUpdateComponent],
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
      .overrideTemplate(EattributUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EattributUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eattributFormService = TestBed.inject(EattributFormService);
    eattributService = TestBed.inject(EattributService);
    evaleurattributService = TestBed.inject(EvaleurattributService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Evaleurattribut query and add missing value', () => {
      const eattribut: IEattribut = { id: 456 };
      const evaleurattribut: IEvaleurattribut = { id: 49162 };
      eattribut.evaleurattribut = evaleurattribut;

      const evaleurattributCollection: IEvaleurattribut[] = [{ id: 48639 }];
      jest.spyOn(evaleurattributService, 'query').mockReturnValue(of(new HttpResponse({ body: evaleurattributCollection })));
      const additionalEvaleurattributs = [evaleurattribut];
      const expectedCollection: IEvaleurattribut[] = [...additionalEvaleurattributs, ...evaleurattributCollection];
      jest.spyOn(evaleurattributService, 'addEvaleurattributToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eattribut });
      comp.ngOnInit();

      expect(evaleurattributService.query).toHaveBeenCalled();
      expect(evaleurattributService.addEvaleurattributToCollectionIfMissing).toHaveBeenCalledWith(
        evaleurattributCollection,
        ...additionalEvaleurattributs.map(expect.objectContaining)
      );
      expect(comp.evaleurattributsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const eattribut: IEattribut = { id: 456 };
      const evaleurattribut: IEvaleurattribut = { id: 90722 };
      eattribut.evaleurattribut = evaleurattribut;

      activatedRoute.data = of({ eattribut });
      comp.ngOnInit();

      expect(comp.evaleurattributsSharedCollection).toContain(evaleurattribut);
      expect(comp.eattribut).toEqual(eattribut);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEattribut>>();
      const eattribut = { id: 123 };
      jest.spyOn(eattributFormService, 'getEattribut').mockReturnValue(eattribut);
      jest.spyOn(eattributService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eattribut });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eattribut }));
      saveSubject.complete();

      // THEN
      expect(eattributFormService.getEattribut).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(eattributService.update).toHaveBeenCalledWith(expect.objectContaining(eattribut));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEattribut>>();
      const eattribut = { id: 123 };
      jest.spyOn(eattributFormService, 'getEattribut').mockReturnValue({ id: null });
      jest.spyOn(eattributService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eattribut: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eattribut }));
      saveSubject.complete();

      // THEN
      expect(eattributFormService.getEattribut).toHaveBeenCalled();
      expect(eattributService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEattribut>>();
      const eattribut = { id: 123 };
      jest.spyOn(eattributService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eattribut });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eattributService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEvaleurattribut', () => {
      it('Should forward to evaleurattributService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(evaleurattributService, 'compareEvaleurattribut');
        comp.compareEvaleurattribut(entity, entity2);
        expect(evaleurattributService.compareEvaleurattribut).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
