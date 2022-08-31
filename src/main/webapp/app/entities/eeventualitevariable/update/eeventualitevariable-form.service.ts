import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEeventualitevariable, NewEeventualitevariable } from '../eeventualitevariable.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEeventualitevariable for edit and NewEeventualitevariableFormGroupInput for create.
 */
type EeventualitevariableFormGroupInput = IEeventualitevariable | PartialWithRequiredKeyOf<NewEeventualitevariable>;

type EeventualitevariableFormDefaults = Pick<NewEeventualitevariable, 'id'>;

type EeventualitevariableFormGroupContent = {
  id: FormControl<IEeventualitevariable['id'] | NewEeventualitevariable['id']>;
  evariable: FormControl<IEeventualitevariable['evariable']>;
  eeventualite: FormControl<IEeventualitevariable['eeventualite']>;
};

export type EeventualitevariableFormGroup = FormGroup<EeventualitevariableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EeventualitevariableFormService {
  createEeventualitevariableFormGroup(
    eeventualitevariable: EeventualitevariableFormGroupInput = { id: null }
  ): EeventualitevariableFormGroup {
    const eeventualitevariableRawValue = {
      ...this.getFormDefaults(),
      ...eeventualitevariable,
    };
    return new FormGroup<EeventualitevariableFormGroupContent>({
      id: new FormControl(
        { value: eeventualitevariableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      evariable: new FormControl(eeventualitevariableRawValue.evariable),
      eeventualite: new FormControl(eeventualitevariableRawValue.eeventualite),
    });
  }

  getEeventualitevariable(form: EeventualitevariableFormGroup): IEeventualitevariable | NewEeventualitevariable {
    return form.getRawValue() as IEeventualitevariable | NewEeventualitevariable;
  }

  resetForm(form: EeventualitevariableFormGroup, eeventualitevariable: EeventualitevariableFormGroupInput): void {
    const eeventualitevariableRawValue = { ...this.getFormDefaults(), ...eeventualitevariable };
    form.reset(
      {
        ...eeventualitevariableRawValue,
        id: { value: eeventualitevariableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EeventualitevariableFormDefaults {
    return {
      id: null,
    };
  }
}
