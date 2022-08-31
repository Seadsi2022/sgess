import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SstructureFormService } from './sstructure-form.service';
import { SstructureService } from '../service/sstructure.service';
import { ISstructure } from '../sstructure.model';
import { IScodevaleur } from 'app/entities/scodevaleur/scodevaleur.model';
import { ScodevaleurService } from 'app/entities/scodevaleur/service/scodevaleur.service';
import { ISlocalite } from 'app/entities/slocalite/slocalite.model';
import { SlocaliteService } from 'app/entities/slocalite/service/slocalite.service';

import { SstructureUpdateComponent } from './sstructure-update.component';

describe('Sstructure Management Update Component', () => {
  let comp: SstructureUpdateComponent;
  let fixture: ComponentFixture<SstructureUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let sstructureFormService: SstructureFormService;
  let sstructureService: SstructureService;
  let scodevaleurService: ScodevaleurService;
  let slocaliteService: SlocaliteService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SstructureUpdateComponent],
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
      .overrideTemplate(SstructureUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SstructureUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    sstructureFormService = TestBed.inject(SstructureFormService);
    sstructureService = TestBed.inject(SstructureService);
    scodevaleurService = TestBed.inject(ScodevaleurService);
    slocaliteService = TestBed.inject(SlocaliteService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Sstructure query and add missing value', () => {
      const sstructure: ISstructure = { id: 456 };
      const parent: ISstructure = { id: 44655 };
      sstructure.parent = parent;

      const sstructureCollection: ISstructure[] = [{ id: 13646 }];
      jest.spyOn(sstructureService, 'query').mockReturnValue(of(new HttpResponse({ body: sstructureCollection })));
      const additionalSstructures = [parent];
      const expectedCollection: ISstructure[] = [...additionalSstructures, ...sstructureCollection];
      jest.spyOn(sstructureService, 'addSstructureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sstructure });
      comp.ngOnInit();

      expect(sstructureService.query).toHaveBeenCalled();
      expect(sstructureService.addSstructureToCollectionIfMissing).toHaveBeenCalledWith(
        sstructureCollection,
        ...additionalSstructures.map(expect.objectContaining)
      );
      expect(comp.sstructuresSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Scodevaleur query and add missing value', () => {
      const sstructure: ISstructure = { id: 456 };
      const scodes: IScodevaleur[] = [{ id: 53951 }];
      sstructure.scodes = scodes;

      const scodevaleurCollection: IScodevaleur[] = [{ id: 43870 }];
      jest.spyOn(scodevaleurService, 'query').mockReturnValue(of(new HttpResponse({ body: scodevaleurCollection })));
      const additionalScodevaleurs = [...scodes];
      const expectedCollection: IScodevaleur[] = [...additionalScodevaleurs, ...scodevaleurCollection];
      jest.spyOn(scodevaleurService, 'addScodevaleurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sstructure });
      comp.ngOnInit();

      expect(scodevaleurService.query).toHaveBeenCalled();
      expect(scodevaleurService.addScodevaleurToCollectionIfMissing).toHaveBeenCalledWith(
        scodevaleurCollection,
        ...additionalScodevaleurs.map(expect.objectContaining)
      );
      expect(comp.scodevaleursSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Slocalite query and add missing value', () => {
      const sstructure: ISstructure = { id: 456 };
      const slocalite: ISlocalite = { id: 8763 };
      sstructure.slocalite = slocalite;

      const slocaliteCollection: ISlocalite[] = [{ id: 66610 }];
      jest.spyOn(slocaliteService, 'query').mockReturnValue(of(new HttpResponse({ body: slocaliteCollection })));
      const additionalSlocalites = [slocalite];
      const expectedCollection: ISlocalite[] = [...additionalSlocalites, ...slocaliteCollection];
      jest.spyOn(slocaliteService, 'addSlocaliteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ sstructure });
      comp.ngOnInit();

      expect(slocaliteService.query).toHaveBeenCalled();
      expect(slocaliteService.addSlocaliteToCollectionIfMissing).toHaveBeenCalledWith(
        slocaliteCollection,
        ...additionalSlocalites.map(expect.objectContaining)
      );
      expect(comp.slocalitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const sstructure: ISstructure = { id: 456 };
      const parent: ISstructure = { id: 16089 };
      sstructure.parent = parent;
      const scode: IScodevaleur = { id: 21673 };
      sstructure.scodes = [scode];
      const slocalite: ISlocalite = { id: 96804 };
      sstructure.slocalite = slocalite;

      activatedRoute.data = of({ sstructure });
      comp.ngOnInit();

      expect(comp.sstructuresSharedCollection).toContain(parent);
      expect(comp.scodevaleursSharedCollection).toContain(scode);
      expect(comp.slocalitesSharedCollection).toContain(slocalite);
      expect(comp.sstructure).toEqual(sstructure);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISstructure>>();
      const sstructure = { id: 123 };
      jest.spyOn(sstructureFormService, 'getSstructure').mockReturnValue(sstructure);
      jest.spyOn(sstructureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sstructure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sstructure }));
      saveSubject.complete();

      // THEN
      expect(sstructureFormService.getSstructure).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(sstructureService.update).toHaveBeenCalledWith(expect.objectContaining(sstructure));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISstructure>>();
      const sstructure = { id: 123 };
      jest.spyOn(sstructureFormService, 'getSstructure').mockReturnValue({ id: null });
      jest.spyOn(sstructureService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sstructure: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: sstructure }));
      saveSubject.complete();

      // THEN
      expect(sstructureFormService.getSstructure).toHaveBeenCalled();
      expect(sstructureService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISstructure>>();
      const sstructure = { id: 123 };
      jest.spyOn(sstructureService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ sstructure });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(sstructureService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSstructure', () => {
      it('Should forward to sstructureService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(sstructureService, 'compareSstructure');
        comp.compareSstructure(entity, entity2);
        expect(sstructureService.compareSstructure).toHaveBeenCalledWith(entity, entity2);
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

    describe('compareSlocalite', () => {
      it('Should forward to slocaliteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(slocaliteService, 'compareSlocalite');
        comp.compareSlocalite(entity, entity2);
        expect(slocaliteService.compareSlocalite).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
