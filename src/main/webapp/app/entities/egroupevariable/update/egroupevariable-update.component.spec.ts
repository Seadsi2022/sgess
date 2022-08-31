import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EgroupevariableFormService } from './egroupevariable-form.service';
import { EgroupevariableService } from '../service/egroupevariable.service';
import { IEgroupevariable } from '../egroupevariable.model';
import { IEvariable } from 'app/entities/evariable/evariable.model';
import { EvariableService } from 'app/entities/evariable/service/evariable.service';
import { IEgroupe } from 'app/entities/egroupe/egroupe.model';
import { EgroupeService } from 'app/entities/egroupe/service/egroupe.service';

import { EgroupevariableUpdateComponent } from './egroupevariable-update.component';

describe('Egroupevariable Management Update Component', () => {
  let comp: EgroupevariableUpdateComponent;
  let fixture: ComponentFixture<EgroupevariableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let egroupevariableFormService: EgroupevariableFormService;
  let egroupevariableService: EgroupevariableService;
  let evariableService: EvariableService;
  let egroupeService: EgroupeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EgroupevariableUpdateComponent],
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
      .overrideTemplate(EgroupevariableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EgroupevariableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    egroupevariableFormService = TestBed.inject(EgroupevariableFormService);
    egroupevariableService = TestBed.inject(EgroupevariableService);
    evariableService = TestBed.inject(EvariableService);
    egroupeService = TestBed.inject(EgroupeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Egroupevariable query and add missing value', () => {
      const egroupevariable: IEgroupevariable = { id: 456 };
      const suivant: IEgroupevariable = { id: 85911 };
      egroupevariable.suivant = suivant;

      const egroupevariableCollection: IEgroupevariable[] = [{ id: 36477 }];
      jest.spyOn(egroupevariableService, 'query').mockReturnValue(of(new HttpResponse({ body: egroupevariableCollection })));
      const additionalEgroupevariables = [suivant];
      const expectedCollection: IEgroupevariable[] = [...additionalEgroupevariables, ...egroupevariableCollection];
      jest.spyOn(egroupevariableService, 'addEgroupevariableToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ egroupevariable });
      comp.ngOnInit();

      expect(egroupevariableService.query).toHaveBeenCalled();
      expect(egroupevariableService.addEgroupevariableToCollectionIfMissing).toHaveBeenCalledWith(
        egroupevariableCollection,
        ...additionalEgroupevariables.map(expect.objectContaining)
      );
      expect(comp.egroupevariablesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Evariable query and add missing value', () => {
      const egroupevariable: IEgroupevariable = { id: 456 };
      const evariable: IEvariable = { id: 66286 };
      egroupevariable.evariable = evariable;

      const evariableCollection: IEvariable[] = [{ id: 55847 }];
      jest.spyOn(evariableService, 'query').mockReturnValue(of(new HttpResponse({ body: evariableCollection })));
      const additionalEvariables = [evariable];
      const expectedCollection: IEvariable[] = [...additionalEvariables, ...evariableCollection];
      jest.spyOn(evariableService, 'addEvariableToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ egroupevariable });
      comp.ngOnInit();

      expect(evariableService.query).toHaveBeenCalled();
      expect(evariableService.addEvariableToCollectionIfMissing).toHaveBeenCalledWith(
        evariableCollection,
        ...additionalEvariables.map(expect.objectContaining)
      );
      expect(comp.evariablesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Egroupe query and add missing value', () => {
      const egroupevariable: IEgroupevariable = { id: 456 };
      const egroupe: IEgroupe = { id: 89277 };
      egroupevariable.egroupe = egroupe;

      const egroupeCollection: IEgroupe[] = [{ id: 62805 }];
      jest.spyOn(egroupeService, 'query').mockReturnValue(of(new HttpResponse({ body: egroupeCollection })));
      const additionalEgroupes = [egroupe];
      const expectedCollection: IEgroupe[] = [...additionalEgroupes, ...egroupeCollection];
      jest.spyOn(egroupeService, 'addEgroupeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ egroupevariable });
      comp.ngOnInit();

      expect(egroupeService.query).toHaveBeenCalled();
      expect(egroupeService.addEgroupeToCollectionIfMissing).toHaveBeenCalledWith(
        egroupeCollection,
        ...additionalEgroupes.map(expect.objectContaining)
      );
      expect(comp.egroupesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const egroupevariable: IEgroupevariable = { id: 456 };
      const suivant: IEgroupevariable = { id: 92395 };
      egroupevariable.suivant = suivant;
      const evariable: IEvariable = { id: 82478 };
      egroupevariable.evariable = evariable;
      const egroupe: IEgroupe = { id: 66487 };
      egroupevariable.egroupe = egroupe;

      activatedRoute.data = of({ egroupevariable });
      comp.ngOnInit();

      expect(comp.egroupevariablesSharedCollection).toContain(suivant);
      expect(comp.evariablesSharedCollection).toContain(evariable);
      expect(comp.egroupesSharedCollection).toContain(egroupe);
      expect(comp.egroupevariable).toEqual(egroupevariable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEgroupevariable>>();
      const egroupevariable = { id: 123 };
      jest.spyOn(egroupevariableFormService, 'getEgroupevariable').mockReturnValue(egroupevariable);
      jest.spyOn(egroupevariableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egroupevariable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: egroupevariable }));
      saveSubject.complete();

      // THEN
      expect(egroupevariableFormService.getEgroupevariable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(egroupevariableService.update).toHaveBeenCalledWith(expect.objectContaining(egroupevariable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEgroupevariable>>();
      const egroupevariable = { id: 123 };
      jest.spyOn(egroupevariableFormService, 'getEgroupevariable').mockReturnValue({ id: null });
      jest.spyOn(egroupevariableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egroupevariable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: egroupevariable }));
      saveSubject.complete();

      // THEN
      expect(egroupevariableFormService.getEgroupevariable).toHaveBeenCalled();
      expect(egroupevariableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEgroupevariable>>();
      const egroupevariable = { id: 123 };
      jest.spyOn(egroupevariableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egroupevariable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(egroupevariableService.update).toHaveBeenCalled();
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

    describe('compareEvariable', () => {
      it('Should forward to evariableService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(evariableService, 'compareEvariable');
        comp.compareEvariable(entity, entity2);
        expect(evariableService.compareEvariable).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEgroupe', () => {
      it('Should forward to egroupeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(egroupeService, 'compareEgroupe');
        comp.compareEgroupe(entity, entity2);
        expect(egroupeService.compareEgroupe).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
