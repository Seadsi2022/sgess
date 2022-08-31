import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../evaleurattribut.test-samples';

import { EvaleurattributFormService } from './evaleurattribut-form.service';

describe('Evaleurattribut Form Service', () => {
  let service: EvaleurattributFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EvaleurattributFormService);
  });

  describe('Service methods', () => {
    describe('createEvaleurattributFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEvaleurattributFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            valeur: expect.any(Object),
            valeurdisplayname: expect.any(Object),
          })
        );
      });

      it('passing IEvaleurattribut should create a new form with FormGroup', () => {
        const formGroup = service.createEvaleurattributFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            valeur: expect.any(Object),
            valeurdisplayname: expect.any(Object),
          })
        );
      });
    });

    describe('getEvaleurattribut', () => {
      it('should return NewEvaleurattribut for default Evaleurattribut initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEvaleurattributFormGroup(sampleWithNewData);

        const evaleurattribut = service.getEvaleurattribut(formGroup) as any;

        expect(evaleurattribut).toMatchObject(sampleWithNewData);
      });

      it('should return NewEvaleurattribut for empty Evaleurattribut initial value', () => {
        const formGroup = service.createEvaleurattributFormGroup();

        const evaleurattribut = service.getEvaleurattribut(formGroup) as any;

        expect(evaleurattribut).toMatchObject({});
      });

      it('should return IEvaleurattribut', () => {
        const formGroup = service.createEvaleurattributFormGroup(sampleWithRequiredData);

        const evaleurattribut = service.getEvaleurattribut(formGroup) as any;

        expect(evaleurattribut).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEvaleurattribut should not enable id FormControl', () => {
        const formGroup = service.createEvaleurattributFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEvaleurattribut should disable id FormControl', () => {
        const formGroup = service.createEvaleurattributFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
