import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEgroupevariable, NewEgroupevariable } from '../egroupevariable.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEgroupevariable for edit and NewEgroupevariableFormGroupInput for create.
 */
type EgroupevariableFormGroupInput = IEgroupevariable | PartialWithRequiredKeyOf<NewEgroupevariable>;

type EgroupevariableFormDefaults = Pick<NewEgroupevariable, 'id' | 'isActive'>;

type EgroupevariableFormGroupContent = {
  id: FormControl<IEgroupevariable['id'] | NewEgroupevariable['id']>;
  ordrevariable: FormControl<IEgroupevariable['ordrevariable']>;
  isActive: FormControl<IEgroupevariable['isActive']>;
  suivant: FormControl<IEgroupevariable['suivant']>;
  evariable: FormControl<IEgroupevariable['evariable']>;
  egroupe: FormControl<IEgroupevariable['egroupe']>;
};

export type EgroupevariableFormGroup = FormGroup<EgroupevariableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EgroupevariableFormService {
  createEgroupevariableFormGroup(egroupevariable: EgroupevariableFormGroupInput = { id: null }): EgroupevariableFormGroup {
    const egroupevariableRawValue = {
      ...this.getFormDefaults(),
      ...egroupevariable,
    };
    return new FormGroup<EgroupevariableFormGroupContent>({
      id: new FormControl(
        { value: egroupevariableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      ordrevariable: new FormControl(egroupevariableRawValue.ordrevariable),
      isActive: new FormControl(egroupevariableRawValue.isActive),
      suivant: new FormControl(egroupevariableRawValue.suivant),
      evariable: new FormControl(egroupevariableRawValue.evariable),
      egroupe: new FormControl(egroupevariableRawValue.egroupe),
    });
  }

  getEgroupevariable(form: EgroupevariableFormGroup): IEgroupevariable | NewEgroupevariable {
    return form.getRawValue() as IEgroupevariable | NewEgroupevariable;
  }

  resetForm(form: EgroupevariableFormGroup, egroupevariable: EgroupevariableFormGroupInput): void {
    const egroupevariableRawValue = { ...this.getFormDefaults(), ...egroupevariable };
    form.reset(
      {
        ...egroupevariableRawValue,
        id: { value: egroupevariableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EgroupevariableFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
