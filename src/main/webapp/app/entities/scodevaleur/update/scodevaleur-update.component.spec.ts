import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ScodevaleurFormService } from './scodevaleur-form.service';
import { ScodevaleurService } from '../service/scodevaleur.service';
import { IScodevaleur } from '../scodevaleur.model';
import { IScode } from 'app/entities/scode/scode.model';
import { ScodeService } from 'app/entities/scode/service/scode.service';

import { ScodevaleurUpdateComponent } from './scodevaleur-update.component';

describe('Scodevaleur Management Update Component', () => {
  let comp: ScodevaleurUpdateComponent;
  let fixture: ComponentFixture<ScodevaleurUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let scodevaleurFormService: ScodevaleurFormService;
  let scodevaleurService: ScodevaleurService;
  let scodeService: ScodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ScodevaleurUpdateComponent],
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
      .overrideTemplate(ScodevaleurUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ScodevaleurUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    scodevaleurFormService = TestBed.inject(ScodevaleurFormService);
    scodevaleurService = TestBed.inject(ScodevaleurService);
    scodeService = TestBed.inject(ScodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Scodevaleur query and add missing value', () => {
      const scodevaleur: IScodevaleur = { id: 456 };
      const parent: IScodevaleur = { id: 30786 };
      scodevaleur.parent = parent;

      const scodevaleurCollection: IScodevaleur[] = [{ id: 40877 }];
      jest.spyOn(scodevaleurService, 'query').mockReturnValue(of(new HttpResponse({ body: scodevaleurCollection })));
      const additionalScodevaleurs = [parent];
      const expectedCollection: IScodevaleur[] = [...additionalScodevaleurs, ...scodevaleurCollection];
      jest.spyOn(scodevaleurService, 'addScodevaleurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ scodevaleur });
      comp.ngOnInit();

      expect(scodevaleurService.query).toHaveBeenCalled();
      expect(scodevaleurService.addScodevaleurToCollectionIfMissing).toHaveBeenCalledWith(
        scodevaleurCollection,
        ...additionalScodevaleurs.map(expect.objectContaining)
      );
      expect(comp.scodevaleursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Scode query and add missing value', () => {
      const scodevaleur: IScodevaleur = { id: 456 };
      const scode: IScode = { id: 66775 };
      scodevaleur.scode = scode;

      const scodeCollection: IScode[] = [{ id: 72126 }];
      jest.spyOn(scodeService, 'query').mockReturnValue(of(new HttpResponse({ body: scodeCollection })));
      const additionalScodes = [scode];
      const expectedCollection: IScode[] = [...additionalScodes, ...scodeCollection];
      jest.spyOn(scodeService, 'addScodeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ scodevaleur });
      comp.ngOnInit();

      expect(scodeService.query).toHaveBeenCalled();
      expect(scodeService.addScodeToCollectionIfMissing).toHaveBeenCalledWith(
        scodeCollection,
        ...additionalScodes.map(expect.objectContaining)
      );
      expect(comp.scodesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const scodevaleur: IScodevaleur = { id: 456 };
      const parent: IScodevaleur = { id: 37952 };
      scodevaleur.parent = parent;
      const scode: IScode = { id: 33092 };
      scodevaleur.scode = scode;

      activatedRoute.data = of({ scodevaleur });
      comp.ngOnInit();

      expect(comp.scodevaleursSharedCollection).toContain(parent);
      expect(comp.scodesSharedCollection).toContain(scode);
      expect(comp.scodevaleur).toEqual(scodevaleur);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScodevaleur>>();
      const scodevaleur = { id: 123 };
      jest.spyOn(scodevaleurFormService, 'getScodevaleur').mockReturnValue(scodevaleur);
      jest.spyOn(scodevaleurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scodevaleur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: scodevaleur }));
      saveSubject.complete();

      // THEN
      expect(scodevaleurFormService.getScodevaleur).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(scodevaleurService.update).toHaveBeenCalledWith(expect.objectContaining(scodevaleur));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScodevaleur>>();
      const scodevaleur = { id: 123 };
      jest.spyOn(scodevaleurFormService, 'getScodevaleur').mockReturnValue({ id: null });
      jest.spyOn(scodevaleurService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scodevaleur: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: scodevaleur }));
      saveSubject.complete();

      // THEN
      expect(scodevaleurFormService.getScodevaleur).toHaveBeenCalled();
      expect(scodevaleurService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScodevaleur>>();
      const scodevaleur = { id: 123 };
      jest.spyOn(scodevaleurService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scodevaleur });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(scodevaleurService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareScodevaleur', () => {
      it('Should forward to scodevaleurService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(scodevaleurService, 'compareScodevaleur');
        comp.compareScodevaleur(entity, entity2);
        expect(scodevaleurService.compareScodevaleur).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareScode', () => {
      it('Should forward to scodeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(scodeService, 'compareScode');
        comp.compareScode(entity, entity2);
        expect(scodeService.compareScode).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
