import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../scode.test-samples';

import { ScodeFormService } from './scode-form.service';

describe('Scode Form Service', () => {
  let service: ScodeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ScodeFormService);
  });

  describe('Service methods', () => {
    describe('createScodeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createScodeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeLib: expect.any(Object),
          })
        );
      });

      it('passing IScode should create a new form with FormGroup', () => {
        const formGroup = service.createScodeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codeLib: expect.any(Object),
          })
        );
      });
    });

    describe('getScode', () => {
      it('should return NewScode for default Scode initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createScodeFormGroup(sampleWithNewData);

        const scode = service.getScode(formGroup) as any;

        expect(scode).toMatchObject(sampleWithNewData);
      });

      it('should return NewScode for empty Scode initial value', () => {
        const formGroup = service.createScodeFormGroup();

        const scode = service.getScode(formGroup) as any;

        expect(scode).toMatchObject({});
      });

      it('should return IScode', () => {
        const formGroup = service.createScodeFormGroup(sampleWithRequiredData);

        const scode = service.getScode(formGroup) as any;

        expect(scode).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IScode should not enable id FormControl', () => {
        const formGroup = service.createScodeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewScode should disable id FormControl', () => {
        const formGroup = service.createScodeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
