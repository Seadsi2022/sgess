import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../eeventualitevariable.test-samples';

import { EeventualitevariableFormService } from './eeventualitevariable-form.service';

describe('Eeventualitevariable Form Service', () => {
  let service: EeventualitevariableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EeventualitevariableFormService);
  });

  describe('Service methods', () => {
    describe('createEeventualitevariableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEeventualitevariableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            evariable: expect.any(Object),
            eeventualite: expect.any(Object),
          })
        );
      });

      it('passing IEeventualitevariable should create a new form with FormGroup', () => {
        const formGroup = service.createEeventualitevariableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            evariable: expect.any(Object),
            eeventualite: expect.any(Object),
          })
        );
      });
    });

    describe('getEeventualitevariable', () => {
      it('should return NewEeventualitevariable for default Eeventualitevariable initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEeventualitevariableFormGroup(sampleWithNewData);

        const eeventualitevariable = service.getEeventualitevariable(formGroup) as any;

        expect(eeventualitevariable).toMatchObject(sampleWithNewData);
      });

      it('should return NewEeventualitevariable for empty Eeventualitevariable initial value', () => {
        const formGroup = service.createEeventualitevariableFormGroup();

        const eeventualitevariable = service.getEeventualitevariable(formGroup) as any;

        expect(eeventualitevariable).toMatchObject({});
      });

      it('should return IEeventualitevariable', () => {
        const formGroup = service.createEeventualitevariableFormGroup(sampleWithRequiredData);

        const eeventualitevariable = service.getEeventualitevariable(formGroup) as any;

        expect(eeventualitevariable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEeventualitevariable should not enable id FormControl', () => {
        const formGroup = service.createEeventualitevariableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEeventualitevariable should disable id FormControl', () => {
        const formGroup = service.createEeventualitevariableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
