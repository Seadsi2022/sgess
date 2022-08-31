import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../evariable.test-samples';

import { EvariableFormService } from './evariable-form.service';

describe('Evariable Form Service', () => {
  let service: EvariableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EvariableFormService);
  });

  describe('Service methods', () => {
    describe('createEvariableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEvariableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomvariable: expect.any(Object),
            descvariable: expect.any(Object),
            champ: expect.any(Object),
            isActive: expect.any(Object),
            etypevariable: expect.any(Object),
            eunite: expect.any(Object),
          })
        );
      });

      it('passing IEvariable should create a new form with FormGroup', () => {
        const formGroup = service.createEvariableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            nomvariable: expect.any(Object),
            descvariable: expect.any(Object),
            champ: expect.any(Object),
            isActive: expect.any(Object),
            etypevariable: expect.any(Object),
            eunite: expect.any(Object),
          })
        );
      });
    });

    describe('getEvariable', () => {
      it('should return NewEvariable for default Evariable initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEvariableFormGroup(sampleWithNewData);

        const evariable = service.getEvariable(formGroup) as any;

        expect(evariable).toMatchObject(sampleWithNewData);
      });

      it('should return NewEvariable for empty Evariable initial value', () => {
        const formGroup = service.createEvariableFormGroup();

        const evariable = service.getEvariable(formGroup) as any;

        expect(evariable).toMatchObject({});
      });

      it('should return IEvariable', () => {
        const formGroup = service.createEvariableFormGroup(sampleWithRequiredData);

        const evariable = service.getEvariable(formGroup) as any;

        expect(evariable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEvariable should not enable id FormControl', () => {
        const formGroup = service.createEvariableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEvariable should disable id FormControl', () => {
        const formGroup = service.createEvariableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
