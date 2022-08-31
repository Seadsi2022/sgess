import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../etypechamp.test-samples';

import { EtypechampFormService } from './etypechamp-form.service';

describe('Etypechamp Form Service', () => {
  let service: EtypechampFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EtypechampFormService);
  });

  describe('Service methods', () => {
    describe('createEtypechampFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEtypechampFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            displayname: expect.any(Object),
            eattributs: expect.any(Object),
          })
        );
      });

      it('passing IEtypechamp should create a new form with FormGroup', () => {
        const formGroup = service.createEtypechampFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            name: expect.any(Object),
            displayname: expect.any(Object),
            eattributs: expect.any(Object),
          })
        );
      });
    });

    describe('getEtypechamp', () => {
      it('should return NewEtypechamp for default Etypechamp initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEtypechampFormGroup(sampleWithNewData);

        const etypechamp = service.getEtypechamp(formGroup) as any;

        expect(etypechamp).toMatchObject(sampleWithNewData);
      });

      it('should return NewEtypechamp for empty Etypechamp initial value', () => {
        const formGroup = service.createEtypechampFormGroup();

        const etypechamp = service.getEtypechamp(formGroup) as any;

        expect(etypechamp).toMatchObject({});
      });

      it('should return IEtypechamp', () => {
        const formGroup = service.createEtypechampFormGroup(sampleWithRequiredData);

        const etypechamp = service.getEtypechamp(formGroup) as any;

        expect(etypechamp).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEtypechamp should not enable id FormControl', () => {
        const formGroup = service.createEtypechampFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEtypechamp should disable id FormControl', () => {
        const formGroup = service.createEtypechampFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
