import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../egroupe.test-samples';

import { EgroupeFormService } from './egroupe-form.service';

describe('Egroupe Form Service', () => {
  let service: EgroupeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EgroupeFormService);
  });

  describe('Service methods', () => {
    describe('createEgroupeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEgroupeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titregroupe: expect.any(Object),
            ordregroupe: expect.any(Object),
            isActive: expect.any(Object),
            equestionnaire: expect.any(Object),
          })
        );
      });

      it('passing IEgroupe should create a new form with FormGroup', () => {
        const formGroup = service.createEgroupeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            titregroupe: expect.any(Object),
            ordregroupe: expect.any(Object),
            isActive: expect.any(Object),
            equestionnaire: expect.any(Object),
          })
        );
      });
    });

    describe('getEgroupe', () => {
      it('should return NewEgroupe for default Egroupe initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEgroupeFormGroup(sampleWithNewData);

        const egroupe = service.getEgroupe(formGroup) as any;

        expect(egroupe).toMatchObject(sampleWithNewData);
      });

      it('should return NewEgroupe for empty Egroupe initial value', () => {
        const formGroup = service.createEgroupeFormGroup();

        const egroupe = service.getEgroupe(formGroup) as any;

        expect(egroupe).toMatchObject({});
      });

      it('should return IEgroupe', () => {
        const formGroup = service.createEgroupeFormGroup(sampleWithRequiredData);

        const egroupe = service.getEgroupe(formGroup) as any;

        expect(egroupe).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEgroupe should not enable id FormControl', () => {
        const formGroup = service.createEgroupeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEgroupe should disable id FormControl', () => {
        const formGroup = service.createEgroupeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
