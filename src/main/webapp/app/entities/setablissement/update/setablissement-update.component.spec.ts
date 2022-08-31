import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { SetablissementFormService } from './setablissement-form.service';
import { SetablissementService } from '../service/setablissement.service';
import { ISetablissement } from '../setablissement.model';
import { ISstructure } from 'app/entities/sstructure/sstructure.model';
import { SstructureService } from 'app/entities/sstructure/service/sstructure.service';

import { SetablissementUpdateComponent } from './setablissement-update.component';

describe('Setablissement Management Update Component', () => {
  let comp: SetablissementUpdateComponent;
  let fixture: ComponentFixture<SetablissementUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let setablissementFormService: SetablissementFormService;
  let setablissementService: SetablissementService;
  let sstructureService: SstructureService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [SetablissementUpdateComponent],
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
      .overrideTemplate(SetablissementUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(SetablissementUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    setablissementFormService = TestBed.inject(SetablissementFormService);
    setablissementService = TestBed.inject(SetablissementService);
    sstructureService = TestBed.inject(SstructureService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Sstructure query and add missing value', () => {
      const setablissement: ISetablissement = { id: 456 };
      const sstructure: ISstructure = { id: 88721 };
      setablissement.sstructure = sstructure;

      const sstructureCollection: ISstructure[] = [{ id: 41459 }];
      jest.spyOn(sstructureService, 'query').mockReturnValue(of(new HttpResponse({ body: sstructureCollection })));
      const additionalSstructures = [sstructure];
      const expectedCollection: ISstructure[] = [...additionalSstructures, ...sstructureCollection];
      jest.spyOn(sstructureService, 'addSstructureToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ setablissement });
      comp.ngOnInit();

      expect(sstructureService.query).toHaveBeenCalled();
      expect(sstructureService.addSstructureToCollectionIfMissing).toHaveBeenCalledWith(
        sstructureCollection,
        ...additionalSstructures.map(expect.objectContaining)
      );
      expect(comp.sstructuresSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const setablissement: ISetablissement = { id: 456 };
      const sstructure: ISstructure = { id: 36010 };
      setablissement.sstructure = sstructure;

      activatedRoute.data = of({ setablissement });
      comp.ngOnInit();

      expect(comp.sstructuresSharedCollection).toContain(sstructure);
      expect(comp.setablissement).toEqual(setablissement);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISetablissement>>();
      const setablissement = { id: 123 };
      jest.spyOn(setablissementFormService, 'getSetablissement').mockReturnValue(setablissement);
      jest.spyOn(setablissementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ setablissement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: setablissement }));
      saveSubject.complete();

      // THEN
      expect(setablissementFormService.getSetablissement).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(setablissementService.update).toHaveBeenCalledWith(expect.objectContaining(setablissement));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISetablissement>>();
      const setablissement = { id: 123 };
      jest.spyOn(setablissementFormService, 'getSetablissement').mockReturnValue({ id: null });
      jest.spyOn(setablissementService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ setablissement: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: setablissement }));
      saveSubject.complete();

      // THEN
      expect(setablissementFormService.getSetablissement).toHaveBeenCalled();
      expect(setablissementService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ISetablissement>>();
      const setablissement = { id: 123 };
      jest.spyOn(setablissementService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ setablissement });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(setablissementService.update).toHaveBeenCalled();
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
  });
});
