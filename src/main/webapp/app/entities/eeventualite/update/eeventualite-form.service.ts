import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEeventualite, NewEeventualite } from '../eeventualite.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEeventualite for edit and NewEeventualiteFormGroupInput for create.
 */
type EeventualiteFormGroupInput = IEeventualite | PartialWithRequiredKeyOf<NewEeventualite>;

type EeventualiteFormDefaults = Pick<NewEeventualite, 'id' | 'isActive'>;

type EeventualiteFormGroupContent = {
  id: FormControl<IEeventualite['id'] | NewEeventualite['id']>;
  eventualitevalue: FormControl<IEeventualite['eventualitevalue']>;
  isActive: FormControl<IEeventualite['isActive']>;
};

export type EeventualiteFormGroup = FormGroup<EeventualiteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EeventualiteFormService {
  createEeventualiteFormGroup(eeventualite: EeventualiteFormGroupInput = { id: null }): EeventualiteFormGroup {
    const eeventualiteRawValue = {
      ...this.getFormDefaults(),
      ...eeventualite,
    };
    return new FormGroup<EeventualiteFormGroupContent>({
      id: new FormControl(
        { value: eeventualiteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      eventualitevalue: new FormControl(eeventualiteRawValue.eventualitevalue),
      isActive: new FormControl(eeventualiteRawValue.isActive),
    });
  }

  getEeventualite(form: EeventualiteFormGroup): IEeventualite | NewEeventualite {
    return form.getRawValue() as IEeventualite | NewEeventualite;
  }

  resetForm(form: EeventualiteFormGroup, eeventualite: EeventualiteFormGroupInput): void {
    const eeventualiteRawValue = { ...this.getFormDefaults(), ...eeventualite };
    form.reset(
      {
        ...eeventualiteRawValue,
        id: { value: eeventualiteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EeventualiteFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
