import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EvariableFormService } from './evariable-form.service';
import { EvariableService } from '../service/evariable.service';
import { IEvariable } from '../evariable.model';
import { IEtypevariable } from 'app/entities/etypevariable/etypevariable.model';
import { EtypevariableService } from 'app/entities/etypevariable/service/etypevariable.service';
import { IEunite } from 'app/entities/eunite/eunite.model';
import { EuniteService } from 'app/entities/eunite/service/eunite.service';

import { EvariableUpdateComponent } from './evariable-update.component';

describe('Evariable Management Update Component', () => {
  let comp: EvariableUpdateComponent;
  let fixture: ComponentFixture<EvariableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let evariableFormService: EvariableFormService;
  let evariableService: EvariableService;
  let etypevariableService: EtypevariableService;
  let euniteService: EuniteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EvariableUpdateComponent],
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
      .overrideTemplate(EvariableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EvariableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    evariableFormService = TestBed.inject(EvariableFormService);
    evariableService = TestBed.inject(EvariableService);
    etypevariableService = TestBed.inject(EtypevariableService);
    euniteService = TestBed.inject(EuniteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Etypevariable query and add missing value', () => {
      const evariable: IEvariable = { id: 456 };
      const etypevariable: IEtypevariable = { id: 2308 };
      evariable.etypevariable = etypevariable;

      const etypevariableCollection: IEtypevariable[] = [{ id: 77794 }];
      jest.spyOn(etypevariableService, 'query').mockReturnValue(of(new HttpResponse({ body: etypevariableCollection })));
      const additionalEtypevariables = [etypevariable];
      const expectedCollection: IEtypevariable[] = [...additionalEtypevariables, ...etypevariableCollection];
      jest.spyOn(etypevariableService, 'addEtypevariableToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evariable });
      comp.ngOnInit();

      expect(etypevariableService.query).toHaveBeenCalled();
      expect(etypevariableService.addEtypevariableToCollectionIfMissing).toHaveBeenCalledWith(
        etypevariableCollection,
        ...additionalEtypevariables.map(expect.objectContaining)
      );
      expect(comp.etypevariablesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Eunite query and add missing value', () => {
      const evariable: IEvariable = { id: 456 };
      const eunite: IEunite = { id: 49329 };
      evariable.eunite = eunite;

      const euniteCollection: IEunite[] = [{ id: 15174 }];
      jest.spyOn(euniteService, 'query').mockReturnValue(of(new HttpResponse({ body: euniteCollection })));
      const additionalEunites = [eunite];
      const expectedCollection: IEunite[] = [...additionalEunites, ...euniteCollection];
      jest.spyOn(euniteService, 'addEuniteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ evariable });
      comp.ngOnInit();

      expect(euniteService.query).toHaveBeenCalled();
      expect(euniteService.addEuniteToCollectionIfMissing).toHaveBeenCalledWith(
        euniteCollection,
        ...additionalEunites.map(expect.objectContaining)
      );
      expect(comp.eunitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const evariable: IEvariable = { id: 456 };
      const etypevariable: IEtypevariable = { id: 75493 };
      evariable.etypevariable = etypevariable;
      const eunite: IEunite = { id: 11021 };
      evariable.eunite = eunite;

      activatedRoute.data = of({ evariable });
      comp.ngOnInit();

      expect(comp.etypevariablesSharedCollection).toContain(etypevariable);
      expect(comp.eunitesSharedCollection).toContain(eunite);
      expect(comp.evariable).toEqual(evariable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvariable>>();
      const evariable = { id: 123 };
      jest.spyOn(evariableFormService, 'getEvariable').mockReturnValue(evariable);
      jest.spyOn(evariableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evariable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evariable }));
      saveSubject.complete();

      // THEN
      expect(evariableFormService.getEvariable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(evariableService.update).toHaveBeenCalledWith(expect.objectContaining(evariable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvariable>>();
      const evariable = { id: 123 };
      jest.spyOn(evariableFormService, 'getEvariable').mockReturnValue({ id: null });
      jest.spyOn(evariableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evariable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evariable }));
      saveSubject.complete();

      // THEN
      expect(evariableFormService.getEvariable).toHaveBeenCalled();
      expect(evariableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvariable>>();
      const evariable = { id: 123 };
      jest.spyOn(evariableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evariable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(evariableService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEtypevariable', () => {
      it('Should forward to etypevariableService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(etypevariableService, 'compareEtypevariable');
        comp.compareEtypevariable(entity, entity2);
        expect(etypevariableService.compareEtypevariable).toHaveBeenCalledWith(entity, entity2);
      });
    });

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
