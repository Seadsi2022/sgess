import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../equestionnaire.test-samples';

import { EquestionnaireFormService } from './equestionnaire-form.service';

describe('Equestionnaire Form Service', () => {
  let service: EquestionnaireFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EquestionnaireFormService);
  });

  describe('Service methods', () => {
    describe('createEquestionnaireFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEquestionnaireFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            objetquest: expect.any(Object),
            descriptionquest: expect.any(Object),
            isActive: expect.any(Object),
            parent: expect.any(Object),
            ecampagne: expect.any(Object),
            typestructure: expect.any(Object),
          })
        );
      });

      it('passing IEquestionnaire should create a new form with FormGroup', () => {
        const formGroup = service.createEquestionnaireFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            objetquest: expect.any(Object),
            descriptionquest: expect.any(Object),
            isActive: expect.any(Object),
            parent: expect.any(Object),
            ecampagne: expect.any(Object),
            typestructure: expect.any(Object),
          })
        );
      });
    });

    describe('getEquestionnaire', () => {
      it('should return NewEquestionnaire for default Equestionnaire initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEquestionnaireFormGroup(sampleWithNewData);

        const equestionnaire = service.getEquestionnaire(formGroup) as any;

        expect(equestionnaire).toMatchObject(sampleWithNewData);
      });

      it('should return NewEquestionnaire for empty Equestionnaire initial value', () => {
        const formGroup = service.createEquestionnaireFormGroup();

        const equestionnaire = service.getEquestionnaire(formGroup) as any;

        expect(equestionnaire).toMatchObject({});
      });

      it('should return IEquestionnaire', () => {
        const formGroup = service.createEquestionnaireFormGroup(sampleWithRequiredData);

        const equestionnaire = service.getEquestionnaire(formGroup) as any;

        expect(equestionnaire).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEquestionnaire should not enable id FormControl', () => {
        const formGroup = service.createEquestionnaireFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEquestionnaire should disable id FormControl', () => {
        const formGroup = service.createEquestionnaireFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
