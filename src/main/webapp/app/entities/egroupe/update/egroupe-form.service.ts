import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEgroupe, NewEgroupe } from '../egroupe.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEgroupe for edit and NewEgroupeFormGroupInput for create.
 */
type EgroupeFormGroupInput = IEgroupe | PartialWithRequiredKeyOf<NewEgroupe>;

type EgroupeFormDefaults = Pick<NewEgroupe, 'id' | 'isActive'>;

type EgroupeFormGroupContent = {
  id: FormControl<IEgroupe['id'] | NewEgroupe['id']>;
  titregroupe: FormControl<IEgroupe['titregroupe']>;
  ordregroupe: FormControl<IEgroupe['ordregroupe']>;
  isActive: FormControl<IEgroupe['isActive']>;
  equestionnaire: FormControl<IEgroupe['equestionnaire']>;
};

export type EgroupeFormGroup = FormGroup<EgroupeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EgroupeFormService {
  createEgroupeFormGroup(egroupe: EgroupeFormGroupInput = { id: null }): EgroupeFormGroup {
    const egroupeRawValue = {
      ...this.getFormDefaults(),
      ...egroupe,
    };
    return new FormGroup<EgroupeFormGroupContent>({
      id: new FormControl(
        { value: egroupeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      titregroupe: new FormControl(egroupeRawValue.titregroupe),
      ordregroupe: new FormControl(egroupeRawValue.ordregroupe),
      isActive: new FormControl(egroupeRawValue.isActive),
      equestionnaire: new FormControl(egroupeRawValue.equestionnaire),
    });
  }

  getEgroupe(form: EgroupeFormGroup): IEgroupe | NewEgroupe {
    return form.getRawValue() as IEgroupe | NewEgroupe;
  }

  resetForm(form: EgroupeFormGroup, egroupe: EgroupeFormGroupInput): void {
    const egroupeRawValue = { ...this.getFormDefaults(), ...egroupe };
    form.reset(
      {
        ...egroupeRawValue,
        id: { value: egroupeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EgroupeFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
