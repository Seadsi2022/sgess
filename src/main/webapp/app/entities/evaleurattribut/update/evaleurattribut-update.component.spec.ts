import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EvaleurattributFormService } from './evaleurattribut-form.service';
import { EvaleurattributService } from '../service/evaleurattribut.service';
import { IEvaleurattribut } from '../evaleurattribut.model';

import { EvaleurattributUpdateComponent } from './evaleurattribut-update.component';

describe('Evaleurattribut Management Update Component', () => {
  let comp: EvaleurattributUpdateComponent;
  let fixture: ComponentFixture<EvaleurattributUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let evaleurattributFormService: EvaleurattributFormService;
  let evaleurattributService: EvaleurattributService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EvaleurattributUpdateComponent],
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
      .overrideTemplate(EvaleurattributUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EvaleurattributUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    evaleurattributFormService = TestBed.inject(EvaleurattributFormService);
    evaleurattributService = TestBed.inject(EvaleurattributService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const evaleurattribut: IEvaleurattribut = { id: 456 };

      activatedRoute.data = of({ evaleurattribut });
      comp.ngOnInit();

      expect(comp.evaleurattribut).toEqual(evaleurattribut);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaleurattribut>>();
      const evaleurattribut = { id: 123 };
      jest.spyOn(evaleurattributFormService, 'getEvaleurattribut').mockReturnValue(evaleurattribut);
      jest.spyOn(evaleurattributService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaleurattribut });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evaleurattribut }));
      saveSubject.complete();

      // THEN
      expect(evaleurattributFormService.getEvaleurattribut).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(evaleurattributService.update).toHaveBeenCalledWith(expect.objectContaining(evaleurattribut));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaleurattribut>>();
      const evaleurattribut = { id: 123 };
      jest.spyOn(evaleurattributFormService, 'getEvaleurattribut').mockReturnValue({ id: null });
      jest.spyOn(evaleurattributService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaleurattribut: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: evaleurattribut }));
      saveSubject.complete();

      // THEN
      expect(evaleurattributFormService.getEvaleurattribut).toHaveBeenCalled();
      expect(evaleurattributService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEvaleurattribut>>();
      const evaleurattribut = { id: 123 };
      jest.spyOn(evaleurattributService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ evaleurattribut });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(evaleurattributService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
