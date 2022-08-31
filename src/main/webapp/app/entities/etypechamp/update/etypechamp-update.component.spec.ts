import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EtypechampFormService } from './etypechamp-form.service';
import { EtypechampService } from '../service/etypechamp.service';
import { IEtypechamp } from '../etypechamp.model';
import { IEattribut } from 'app/entities/eattribut/eattribut.model';
import { EattributService } from 'app/entities/eattribut/service/eattribut.service';

import { EtypechampUpdateComponent } from './etypechamp-update.component';

describe('Etypechamp Management Update Component', () => {
  let comp: EtypechampUpdateComponent;
  let fixture: ComponentFixture<EtypechampUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let etypechampFormService: EtypechampFormService;
  let etypechampService: EtypechampService;
  let eattributService: EattributService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EtypechampUpdateComponent],
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
      .overrideTemplate(EtypechampUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EtypechampUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    etypechampFormService = TestBed.inject(EtypechampFormService);
    etypechampService = TestBed.inject(EtypechampService);
    eattributService = TestBed.inject(EattributService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Eattribut query and add missing value', () => {
      const etypechamp: IEtypechamp = { id: 456 };
      const eattributs: IEattribut[] = [{ id: 67572 }];
      etypechamp.eattributs = eattributs;

      const eattributCollection: IEattribut[] = [{ id: 89382 }];
      jest.spyOn(eattributService, 'query').mockReturnValue(of(new HttpResponse({ body: eattributCollection })));
      const additionalEattributs = [...eattributs];
      const expectedCollection: IEattribut[] = [...additionalEattributs, ...eattributCollection];
      jest.spyOn(eattributService, 'addEattributToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ etypechamp });
      comp.ngOnInit();

      expect(eattributService.query).toHaveBeenCalled();
      expect(eattributService.addEattributToCollectionIfMissing).toHaveBeenCalledWith(
        eattributCollection,
        ...additionalEattributs.map(expect.objectContaining)
      );
      expect(comp.eattributsSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const etypechamp: IEtypechamp = { id: 456 };
      const eattribut: IEattribut = { id: 46678 };
      etypechamp.eattributs = [eattribut];

      activatedRoute.data = of({ etypechamp });
      comp.ngOnInit();

      expect(comp.eattributsSharedCollection).toContain(eattribut);
      expect(comp.etypechamp).toEqual(etypechamp);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtypechamp>>();
      const etypechamp = { id: 123 };
      jest.spyOn(etypechampFormService, 'getEtypechamp').mockReturnValue(etypechamp);
      jest.spyOn(etypechampService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etypechamp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: etypechamp }));
      saveSubject.complete();

      // THEN
      expect(etypechampFormService.getEtypechamp).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(etypechampService.update).toHaveBeenCalledWith(expect.objectContaining(etypechamp));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtypechamp>>();
      const etypechamp = { id: 123 };
      jest.spyOn(etypechampFormService, 'getEtypechamp').mockReturnValue({ id: null });
      jest.spyOn(etypechampService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etypechamp: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: etypechamp }));
      saveSubject.complete();

      // THEN
      expect(etypechampFormService.getEtypechamp).toHaveBeenCalled();
      expect(etypechampService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtypechamp>>();
      const etypechamp = { id: 123 };
      jest.spyOn(etypechampService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etypechamp });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(etypechampService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEattribut', () => {
      it('Should forward to eattributService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(eattributService, 'compareEattribut');
        comp.compareEattribut(entity, entity2);
        expect(eattributService.compareEattribut).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
