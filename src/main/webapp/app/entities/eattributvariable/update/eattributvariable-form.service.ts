import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEattributvariable, NewEattributvariable } from '../eattributvariable.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEattributvariable for edit and NewEattributvariableFormGroupInput for create.
 */
type EattributvariableFormGroupInput = IEattributvariable | PartialWithRequiredKeyOf<NewEattributvariable>;

type EattributvariableFormDefaults = Pick<NewEattributvariable, 'id' | 'isActive'>;

type EattributvariableFormGroupContent = {
  id: FormControl<IEattributvariable['id'] | NewEattributvariable['id']>;
  attrname: FormControl<IEattributvariable['attrname']>;
  attrvalue: FormControl<IEattributvariable['attrvalue']>;
  isActive: FormControl<IEattributvariable['isActive']>;
  evariable: FormControl<IEattributvariable['evariable']>;
};

export type EattributvariableFormGroup = FormGroup<EattributvariableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EattributvariableFormService {
  createEattributvariableFormGroup(eattributvariable: EattributvariableFormGroupInput = { id: null }): EattributvariableFormGroup {
    const eattributvariableRawValue = {
      ...this.getFormDefaults(),
      ...eattributvariable,
    };
    return new FormGroup<EattributvariableFormGroupContent>({
      id: new FormControl(
        { value: eattributvariableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      attrname: new FormControl(eattributvariableRawValue.attrname),
      attrvalue: new FormControl(eattributvariableRawValue.attrvalue),
      isActive: new FormControl(eattributvariableRawValue.isActive),
      evariable: new FormControl(eattributvariableRawValue.evariable),
    });
  }

  getEattributvariable(form: EattributvariableFormGroup): IEattributvariable | NewEattributvariable {
    return form.getRawValue() as IEattributvariable | NewEattributvariable;
  }

  resetForm(form: EattributvariableFormGroup, eattributvariable: EattributvariableFormGroupInput): void {
    const eattributvariableRawValue = { ...this.getFormDefaults(), ...eattributvariable };
    form.reset(
      {
        ...eattributvariableRawValue,
        id: { value: eattributvariableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EattributvariableFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
