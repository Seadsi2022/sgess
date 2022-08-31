import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../eeventualite.test-samples';

import { EeventualiteFormService } from './eeventualite-form.service';

describe('Eeventualite Form Service', () => {
  let service: EeventualiteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EeventualiteFormService);
  });

  describe('Service methods', () => {
    describe('createEeventualiteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEeventualiteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventualitevalue: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });

      it('passing IEeventualite should create a new form with FormGroup', () => {
        const formGroup = service.createEeventualiteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            eventualitevalue: expect.any(Object),
            isActive: expect.any(Object),
          })
        );
      });
    });

    describe('getEeventualite', () => {
      it('should return NewEeventualite for default Eeventualite initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEeventualiteFormGroup(sampleWithNewData);

        const eeventualite = service.getEeventualite(formGroup) as any;

        expect(eeventualite).toMatchObject(sampleWithNewData);
      });

      it('should return NewEeventualite for empty Eeventualite initial value', () => {
        const formGroup = service.createEeventualiteFormGroup();

        const eeventualite = service.getEeventualite(formGroup) as any;

        expect(eeventualite).toMatchObject({});
      });

      it('should return IEeventualite', () => {
        const formGroup = service.createEeventualiteFormGroup(sampleWithRequiredData);

        const eeventualite = service.getEeventualite(formGroup) as any;

        expect(eeventualite).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEeventualite should not enable id FormControl', () => {
        const formGroup = service.createEeventualiteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEeventualite should disable id FormControl', () => {
        const formGroup = service.createEeventualiteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
