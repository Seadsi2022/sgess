import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEtypechamp, NewEtypechamp } from '../etypechamp.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEtypechamp for edit and NewEtypechampFormGroupInput for create.
 */
type EtypechampFormGroupInput = IEtypechamp | PartialWithRequiredKeyOf<NewEtypechamp>;

type EtypechampFormDefaults = Pick<NewEtypechamp, 'id' | 'eattributs'>;

type EtypechampFormGroupContent = {
  id: FormControl<IEtypechamp['id'] | NewEtypechamp['id']>;
  name: FormControl<IEtypechamp['name']>;
  displayname: FormControl<IEtypechamp['displayname']>;
  eattributs: FormControl<IEtypechamp['eattributs']>;
};

export type EtypechampFormGroup = FormGroup<EtypechampFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EtypechampFormService {
  createEtypechampFormGroup(etypechamp: EtypechampFormGroupInput = { id: null }): EtypechampFormGroup {
    const etypechampRawValue = {
      ...this.getFormDefaults(),
      ...etypechamp,
    };
    return new FormGroup<EtypechampFormGroupContent>({
      id: new FormControl(
        { value: etypechampRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      name: new FormControl(etypechampRawValue.name),
      displayname: new FormControl(etypechampRawValue.displayname),
      eattributs: new FormControl(etypechampRawValue.eattributs ?? []),
    });
  }

  getEtypechamp(form: EtypechampFormGroup): IEtypechamp | NewEtypechamp {
    return form.getRawValue() as IEtypechamp | NewEtypechamp;
  }

  resetForm(form: EtypechampFormGroup, etypechamp: EtypechampFormGroupInput): void {
    const etypechampRawValue = { ...this.getFormDefaults(), ...etypechamp };
    form.reset(
      {
        ...etypechampRawValue,
        id: { value: etypechampRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EtypechampFormDefaults {
    return {
      id: null,
      eattributs: [],
    };
  }
}
