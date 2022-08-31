import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { EtypevariableFormService } from './etypevariable-form.service';
import { EtypevariableService } from '../service/etypevariable.service';
import { IEtypevariable } from '../etypevariable.model';

import { EtypevariableUpdateComponent } from './etypevariable-update.component';

describe('Etypevariable Management Update Component', () => {
  let comp: EtypevariableUpdateComponent;
  let fixture: ComponentFixture<EtypevariableUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let etypevariableFormService: EtypevariableFormService;
  let etypevariableService: EtypevariableService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [EtypevariableUpdateComponent],
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
      .overrideTemplate(EtypevariableUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(EtypevariableUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    etypevariableFormService = TestBed.inject(EtypevariableFormService);
    etypevariableService = TestBed.inject(EtypevariableService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const etypevariable: IEtypevariable = { id: 456 };

      activatedRoute.data = of({ etypevariable });
      comp.ngOnInit();

      expect(comp.etypevariable).toEqual(etypevariable);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtypevariable>>();
      const etypevariable = { id: 123 };
      jest.spyOn(etypevariableFormService, 'getEtypevariable').mockReturnValue(etypevariable);
      jest.spyOn(etypevariableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etypevariable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: etypevariable }));
      saveSubject.complete();

      // THEN
      expect(etypevariableFormService.getEtypevariable).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(etypevariableService.update).toHaveBeenCalledWith(expect.objectContaining(etypevariable));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtypevariable>>();
      const etypevariable = { id: 123 };
      jest.spyOn(etypevariableFormService, 'getEtypevariable').mockReturnValue({ id: null });
      jest.spyOn(etypevariableService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etypevariable: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: etypevariable }));
      saveSubject.complete();

      // THEN
      expect(etypevariableFormService.getEtypevariable).toHaveBeenCalled();
      expect(etypevariableService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IEtypevariable>>();
      const etypevariable = { id: 123 };
      jest.spyOn(etypevariableService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ etypevariable });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(etypevariableService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
