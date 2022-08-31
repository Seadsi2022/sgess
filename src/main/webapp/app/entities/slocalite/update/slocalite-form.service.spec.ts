import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../slocalite.test-samples';

import { SlocaliteFormService } from './slocalite-form.service';

describe('Slocalite Form Service', () => {
  let service: SlocaliteFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SlocaliteFormService);
  });

  describe('Service methods', () => {
    describe('createSlocaliteFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createSlocaliteFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codelocalite: expect.any(Object),
            nomlocalite: expect.any(Object),
            parent: expect.any(Object),
            natureLocalite: expect.any(Object),
            typeLocalite: expect.any(Object),
          })
        );
      });

      it('passing ISlocalite should create a new form with FormGroup', () => {
        const formGroup = service.createSlocaliteFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codelocalite: expect.any(Object),
            nomlocalite: expect.any(Object),
            parent: expect.any(Object),
            natureLocalite: expect.any(Object),
            typeLocalite: expect.any(Object),
          })
        );
      });
    });

    describe('getSlocalite', () => {
      it('should return NewSlocalite for default Slocalite initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createSlocaliteFormGroup(sampleWithNewData);

        const slocalite = service.getSlocalite(formGroup) as any;

        expect(slocalite).toMatchObject(sampleWithNewData);
      });

      it('should return NewSlocalite for empty Slocalite initial value', () => {
        const formGroup = service.createSlocaliteFormGroup();

        const slocalite = service.getSlocalite(formGroup) as any;

        expect(slocalite).toMatchObject({});
      });

      it('should return ISlocalite', () => {
        const formGroup = service.createSlocaliteFormGroup(sampleWithRequiredData);

        const slocalite = service.getSlocalite(formGroup) as any;

        expect(slocalite).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ISlocalite should not enable id FormControl', () => {
        const formGroup = service.createSlocaliteFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewSlocalite should disable id FormControl', () => {
        const formGroup = service.createSlocaliteFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
