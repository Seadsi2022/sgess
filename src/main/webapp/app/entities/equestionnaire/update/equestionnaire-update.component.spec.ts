import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EquestionnaireFormService } from './equestionnaire-form.service';
import { EquestionnaireService } from '../service/equestionnaire.service';
import { IEquestionnaire } from '../equestionnaire.model';
import { IEcampagne } from 'app/entities/ecampagne/ecampagne.model';
import { EcampagneService } from 'app/entities/ecampagne/service/ecampagne.service';
import { IScodevaleur } from 'app/entities/scodevaleur/scodevaleur.model';
import { ScodevaleurService } from 'app/entities/scodevaleur/service/scodevaleur.service';

import { EquestionnaireUpdateComponent } from './equestionnaire-update.component';

describe('Equestionnaire Management Update Component', () => {
  let comp: EquestionnaireUpdateComponent;
  let fixture: ComponentFixture<EquestionnaireUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let equestionnaireFormService: EquestionnaireFormService;
  let equestionnaireService: EquestionnaireService;
  let ecampagneService: EcampagneService;
  let scodevaleurService: ScodevaleurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EquestionnaireUpdateComponent],
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
      .overrideTemplate(EquestionnaireUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EquestionnaireUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    equestionnaireFormService = TestBed.inject(EquestionnaireFormService);
    equestionnaireService = TestBed.inject(EquestionnaireService);
    ecampagneService = TestBed.inject(EcampagneService);
    scodevaleurService = TestBed.inject(ScodevaleurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Equestionnaire query and add missing value', () => {
      const equestionnaire: IEquestionnaire = { id: 456 };
      const parent: IEquestionnaire = { id: 84761 };
      equestionnaire.parent = parent;

      const equestionnaireCollection: IEquestionnaire[] = [{ id: 20403 }];
      jest.spyOn(equestionnaireService, 'query').mockReturnValue(of(new HttpResponse({ body: equestionnaireCollection })));
      const additionalEquestionnaires = [parent];
      const expectedCollection: IEquestionnaire[] = [...additionalEquestionnaires, ...equestionnaireCollection];
      jest.spyOn(equestionnaireService, 'addEquestionnaireToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ equestionnaire });
      comp.ngOnInit();

      expect(equestionnaireService.query).toHaveBeenCalled();
      expect(equestionnaireService.addEquestionnaireToCollectionIfMissing).toHaveBeenCalledWith(
        equestionnaireCollection,
        ...additionalEquestionnaires.map(expect.objectContaining)
      );
      expect(comp.equestionnairesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Ecampagne query and add missing value', () => {
      const equestionnaire: IEquestionnaire = { id: 456 };
      const ecampagne: IEcampagne = { id: 63638 };
      equestionnaire.ecampagne = ecampagne;

      const ecampagneCollection: IEcampagne[] = [{ id: 2247 }];
      jest.spyOn(ecampagneService, 'query').mockReturnValue(of(new HttpResponse({ body: ecampagneCollection })));
      const additionalEcampagnes = [ecampagne];
      const expectedCollection: IEcampagne[] = [...additionalEcampagnes, ...ecampagneCollection];
      jest.spyOn(ecampagneService, 'addEcampagneToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ equestionnaire });
      comp.ngOnInit();

      expect(ecampagneService.query).toHaveBeenCalled();
      expect(ecampagneService.addEcampagneToCollectionIfMissing).toHaveBeenCalledWith(
        ecampagneCollection,
        ...additionalEcampagnes.map(expect.objectContaining)
      );
      expect(comp.ecampagnesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Scodevaleur query and add missing value', () => {
      const equestionnaire: IEquestionnaire = { id: 456 };
      const typestructure: IScodevaleur = { id: 75600 };
      equestionnaire.typestructure = typestructure;

      const scodevaleurCollection: IScodevaleur[] = [{ id: 73363 }];
      jest.spyOn(scodevaleurService, 'query').mockReturnValue(of(new HttpResponse({ body: scodevaleurCollection })));
      const additionalScodevaleurs = [typestructure];
      const expectedCollection: IScodevaleur[] = [...additionalScodevaleurs, ...scodevaleurCollection];
      jest.spyOn(scodevaleurService, 'addScodevaleurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ equestionnaire });
      comp.ngOnInit();

      expect(scodevaleurService.query).toHaveBeenCalled();
      expect(scodevaleurService.addScodevaleurToCollectionIfMissing).toHaveBeenCalledWith(
        scodevaleurCollection,
        ...additionalScodevaleurs.map(expect.objectContaining)
      );
      expect(comp.scodevaleursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const equestionnaire: IEquestionnaire = { id: 456 };
      const parent: IEquestionnaire = { id: 3118 };
      equestionnaire.parent = parent;
      const ecampagne: IEcampagne = { id: 5296 };
      equestionnaire.ecampagne = ecampagne;
      const typestructure: IScodevaleur = { id: 89115 };
      equestionnaire.typestructure = typestructure;

      activatedRoute.data = of({ equestionnaire });
      comp.ngOnInit();

      expect(comp.equestionnairesSharedCollection).toContain(parent);
      expect(comp.ecampagnesSharedCollection).toContain(ecampagne);
      expect(comp.scodevaleursSharedCollection).toContain(typestructure);
      expect(comp.equestionnaire).toEqual(equestionnaire);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEquestionnaire>>();
      const equestionnaire = { id: 123 };
      jest.spyOn(equestionnaireFormService, 'getEquestionnaire').mockReturnValue(equestionnaire);
      jest.spyOn(equestionnaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ equestionnaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: equestionnaire }));
      saveSubject.complete();

      // THEN
      expect(equestionnaireFormService.getEquestionnaire).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(equestionnaireService.update).toHaveBeenCalledWith(expect.objectContaining(equestionnaire));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEquestionnaire>>();
      const equestionnaire = { id: 123 };
      jest.spyOn(equestionnaireFormService, 'getEquestionnaire').mockReturnValue({ id: null });
      jest.spyOn(equestionnaireService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ equestionnaire: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: equestionnaire }));
      saveSubject.complete();

      // THEN
      expect(equestionnaireFormService.getEquestionnaire).toHaveBeenCalled();
      expect(equestionnaireService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEquestionnaire>>();
      const equestionnaire = { id: 123 };
      jest.spyOn(equestionnaireService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ equestionnaire });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(equestionnaireService.update).toHaveBeenCalled();
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

    describe('compareEcampagne', () => {
      it('Should forward to ecampagneService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(ecampagneService, 'compareEcampagne');
        comp.compareEcampagne(entity, entity2);
        expect(ecampagneService.compareEcampagne).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareScodevaleur', () => {
      it('Should forward to scodevaleurService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(scodevaleurService, 'compareScodevaleur');
        comp.compareScodevaleur(entity, entity2);
        expect(scodevaleurService.compareScodevaleur).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
