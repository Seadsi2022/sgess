import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../setablissement.test-samples';

import { SetablissementFormService } from './setablissement-form.service';

describe('Setablissement Form Service', () => {
  let service: SetablissementFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SetablissementFormService);
  });

  describe('Service methods', () => {
    describe('createSetablissementFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSetablissementFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeadministratif: expect.any(Object),
            sstructure: expect.any(Object),
          })
        );
      });

      it('passing ISetablissement should create a new form with FormGroup', () => {
        const formGroup = service.createSetablissementFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeadministratif: expect.any(Object),
            sstructure: expect.any(Object),
          })
        );
      });
    });

    describe('getSetablissement', () => {
      it('should return NewSetablissement for default Setablissement initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSetablissementFormGroup(sampleWithNewData);

        const setablissement = service.getSetablissement(formGroup) as any;

        expect(setablissement).toMatchObject(sampleWithNewData);
      });

      it('should return NewSetablissement for empty Setablissement initial value', () => {
        const formGroup = service.createSetablissementFormGroup();

        const setablissement = service.getSetablissement(formGroup) as any;

        expect(setablissement).toMatchObject({});
      });

      it('should return ISetablissement', () => {
        const formGroup = service.createSetablissementFormGroup(sampleWithRequiredData);

        const setablissement = service.getSetablissement(formGroup) as any;

        expect(setablissement).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISetablissement should not enable id FormControl', () => {
        const formGroup = service.createSetablissementFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSetablissement should disable id FormControl', () => {
        const formGroup = service.createSetablissementFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
