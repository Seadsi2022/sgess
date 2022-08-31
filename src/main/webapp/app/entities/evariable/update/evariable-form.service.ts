import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEvariable, NewEvariable } from '../evariable.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEvariable for edit and NewEvariableFormGroupInput for create.
 */
type EvariableFormGroupInput = IEvariable | PartialWithRequiredKeyOf<NewEvariable>;

type EvariableFormDefaults = Pick<NewEvariable, 'id' | 'isActive'>;

type EvariableFormGroupContent = {
  id: FormControl<IEvariable['id'] | NewEvariable['id']>;
  nomvariable: FormControl<IEvariable['nomvariable']>;
  descvariable: FormControl<IEvariable['descvariable']>;
  champ: FormControl<IEvariable['champ']>;
  isActive: FormControl<IEvariable['isActive']>;
  etypevariable: FormControl<IEvariable['etypevariable']>;
  eunite: FormControl<IEvariable['eunite']>;
};

export type EvariableFormGroup = FormGroup<EvariableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EvariableFormService {
  createEvariableFormGroup(evariable: EvariableFormGroupInput = { id: null }): EvariableFormGroup {
    const evariableRawValue = {
      ...this.getFormDefaults(),
      ...evariable,
    };
    return new FormGroup<EvariableFormGroupContent>({
      id: new FormControl(
        { value: evariableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomvariable: new FormControl(evariableRawValue.nomvariable),
      descvariable: new FormControl(evariableRawValue.descvariable),
      champ: new FormControl(evariableRawValue.champ),
      isActive: new FormControl(evariableRawValue.isActive),
      etypevariable: new FormControl(evariableRawValue.etypevariable),
      eunite: new FormControl(evariableRawValue.eunite),
    });
  }

  getEvariable(form: EvariableFormGroup): IEvariable | NewEvariable {
    return form.getRawValue() as IEvariable | NewEvariable;
  }

  resetForm(form: EvariableFormGroup, evariable: EvariableFormGroupInput): void {
    const evariableRawValue = { ...this.getFormDefaults(), ...evariable };
    form.reset(
      {
        ...evariableRawValue,
        id: { value: evariableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EvariableFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
