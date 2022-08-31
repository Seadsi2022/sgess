import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SlocaliteFormService } from './slocalite-form.service';
import { SlocaliteService } from '../service/slocalite.service';
import { ISlocalite } from '../slocalite.model';
import { IScodevaleur } from 'app/entities/scodevaleur/scodevaleur.model';
import { ScodevaleurService } from 'app/entities/scodevaleur/service/scodevaleur.service';

import { SlocaliteUpdateComponent } from './slocalite-update.component';

describe('Slocalite Management Update Component', () => {
  let comp: SlocaliteUpdateComponent;
  let fixture: ComponentFixture<SlocaliteUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let slocaliteFormService: SlocaliteFormService;
  let slocaliteService: SlocaliteService;
  let scodevaleurService: ScodevaleurService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SlocaliteUpdateComponent],
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
      .overrideTemplate(SlocaliteUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SlocaliteUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    slocaliteFormService = TestBed.inject(SlocaliteFormService);
    slocaliteService = TestBed.inject(SlocaliteService);
    scodevaleurService = TestBed.inject(ScodevaleurService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Slocalite query and add missing value', () => {
      const slocalite: ISlocalite = { id: 456 };
      const parent: ISlocalite = { id: 33316 };
      slocalite.parent = parent;

      const slocaliteCollection: ISlocalite[] = [{ id: 88550 }];
      jest.spyOn(slocaliteService, 'query').mockReturnValue(of(new HttpResponse({ body: slocaliteCollection })));
      const additionalSlocalites = [parent];
      const expectedCollection: ISlocalite[] = [...additionalSlocalites, ...slocaliteCollection];
      jest.spyOn(slocaliteService, 'addSlocaliteToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ slocalite });
      comp.ngOnInit();

      expect(slocaliteService.query).toHaveBeenCalled();
      expect(slocaliteService.addSlocaliteToCollectionIfMissing).toHaveBeenCalledWith(
        slocaliteCollection,
        ...additionalSlocalites.map(expect.objectContaining)
      );
      expect(comp.slocalitesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Scodevaleur query and add missing value', () => {
      const slocalite: ISlocalite = { id: 456 };
      const natureLocalite: IScodevaleur = { id: 46841 };
      slocalite.natureLocalite = natureLocalite;
      const typeLocalite: IScodevaleur = { id: 78392 };
      slocalite.typeLocalite = typeLocalite;

      const scodevaleurCollection: IScodevaleur[] = [{ id: 26274 }];
      jest.spyOn(scodevaleurService, 'query').mockReturnValue(of(new HttpResponse({ body: scodevaleurCollection })));
      const additionalScodevaleurs = [natureLocalite, typeLocalite];
      const expectedCollection: IScodevaleur[] = [...additionalScodevaleurs, ...scodevaleurCollection];
      jest.spyOn(scodevaleurService, 'addScodevaleurToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ slocalite });
      comp.ngOnInit();

      expect(scodevaleurService.query).toHaveBeenCalled();
      expect(scodevaleurService.addScodevaleurToCollectionIfMissing).toHaveBeenCalledWith(
        scodevaleurCollection,
        ...additionalScodevaleurs.map(expect.objectContaining)
      );
      expect(comp.scodevaleursSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const slocalite: ISlocalite = { id: 456 };
      const parent: ISlocalite = { id: 67278 };
      slocalite.parent = parent;
      const natureLocalite: IScodevaleur = { id: 54242 };
      slocalite.natureLocalite = natureLocalite;
      const typeLocalite: IScodevaleur = { id: 25353 };
      slocalite.typeLocalite = typeLocalite;

      activatedRoute.data = of({ slocalite });
      comp.ngOnInit();

      expect(comp.slocalitesSharedCollection).toContain(parent);
      expect(comp.scodevaleursSharedCollection).toContain(natureLocalite);
      expect(comp.scodevaleursSharedCollection).toContain(typeLocalite);
      expect(comp.slocalite).toEqual(slocalite);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISlocalite>>();
      const slocalite = { id: 123 };
      jest.spyOn(slocaliteFormService, 'getSlocalite').mockReturnValue(slocalite);
      jest.spyOn(slocaliteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ slocalite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: slocalite }));
      saveSubject.complete();

      // THEN
      expect(slocaliteFormService.getSlocalite).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(slocaliteService.update).toHaveBeenCalledWith(expect.objectContaining(slocalite));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISlocalite>>();
      const slocalite = { id: 123 };
      jest.spyOn(slocaliteFormService, 'getSlocalite').mockReturnValue({ id: null });
      jest.spyOn(slocaliteService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ slocalite: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: slocalite }));
      saveSubject.complete();

      // THEN
      expect(slocaliteFormService.getSlocalite).toHaveBeenCalled();
      expect(slocaliteService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISlocalite>>();
      const slocalite = { id: 123 };
      jest.spyOn(slocaliteService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ slocalite });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(slocaliteService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareSlocalite', () => {
      it('Should forward to slocaliteService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(slocaliteService, 'compareSlocalite');
        comp.compareSlocalite(entity, entity2);
        expect(slocaliteService.compareSlocalite).toHaveBeenCalledWith(entity, entity2);
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
