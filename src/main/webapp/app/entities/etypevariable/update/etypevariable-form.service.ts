import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEtypevariable, NewEtypevariable } from '../etypevariable.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEtypevariable for edit and NewEtypevariableFormGroupInput for create.
 */
type EtypevariableFormGroupInput = IEtypevariable | PartialWithRequiredKeyOf<NewEtypevariable>;

type EtypevariableFormDefaults = Pick<NewEtypevariable, 'id' | 'isActive'>;

type EtypevariableFormGroupContent = {
  id: FormControl<IEtypevariable['id'] | NewEtypevariable['id']>;
  nomtypevar: FormControl<IEtypevariable['nomtypevar']>;
  desctypevariable: FormControl<IEtypevariable['desctypevariable']>;
  isActive: FormControl<IEtypevariable['isActive']>;
};

export type EtypevariableFormGroup = FormGroup<EtypevariableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EtypevariableFormService {
  createEtypevariableFormGroup(etypevariable: EtypevariableFormGroupInput = { id: null }): EtypevariableFormGroup {
    const etypevariableRawValue = {
      ...this.getFormDefaults(),
      ...etypevariable,
    };
    return new FormGroup<EtypevariableFormGroupContent>({
      id: new FormControl(
        { value: etypevariableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomtypevar: new FormControl(etypevariableRawValue.nomtypevar),
      desctypevariable: new FormControl(etypevariableRawValue.desctypevariable),
      isActive: new FormControl(etypevariableRawValue.isActive),
    });
  }

  getEtypevariable(form: EtypevariableFormGroup): IEtypevariable | NewEtypevariable {
    return form.getRawValue() as IEtypevariable | NewEtypevariable;
  }

  resetForm(form: EtypevariableFormGroup, etypevariable: EtypevariableFormGroupInput): void {
    const etypevariableRawValue = { ...this.getFormDefaults(), ...etypevariable };
    form.reset(
      {
        ...etypevariableRawValue,
        id: { value: etypevariableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EtypevariableFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
