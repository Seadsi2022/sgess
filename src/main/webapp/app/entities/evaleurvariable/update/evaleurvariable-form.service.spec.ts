import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../evaleurvariable.test-samples';

import { EvaleurvariableFormService } from './evaleurvariable-form.service';

describe('Evaleurvariable Form Service', () => {
  let service: EvaleurvariableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EvaleurvariableFormService);
  });

  describe('Service methods', () => {
    describe('createEvaleurvariableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEvaleurvariableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            valeur: expect.any(Object),
            ligne: expect.any(Object),
            colonne: expect.any(Object),
            isActive: expect.any(Object),
            egroupevariable: expect.any(Object),
            sstructure: expect.any(Object),
          })
        );
      });

      it('passing IEvaleurvariable should create a new form with FormGroup', () => {
        const formGroup = service.createEvaleurvariableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            valeur: expect.any(Object),
            ligne: expect.any(Object),
            colonne: expect.any(Object),
            isActive: expect.any(Object),
            egroupevariable: expect.any(Object),
            sstructure: expect.any(Object),
          })
        );
      });
    });

    describe('getEvaleurvariable', () => {
      it('should return NewEvaleurvariable for default Evaleurvariable initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEvaleurvariableFormGroup(sampleWithNewData);

        const evaleurvariable = service.getEvaleurvariable(formGroup) as any;

        expect(evaleurvariable).toMatchObject(sampleWithNewData);
      });

      it('should return NewEvaleurvariable for empty Evaleurvariable initial value', () => {
        const formGroup = service.createEvaleurvariableFormGroup();

        const evaleurvariable = service.getEvaleurvariable(formGroup) as any;

        expect(evaleurvariable).toMatchObject({});
      });

      it('should return IEvaleurvariable', () => {
        const formGroup = service.createEvaleurvariableFormGroup(sampleWithRequiredData);

        const evaleurvariable = service.getEvaleurvariable(formGroup) as any;

        expect(evaleurvariable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEvaleurvariable should not enable id FormControl', () => {
        const formGroup = service.createEvaleurvariableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEvaleurvariable should disable id FormControl', () => {
        const formGroup = service.createEvaleurvariableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
