import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EattributvariableFormService } from './eattributvariable-form.service';
import { EattributvariableService } from '../service/eattributvariable.service';
import { IEattributvariable } from '../eattributvariable.model';
import { IEvariable } from 'app/entities/evariable/evariable.model';
import { EvariableService } from 'app/entities/evariable/service/evariable.service';

import { EattributvariableUpdateComponent } from './eattributvariable-update.component';

describe('Eattributvariable Management Update Component', () => {
  let comp: EattributvariableUpdateComponent;
  let fixture: ComponentFixture<EattributvariableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let eattributvariableFormService: EattributvariableFormService;
  let eattributvariableService: EattributvariableService;
  let evariableService: EvariableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EattributvariableUpdateComponent],
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
      .overrideTemplate(EattributvariableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EattributvariableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    eattributvariableFormService = TestBed.inject(EattributvariableFormService);
    eattributvariableService = TestBed.inject(EattributvariableService);
    evariableService = TestBed.inject(EvariableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Evariable query and add missing value', () => {
      const eattributvariable: IEattributvariable = { id: 456 };
      const evariable: IEvariable = { id: 71695 };
      eattributvariable.evariable = evariable;

      const evariableCollection: IEvariable[] = [{ id: 71033 }];
      jest.spyOn(evariableService, 'query').mockReturnValue(of(new HttpResponse({ body: evariableCollection })));
      const additionalEvariables = [evariable];
      const expectedCollection: IEvariable[] = [...additionalEvariables, ...evariableCollection];
      jest.spyOn(evariableService, 'addEvariableToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ eattributvariable });
      comp.ngOnInit();

      expect(evariableService.query).toHaveBeenCalled();
      expect(evariableService.addEvariableToCollectionIfMissing).toHaveBeenCalledWith(
        evariableCollection,
        ...additionalEvariables.map(expect.objectContaining)
      );
      expect(comp.evariablesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const eattributvariable: IEattributvariable = { id: 456 };
      const evariable: IEvariable = { id: 83241 };
      eattributvariable.evariable = evariable;

      activatedRoute.data = of({ eattributvariable });
      comp.ngOnInit();

      expect(comp.evariablesSharedCollection).toContain(evariable);
      expect(comp.eattributvariable).toEqual(eattributvariable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEattributvariable>>();
      const eattributvariable = { id: 123 };
      jest.spyOn(eattributvariableFormService, 'getEattributvariable').mockReturnValue(eattributvariable);
      jest.spyOn(eattributvariableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eattributvariable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eattributvariable }));
      saveSubject.complete();

      // THEN
      expect(eattributvariableFormService.getEattributvariable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(eattributvariableService.update).toHaveBeenCalledWith(expect.objectContaining(eattributvariable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEattributvariable>>();
      const eattributvariable = { id: 123 };
      jest.spyOn(eattributvariableFormService, 'getEattributvariable').mockReturnValue({ id: null });
      jest.spyOn(eattributvariableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eattributvariable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: eattributvariable }));
      saveSubject.complete();

      // THEN
      expect(eattributvariableFormService.getEattributvariable).toHaveBeenCalled();
      expect(eattributvariableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEattributvariable>>();
      const eattributvariable = { id: 123 };
      jest.spyOn(eattributvariableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ eattributvariable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(eattributvariableService.update).toHaveBeenCalled();
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
  });
});
