import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../eattribut.test-samples';

import { EattributFormService } from './eattribut-form.service';

describe('Eattribut Form Service', () => {
  let service: EattributFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EattributFormService);
  });

  describe('Service methods', () => {
    describe('createEattributFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEattributFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            attrname: expect.any(Object),
            attrdisplayname: expect.any(Object),
            evaleurattribut: expect.any(Object),
            etypechamps: expect.any(Object),
          })
        );
      });

      it('passing IEattribut should create a new form with FormGroup', () => {
        const formGroup = service.createEattributFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            attrname: expect.any(Object),
            attrdisplayname: expect.any(Object),
            evaleurattribut: expect.any(Object),
            etypechamps: expect.any(Object),
          })
        );
      });
    });

    describe('getEattribut', () => {
      it('should return NewEattribut for default Eattribut initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEattributFormGroup(sampleWithNewData);

        const eattribut = service.getEattribut(formGroup) as any;

        expect(eattribut).toMatchObject(sampleWithNewData);
      });

      it('should return NewEattribut for empty Eattribut initial value', () => {
        const formGroup = service.createEattributFormGroup();

        const eattribut = service.getEattribut(formGroup) as any;

        expect(eattribut).toMatchObject({});
      });

      it('should return IEattribut', () => {
        const formGroup = service.createEattributFormGroup(sampleWithRequiredData);

        const eattribut = service.getEattribut(formGroup) as any;

        expect(eattribut).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEattribut should not enable id FormControl', () => {
        const formGroup = service.createEattributFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEattribut should disable id FormControl', () => {
        const formGroup = service.createEattributFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
