import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../ecampagne.test-samples';

import { EcampagneFormService } from './ecampagne-form.service';

describe('Ecampagne Form Service', () => {
  let service: EcampagneFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EcampagneFormService);
  });

  describe('Service methods', () => {
    describe('createEcampagneFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEcampagneFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            objetcampagne: expect.any(Object),
            description: expect.any(Object),
            debutcampagne: expect.any(Object),
            fincampagne: expect.any(Object),
            debutreelcamp: expect.any(Object),
            finreelcamp: expect.any(Object),
            isopen: expect.any(Object),
          })
        );
      });

      it('passing IEcampagne should create a new form with FormGroup', () => {
        const formGroup = service.createEcampagneFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            objetcampagne: expect.any(Object),
            description: expect.any(Object),
            debutcampagne: expect.any(Object),
            fincampagne: expect.any(Object),
            debutreelcamp: expect.any(Object),
            finreelcamp: expect.any(Object),
            isopen: expect.any(Object),
          })
        );
      });
    });

    describe('getEcampagne', () => {
      it('should return NewEcampagne for default Ecampagne initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEcampagneFormGroup(sampleWithNewData);

        const ecampagne = service.getEcampagne(formGroup) as any;

        expect(ecampagne).toMatchObject(sampleWithNewData);
      });

      it('should return NewEcampagne for empty Ecampagne initial value', () => {
        const formGroup = service.createEcampagneFormGroup();

        const ecampagne = service.getEcampagne(formGroup) as any;

        expect(ecampagne).toMatchObject({});
      });

      it('should return IEcampagne', () => {
        const formGroup = service.createEcampagneFormGroup(sampleWithRequiredData);

        const ecampagne = service.getEcampagne(formGroup) as any;

        expect(ecampagne).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEcampagne should not enable id FormControl', () => {
        const formGroup = service.createEcampagneFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEcampagne should disable id FormControl', () => {
        const formGroup = service.createEcampagneFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
