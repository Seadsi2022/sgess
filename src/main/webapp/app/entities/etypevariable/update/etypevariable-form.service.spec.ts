import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../etypevariable.test-samples';

import { EtypevariableFormService } from './etypevariable-form.service';

describe('Etypevariable Form Service', () => {
  let service: EtypevariableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EtypevariableFormService);
  });

  describe('Service methods', () => {
    describe('createEtypevariableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEtypevariableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomtypevar: expect.any(Object),
            desctypevariable: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });

      it('passing IEtypevariable should create a new form with FormGroup', () => {
        const formGroup = service.createEtypevariableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomtypevar: expect.any(Object),
            desctypevariable: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });
    });

    describe('getEtypevariable', () => {
      it('should return NewEtypevariable for default Etypevariable initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEtypevariableFormGroup(sampleWithNewData);

        const etypevariable = service.getEtypevariable(formGroup) as any;

        expect(etypevariable).toMatchObject(sampleWithNewData);
      });

      it('should return NewEtypevariable for empty Etypevariable initial value', () => {
        const formGroup = service.createEtypevariableFormGroup();

        const etypevariable = service.getEtypevariable(formGroup) as any;

        expect(etypevariable).toMatchObject({});
      });

      it('should return IEtypevariable', () => {
        const formGroup = service.createEtypevariableFormGroup(sampleWithRequiredData);

        const etypevariable = service.getEtypevariable(formGroup) as any;

        expect(etypevariable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEtypevariable should not enable id FormControl', () => {
        const formGroup = service.createEtypevariableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEtypevariable should disable id FormControl', () => {
        const formGroup = service.createEtypevariableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
