import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../eattributvariable.test-samples';

import { EattributvariableFormService } from './eattributvariable-form.service';

describe('Eattributvariable Form Service', () => {
  let service: EattributvariableFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EattributvariableFormService);
  });

  describe('Service methods', () => {
    describe('createEattributvariableFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createEattributvariableFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            attrname: expect.any(Object),
            attrvalue: expect.any(Object),
            isActive: expect.any(Object),
            evariable: expect.any(Object),
          })
        );
      });

      it('passing IEattributvariable should create a new form with FormGroup', () => {
        const formGroup = service.createEattributvariableFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            attrname: expect.any(Object),
            attrvalue: expect.any(Object),
            isActive: expect.any(Object),
            evariable: expect.any(Object),
          })
        );
      });
    });

    describe('getEattributvariable', () => {
      it('should return NewEattributvariable for default Eattributvariable initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createEattributvariableFormGroup(sampleWithNewData);

        const eattributvariable = service.getEattributvariable(formGroup) as any;

        expect(eattributvariable).toMatchObject(sampleWithNewData);
      });

      it('should return NewEattributvariable for empty Eattributvariable initial value', () => {
        const formGroup = service.createEattributvariableFormGroup();

        const eattributvariable = service.getEattributvariable(formGroup) as any;

        expect(eattributvariable).toMatchObject({});
      });

      it('should return IEattributvariable', () => {
        const formGroup = service.createEattributvariableFormGroup(sampleWithRequiredData);

        const eattributvariable = service.getEattributvariable(formGroup) as any;

        expect(eattributvariable).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IEattributvariable should not enable id FormControl', () => {
        const formGroup = service.createEattributvariableFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewEattributvariable should disable id FormControl', () => {
        const formGroup = service.createEattributvariableFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
