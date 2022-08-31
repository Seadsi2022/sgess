import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../sstructure.test-samples';

import { SstructureFormService } from './sstructure-form.service';

describe('Sstructure Form Service', () => {
  let service: SstructureFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SstructureFormService);
  });

  describe('Service methods', () => {
    describe('createSstructureFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSstructureFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomstructure: expect.any(Object),
            siglestructure: expect.any(Object),
            telstructure: expect.any(Object),
            bpstructure: expect.any(Object),
            emailstructure: expect.any(Object),
            profondeur: expect.any(Object),
            parent: expect.any(Object),
            scodes: expect.any(Object),
            slocalite: expect.any(Object),
          })
        );
      });

      it('passing ISstructure should create a new form with FormGroup', () => {
        const formGroup = service.createSstructureFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomstructure: expect.any(Object),
            siglestructure: expect.any(Object),
            telstructure: expect.any(Object),
            bpstructure: expect.any(Object),
            emailstructure: expect.any(Object),
            profondeur: expect.any(Object),
            parent: expect.any(Object),
            scodes: expect.any(Object),
            slocalite: expect.any(Object),
          })
        );
      });
    });

    describe('getSstructure', () => {
      it('should return NewSstructure for default Sstructure initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSstructureFormGroup(sampleWithNewData);

        const sstructure = service.getSstructure(formGroup) as any;

        expect(sstructure).toMatchObject(sampleWithNewData);
      });

      it('should return NewSstructure for empty Sstructure initial value', () => {
        const formGroup = service.createSstructureFormGroup();

        const sstructure = service.getSstructure(formGroup) as any;

        expect(sstructure).toMatchObject({});
      });

      it('should return ISstructure', () => {
        const formGroup = service.createSstructureFormGroup(sampleWithRequiredData);

        const sstructure = service.getSstructure(formGroup) as any;

        expect(sstructure).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISstructure should not enable id FormControl', () => {
        const formGroup = service.createSstructureFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSstructure should disable id FormControl', () => {
        const formGroup = service.createSstructureFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
