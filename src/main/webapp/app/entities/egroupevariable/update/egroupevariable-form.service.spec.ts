import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../egroupevariable.test-samples';

import { EgroupevariableFormService } from './egroupevariable-form.service';

describe('Egroupevariable Form Service', () => {
  let service: EgroupevariableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EgroupevariableFormService);
  });

  describe('Service methods', () => {
    describe('createEgroupevariableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEgroupevariableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ordrevariable: expect.any(Object),
            isActive: expect.any(Object),
            suivant: expect.any(Object),
            evariable: expect.any(Object),
            egroupe: expect.any(Object),
          })
        );
      });

      it('passing IEgroupevariable should create a new form with FormGroup', () => {
        const formGroup = service.createEgroupevariableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            ordrevariable: expect.any(Object),
            isActive: expect.any(Object),
            suivant: expect.any(Object),
            evariable: expect.any(Object),
            egroupe: expect.any(Object),
          })
        );
      });
    });

    describe('getEgroupevariable', () => {
      it('should return NewEgroupevariable for default Egroupevariable initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEgroupevariableFormGroup(sampleWithNewData);

        const egroupevariable = service.getEgroupevariable(formGroup) as any;

        expect(egroupevariable).toMatchObject(sampleWithNewData);
      });

      it('should return NewEgroupevariable for empty Egroupevariable initial value', () => {
        const formGroup = service.createEgroupevariableFormGroup();

        const egroupevariable = service.getEgroupevariable(formGroup) as any;

        expect(egroupevariable).toMatchObject({});
      });

      it('should return IEgroupevariable', () => {
        const formGroup = service.createEgroupevariableFormGroup(sampleWithRequiredData);

        const egroupevariable = service.getEgroupevariable(formGroup) as any;

        expect(egroupevariable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEgroupevariable should not enable id FormControl', () => {
        const formGroup = service.createEgroupevariableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEgroupevariable should disable id FormControl', () => {
        const formGroup = service.createEgroupevariableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
