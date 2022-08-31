import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EgroupeFormService } from './egroupe-form.service';
import { EgroupeService } from '../service/egroupe.service';
import { IEgroupe } from '../egroupe.model';
import { IEquestionnaire } from 'app/entities/equestionnaire/equestionnaire.model';
import { EquestionnaireService } from 'app/entities/equestionnaire/service/equestionnaire.service';

import { EgroupeUpdateComponent } from './egroupe-update.component';

describe('Egroupe Management Update Component', () => {
  let comp: EgroupeUpdateComponent;
  let fixture: ComponentFixture<EgroupeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let egroupeFormService: EgroupeFormService;
  let egroupeService: EgroupeService;
  let equestionnaireService: EquestionnaireService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EgroupeUpdateComponent],
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
      .overrideTemplate(EgroupeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EgroupeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    egroupeFormService = TestBed.inject(EgroupeFormService);
    egroupeService = TestBed.inject(EgroupeService);
    equestionnaireService = TestBed.inject(EquestionnaireService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Equestionnaire query and add missing value', () => {
      const egroupe: IEgroupe = { id: 456 };
      const equestionnaire: IEquestionnaire = { id: 2592 };
      egroupe.equestionnaire = equestionnaire;

      const equestionnaireCollection: IEquestionnaire[] = [{ id: 64844 }];
      jest.spyOn(equestionnaireService, 'query').mockReturnValue(of(new HttpResponse({ body: equestionnaireCollection })));
      const additionalEquestionnaires = [equestionnaire];
      const expectedCollection: IEquestionnaire[] = [...additionalEquestionnaires, ...equestionnaireCollection];
      jest.spyOn(equestionnaireService, 'addEquestionnaireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ egroupe });
      comp.ngOnInit();

      expect(equestionnaireService.query).toHaveBeenCalled();
      expect(equestionnaireService.addEquestionnaireToCollectionIfMissing).toHaveBeenCalledWith(
        equestionnaireCollection,
        ...additionalEquestionnaires.map(expect.objectContaining)
      );
      expect(comp.equestionnairesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const egroupe: IEgroupe = { id: 456 };
      const equestionnaire: IEquestionnaire = { id: 67155 };
      egroupe.equestionnaire = equestionnaire;

      activatedRoute.data = of({ egroupe });
      comp.ngOnInit();

      expect(comp.equestionnairesSharedCollection).toContain(equestionnaire);
      expect(comp.egroupe).toEqual(egroupe);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEgroupe>>();
      const egroupe = { id: 123 };
      jest.spyOn(egroupeFormService, 'getEgroupe').mockReturnValue(egroupe);
      jest.spyOn(egroupeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egroupe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: egroupe }));
      saveSubject.complete();

      // THEN
      expect(egroupeFormService.getEgroupe).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(egroupeService.update).toHaveBeenCalledWith(expect.objectContaining(egroupe));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEgroupe>>();
      const egroupe = { id: 123 };
      jest.spyOn(egroupeFormService, 'getEgroupe').mockReturnValue({ id: null });
      jest.spyOn(egroupeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egroupe: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: egroupe }));
      saveSubject.complete();

      // THEN
      expect(egroupeFormService.getEgroupe).toHaveBeenCalled();
      expect(egroupeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEgroupe>>();
      const egroupe = { id: 123 };
      jest.spyOn(egroupeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ egroupe });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(egroupeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareEquestionnaire', () => {
      it('Should forward to equestionnaireService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(equestionnaireService, 'compareEquestionnaire');
        comp.compareEquestionnaire(entity, entity2);
        expect(equestionnaireService.compareEquestionnaire).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
