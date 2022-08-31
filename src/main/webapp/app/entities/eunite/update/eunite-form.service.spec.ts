import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../eunite.test-samples';

import { EuniteFormService } from './eunite-form.service';

describe('Eunite Form Service', () => {
  let service: EuniteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EuniteFormService);
  });

  describe('Service methods', () => {
    describe('createEuniteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEuniteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomunite: expect.any(Object),
            symboleunite: expect.any(Object),
            facteur: expect.any(Object),
            unitebase: expect.any(Object),
          })
        );
      });

      it('passing IEunite should create a new form with FormGroup', () => {
        const formGroup = service.createEuniteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomunite: expect.any(Object),
            symboleunite: expect.any(Object),
            facteur: expect.any(Object),
            unitebase: expect.any(Object),
          })
        );
      });
    });

    describe('getEunite', () => {
      it('should return NewEunite for default Eunite initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEuniteFormGroup(sampleWithNewData);

        const eunite = service.getEunite(formGroup) as any;

        expect(eunite).toMatchObject(sampleWithNewData);
      });

      it('should return NewEunite for empty Eunite initial value', () => {
        const formGroup = service.createEuniteFormGroup();

        const eunite = service.getEunite(formGroup) as any;

        expect(eunite).toMatchObject({});
      });

      it('should return IEunite', () => {
        const formGroup = service.createEuniteFormGroup(sampleWithRequiredData);

        const eunite = service.getEunite(formGroup) as any;

        expect(eunite).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEunite should not enable id FormControl', () => {
        const formGroup = service.createEuniteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEunite should disable id FormControl', () => {
        const formGroup = service.createEuniteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
