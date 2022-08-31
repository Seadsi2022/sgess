import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ScodeFormService } from './scode-form.service';
import { ScodeService } from '../service/scode.service';
import { IScode } from '../scode.model';

import { ScodeUpdateComponent } from './scode-update.component';

describe('Scode Management Update Component', () => {
  let comp: ScodeUpdateComponent;
  let fixture: ComponentFixture<ScodeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let scodeFormService: ScodeFormService;
  let scodeService: ScodeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ScodeUpdateComponent],
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
      .overrideTemplate(ScodeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ScodeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    scodeFormService = TestBed.inject(ScodeFormService);
    scodeService = TestBed.inject(ScodeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should update editForm', () => {
      const scode: IScode = { id: 456 };

      activatedRoute.data = of({ scode });
      comp.ngOnInit();

      expect(comp.scode).toEqual(scode);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScode>>();
      const scode = { id: 123 };
      jest.spyOn(scodeFormService, 'getScode').mockReturnValue(scode);
      jest.spyOn(scodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: scode }));
      saveSubject.complete();

      // THEN
      expect(scodeFormService.getScode).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(scodeService.update).toHaveBeenCalledWith(expect.objectContaining(scode));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScode>>();
      const scode = { id: 123 };
      jest.spyOn(scodeFormService, 'getScode').mockReturnValue({ id: null });
      jest.spyOn(scodeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scode: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: scode }));
      saveSubject.complete();

      // THEN
      expect(scodeFormService.getScode).toHaveBeenCalled();
      expect(scodeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IScode>>();
      const scode = { id: 123 };
      jest.spyOn(scodeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ scode });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(scodeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });
});
