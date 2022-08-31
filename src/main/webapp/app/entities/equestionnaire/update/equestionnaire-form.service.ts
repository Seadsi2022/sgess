import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEquestionnaire, NewEquestionnaire } from '../equestionnaire.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEquestionnaire for edit and NewEquestionnaireFormGroupInput for create.
 */
type EquestionnaireFormGroupInput = IEquestionnaire | PartialWithRequiredKeyOf<NewEquestionnaire>;

type EquestionnaireFormDefaults = Pick<NewEquestionnaire, 'id' | 'isActive'>;

type EquestionnaireFormGroupContent = {
  id: FormControl<IEquestionnaire['id'] | NewEquestionnaire['id']>;
  objetquest: FormControl<IEquestionnaire['objetquest']>;
  descriptionquest: FormControl<IEquestionnaire['descriptionquest']>;
  isActive: FormControl<IEquestionnaire['isActive']>;
  parent: FormControl<IEquestionnaire['parent']>;
  ecampagne: FormControl<IEquestionnaire['ecampagne']>;
  typestructure: FormControl<IEquestionnaire['typestructure']>;
};

export type EquestionnaireFormGroup = FormGroup<EquestionnaireFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EquestionnaireFormService {
  createEquestionnaireFormGroup(equestionnaire: EquestionnaireFormGroupInput = { id: null }): EquestionnaireFormGroup {
    const equestionnaireRawValue = {
      ...this.getFormDefaults(),
      ...equestionnaire,
    };
    return new FormGroup<EquestionnaireFormGroupContent>({
      id: new FormControl(
        { value: equestionnaireRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      objetquest: new FormControl(equestionnaireRawValue.objetquest),
      descriptionquest: new FormControl(equestionnaireRawValue.descriptionquest),
      isActive: new FormControl(equestionnaireRawValue.isActive),
      parent: new FormControl(equestionnaireRawValue.parent),
      ecampagne: new FormControl(equestionnaireRawValue.ecampagne),
      typestructure: new FormControl(equestionnaireRawValue.typestructure),
    });
  }

  getEquestionnaire(form: EquestionnaireFormGroup): IEquestionnaire | NewEquestionnaire {
    return form.getRawValue() as IEquestionnaire | NewEquestionnaire;
  }

  resetForm(form: EquestionnaireFormGroup, equestionnaire: EquestionnaireFormGroupInput): void {
    const equestionnaireRawValue = { ...this.getFormDefaults(), ...equestionnaire };
    form.reset(
      {
        ...equestionnaireRawValue,
        id: { value: equestionnaireRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EquestionnaireFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
