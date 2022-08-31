import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EeventualitevariableFormService } from './eeventualitevariable-form.service';
import { EeventualitevariableService } from '../service/eeventualitevariable.service';
import { IEeventualitevariable } from '../eeventualitevariable.model';
import { IEvariable } from 'app/entities/evariable/evariable.model';
import { EvariableService } from 'app/entities/evariable/service/evariable.service';
import { IEeventualite } from 'app/entities/eeventualite/eeventualite.model';
import { EeventualiteService } from 'app/entities/eeventualite/service/eeventualite.service';

import { EeventualitevariableUpdateComponent } from './eeventualitevariable-update.component';

describe('Eeventualitevariable Management Update Component', () => {
  let comp: EeventualitevariableUpdateComponent;
  let fixture: ComponentFixture<EeventualitevariableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eeventualitevariableFormService: EeventualitevariableFormService;
  let eeventualitevariableService: EeventualitevariableService;
  let evariableService: EvariableService;
  let eeventualiteService: EeventualiteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EeventualitevariableUpdateComponent],
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
      .overrideTemplate(EeventualitevariableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EeventualitevariableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eeventualitevariableFormService = TestBed.inject(EeventualitevariableFormService);
    eeventualitevariableService = TestBed.inject(EeventualitevariableService);
    evariableService = TestBed.inject(EvariableService);
    eeventualiteService = TestBed.inject(EeventualiteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Evariable query and add missing value', () => {
      const eeventualitevariable: IEeventualitevariable = { id: 456 };
      const evariable: IEvariable = { id: 84523 };
      eeventualitevariable.evariable = evariable;

      const evariableCollection: IEvariable[] = [{ id: 22631 }];
      jest.spyOn(evariableService, 'query').mockReturnValue(of(new HttpResponse({ body: evariableCollection })));
      const additionalEvariables = [evariable];
      const expectedCollection: IEvariable[] = [...additionalEvariables, ...evariableCollection];
      jest.spyOn(evariableService, 'addEvariableToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eeventualitevariable });
      comp.ngOnInit();

      expect(evariableService.query).toHaveBeenCalled();
      expect(evariableService.addEvariableToCollectionIfMissing).toHaveBeenCalledWith(
        evariableCollection,
        ...additionalEvariables.map(expect.objectContaining)
      );
      expect(comp.evariablesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Eeventualite query and add missing value', () => {
      const eeventualitevariable: IEeventualitevariable = { id: 456 };
      const eeventualite: IEeventualite = { id: 72002 };
      eeventualitevariable.eeventualite = eeventualite;

      const eeventualiteCollection: IEeventualite[] = [{ id: 72539 }];
      jest.spyOn(eeventualiteService, 'query').mockReturnValue(of(new HttpResponse({ body: eeventualiteCollection })));
      const additionalEeventualites = [eeventualite];
      const expectedCollection: IEeventualite[] = [...additionalEeventualites, ...eeventualiteCollection];
      jest.spyOn(eeventualiteService, 'addEeventualiteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eeventualitevariable });
      comp.ngOnInit();

      expect(eeventualiteService.query).toHaveBeenCalled();
      expect(eeventualiteService.addEeventualiteToCollectionIfMissing).toHaveBeenCalledWith(
        eeventualiteCollection,
        ...additionalEeventualites.map(expect.objectContaining)
      );
      expect(comp.eeventualitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const eeventualitevariable: IEeventualitevariable = { id: 456 };
      const evariable: IEvariable = { id: 86817 };
      eeventualitevariable.evariable = evariable;
      const eeventualite: IEeventualite = { id: 11529 };
      eeventualitevariable.eeventualite = eeventualite;

      activatedRoute.data = of({ eeventualitevariable });
      comp.ngOnInit();

      expect(comp.evariablesSharedCollection).toContain(evariable);
      expect(comp.eeventualitesSharedCollection).toContain(eeventualite);
      expect(comp.eeventualitevariable).toEqual(eeventualitevariable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEeventualitevariable>>();
      const eeventualitevariable = { id: 123 };
      jest.spyOn(eeventualitevariableFormService, 'getEeventualitevariable').mockReturnValue(eeventualitevariable);
      jest.spyOn(eeventualitevariableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eeventualitevariable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eeventualitevariable }));
      saveSubject.complete();

      // THEN
      expect(eeventualitevariableFormService.getEeventualitevariable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(eeventualitevariableService.update).toHaveBeenCalledWith(expect.objectContaining(eeventualitevariable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEeventualitevariable>>();
      const eeventualitevariable = { id: 123 };
      jest.spyOn(eeventualitevariableFormService, 'getEeventualitevariable').mockReturnValue({ id: null });
      jest.spyOn(eeventualitevariableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eeventualitevariable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eeventualitevariable }));
      saveSubject.complete();

      // THEN
      expect(eeventualitevariableFormService.getEeventualitevariable).toHaveBeenCalled();
      expect(eeventualitevariableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEeventualitevariable>>();
      const eeventualitevariable = { id: 123 };
      jest.spyOn(eeventualitevariableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eeventualitevariable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eeventualitevariableService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEvariable', () => {
      it('Should forward to evariableService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(evariableService, 'compareEvariable');
        comp.compareEvariable(entity, entity2);
        expect(evariableService.compareEvariable).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareEeventualite', () => {
      it('Should forward to eeventualiteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(eeventualiteService, 'compareEeventualite');
        comp.compareEeventualite(entity, entity2);
        expect(eeventualiteService.compareEeventualite).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
