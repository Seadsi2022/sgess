import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EvaleurvariableFormService } from './evaleurvariable-form.service';
import { EvaleurvariableService } from '../service/evaleurvariable.service';
import { IEvaleurvariable } from '../evaleurvariable.model';
import { IEgroupevariable } from 'app/entities/egroupevariable/egroupevariable.model';
import { EgroupevariableService } from 'app/entities/egroupevariable/service/egroupevariable.service';
import { ISstructure } from 'app/entities/sstructure/sstructure.model';
import { SstructureService } from 'app/entities/sstructure/service/sstructure.service';

import { EvaleurvariableUpdateComponent } from './evaleurvariable-update.component';

describe('Evaleurvariable Management Update Component', () => {
  let comp: EvaleurvariableUpdateComponent;
  let fixture: ComponentFixture<EvaleurvariableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let evaleurvariableFormService: EvaleurvariableFormService;
  let evaleurvariableService: EvaleurvariableService;
  let egroupevariableService: EgroupevariableService;
  let sstructureService: SstructureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EvaleurvariableUpdateComponent],
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
      .overrideTemplate(EvaleurvariableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EvaleurvariableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    evaleurvariableFormService = TestBed.inject(EvaleurvariableFormService);
    evaleurvariableService = TestBed.inject(EvaleurvariableService);
    egroupevariableService = TestBed.inject(EgroupevariableService);
    sstructureService = TestBed.inject(SstructureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Egroupevariable query and add missing value', () => {
      const evaleurvariable: IEvaleurvariable = { id: 456 };
      const egroupevariable: IEgroupevariable = { id: 38120 };
      evaleurvariable.egroupevariable = egroupevariable;

      const egroupevariableCollection: IEgroupevariable[] = [{ id: 15001 }];
      jest.spyOn(egroupevariableService, 'query').mockReturnValue(of(new HttpResponse({ body: egroupevariableCollection })));
      const additionalEgroupevariables = [egroupevariable];
      const expectedCollection: IEgroupevariable[] = [...additionalEgroupevariables, ...egroupevariableCollection];
      jest.spyOn(egroupevariableService, 'addEgroupevariableToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaleurvariable });
      comp.ngOnInit();

      expect(egroupevariableService.query).toHaveBeenCalled();
      expect(egroupevariableService.addEgroupevariableToCollectionIfMissing).toHaveBeenCalledWith(
        egroupevariableCollection,
        ...additionalEgroupevariables.map(expect.objectContaining)
      );
      expect(comp.egroupevariablesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Sstructure query and add missing value', () => {
      const evaleurvariable: IEvaleurvariable = { id: 456 };
      const sstructure: ISstructure = { id: 72447 };
      evaleurvariable.sstructure = sstructure;

      const sstructureCollection: ISstructure[] = [{ id: 93967 }];
      jest.spyOn(sstructureService, 'query').mockReturnValue(of(new HttpResponse({ body: sstructureCollection })));
      const additionalSstructures = [sstructure];
      const expectedCollection: ISstructure[] = [...additionalSstructures, ...sstructureCollection];
      jest.spyOn(sstructureService, 'addSstructureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evaleurvariable });
      comp.ngOnInit();

      expect(sstructureService.query).toHaveBeenCalled();
      expect(sstructureService.addSstructureToCollectionIfMissing).toHaveBeenCalledWith(
        sstructureCollection,
        ...additionalSstructures.map(expect.objectContaining)
      );
      expect(comp.sstructuresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const evaleurvariable: IEvaleurvariable = { id: 456 };
      const egroupevariable: IEgroupevariable = { id: 50363 };
      evaleurvariable.egroupevariable = egroupevariable;
      const sstructure: ISstructure = { id: 38049 };
      evaleurvariable.sstructure = sstructure;

      activatedRoute.data = of({ evaleurvariable });
      comp.ngOnInit();

      expect(comp.egroupevariablesSharedCollection).toContain(egroupevariable);
      expect(comp.sstructuresSharedCollection).toContain(sstructure);
      expect(comp.evaleurvariable).toEqual(evaleurvariable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaleurvariable>>();
      const evaleurvariable = { id: 123 };
      jest.spyOn(evaleurvariableFormService, 'getEvaleurvariable').mockReturnValue(evaleurvariable);
      jest.spyOn(evaleurvariableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaleurvariable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evaleurvariable }));
      saveSubject.complete();

      // THEN
      expect(evaleurvariableFormService.getEvaleurvariable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(evaleurvariableService.update).toHaveBeenCalledWith(expect.objectContaining(evaleurvariable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaleurvariable>>();
      const evaleurvariable = { id: 123 };
      jest.spyOn(evaleurvariableFormService, 'getEvaleurvariable').mockReturnValue({ id: null });
      jest.spyOn(evaleurvariableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaleurvariable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evaleurvariable }));
      saveSubject.complete();

      // THEN
      expect(evaleurvariableFormService.getEvaleurvariable).toHaveBeenCalled();
      expect(evaleurvariableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaleurvariable>>();
      const evaleurvariable = { id: 123 };
      jest.spyOn(evaleurvariableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaleurvariable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(evaleurvariableService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEgroupevariable', () => {
      it('Should forward to egroupevariableService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(egroupevariableService, 'compareEgroupevariable');
        comp.compareEgroupevariable(entity, entity2);
        expect(egroupevariableService.compareEgroupevariable).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareSstructure', () => {
      it('Should forward to sstructureService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sstructureService, 'compareSstructure');
        comp.compareSstructure(entity, entity2);
        expect(sstructureService.compareSstructure).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
