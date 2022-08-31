import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IScode, NewScode } from '../scode.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IScode for edit and NewScodeFormGroupInput for create.
 */
type ScodeFormGroupInput = IScode | PartialWithRequiredKeyOf<NewScode>;

type ScodeFormDefaults = Pick<NewScode, 'id'>;

type ScodeFormGroupContent = {
  id: FormControl<IScode['id'] | NewScode['id']>;
  codeLib: FormControl<IScode['codeLib']>;
};

export type ScodeFormGroup = FormGroup<ScodeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ScodeFormService {
  createScodeFormGroup(scode: ScodeFormGroupInput = { id: null }): ScodeFormGroup {
    const scodeRawValue = {
      ...this.getFormDefaults(),
      ...scode,
    };
    return new FormGroup<ScodeFormGroupContent>({
      id: new FormControl(
        { value: scodeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      codeLib: new FormControl(scodeRawValue.codeLib),
    });
  }

  getScode(form: ScodeFormGroup): IScode | NewScode {
    return form.getRawValue() as IScode | NewScode;
  }

  resetForm(form: ScodeFormGroup, scode: ScodeFormGroupInput): void {
    const scodeRawValue = { ...this.getFormDefaults(), ...scode };
    form.reset(
      {
        ...scodeRawValue,
        id: { value: scodeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ScodeFormDefaults {
    return {
      id: null,
    };
  }
}
