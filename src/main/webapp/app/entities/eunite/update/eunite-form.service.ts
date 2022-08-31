import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEunite, NewEunite } from '../eunite.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEunite for edit and NewEuniteFormGroupInput for create.
 */
type EuniteFormGroupInput = IEunite | PartialWithRequiredKeyOf<NewEunite>;

type EuniteFormDefaults = Pick<NewEunite, 'id'>;

type EuniteFormGroupContent = {
  id: FormControl<IEunite['id'] | NewEunite['id']>;
  nomunite: FormControl<IEunite['nomunite']>;
  symboleunite: FormControl<IEunite['symboleunite']>;
  facteur: FormControl<IEunite['facteur']>;
  unitebase: FormControl<IEunite['unitebase']>;
};

export type EuniteFormGroup = FormGroup<EuniteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EuniteFormService {
  createEuniteFormGroup(eunite: EuniteFormGroupInput = { id: null }): EuniteFormGroup {
    const euniteRawValue = {
      ...this.getFormDefaults(),
      ...eunite,
    };
    return new FormGroup<EuniteFormGroupContent>({
      id: new FormControl(
        { value: euniteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomunite: new FormControl(euniteRawValue.nomunite),
      symboleunite: new FormControl(euniteRawValue.symboleunite),
      facteur: new FormControl(euniteRawValue.facteur),
      unitebase: new FormControl(euniteRawValue.unitebase),
    });
  }

  getEunite(form: EuniteFormGroup): IEunite | NewEunite {
    return form.getRawValue() as IEunite | NewEunite;
  }

  resetForm(form: EuniteFormGroup, eunite: EuniteFormGroupInput): void {
    const euniteRawValue = { ...this.getFormDefaults(), ...eunite };
    form.reset(
      {
        ...euniteRawValue,
        id: { value: euniteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EuniteFormDefaults {
    return {
      id: null,
    };
  }
}
