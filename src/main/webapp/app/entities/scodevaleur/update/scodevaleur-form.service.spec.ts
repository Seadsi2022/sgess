import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../scodevaleur.test-samples';

import { ScodevaleurFormService } from './scodevaleur-form.service';

describe('Scodevaleur Form Service', () => {
  let service: ScodevaleurFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ScodevaleurFormService);
  });

  describe('Service methods', () => {
    describe('createScodevaleurFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createScodevaleurFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codevaleurLib: expect.any(Object),
            parent: expect.any(Object),
            scode: expect.any(Object),
            sstructures: expect.any(Object),
          })
        );
      });

      it('passing IScodevaleur should create a new form with FormGroup', () => {
        const formGroup = service.createScodevaleurFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            codevaleurLib: expect.any(Object),
            parent: expect.any(Object),
            scode: expect.any(Object),
            sstructures: expect.any(Object),
          })
        );
      });
    });

    describe('getScodevaleur', () => {
      it('should return NewScodevaleur for default Scodevaleur initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createScodevaleurFormGroup(sampleWithNewData);

        const scodevaleur = service.getScodevaleur(formGroup) as any;

        expect(scodevaleur).toMatchObject(sampleWithNewData);
      });

      it('should return NewScodevaleur for empty Scodevaleur initial value', () => {
        const formGroup = service.createScodevaleurFormGroup();

        const scodevaleur = service.getScodevaleur(formGroup) as any;

        expect(scodevaleur).toMatchObject({});
      });

      it('should return IScodevaleur', () => {
        const formGroup = service.createScodevaleurFormGroup(sampleWithRequiredData);

        const scodevaleur = service.getScodevaleur(formGroup) as any;

        expect(scodevaleur).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IScodevaleur should not enable id FormControl', () => {
        const formGroup = service.createScodevaleurFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewScodevaleur should disable id FormControl', () => {
        const formGroup = service.createScodevaleurFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
