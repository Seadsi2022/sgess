import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISetablissement, NewSetablissement } from '../setablissement.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISetablissement for edit and NewSetablissementFormGroupInput for create.
 */
type SetablissementFormGroupInput = ISetablissement | PartialWithRequiredKeyOf<NewSetablissement>;

type SetablissementFormDefaults = Pick<NewSetablissement, 'id'>;

type SetablissementFormGroupContent = {
  id: FormControl<ISetablissement['id'] | NewSetablissement['id']>;
  codeadministratif: FormControl<ISetablissement['codeadministratif']>;
  sstructure: FormControl<ISetablissement['sstructure']>;
};

export type SetablissementFormGroup = FormGroup<SetablissementFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SetablissementFormService {
  createSetablissementFormGroup(setablissement: SetablissementFormGroupInput = { id: null }): SetablissementFormGroup {
    const setablissementRawValue = {
      ...this.getFormDefaults(),
      ...setablissement,
    };
    return new FormGroup<SetablissementFormGroupContent>({
      id: new FormControl(
        { value: setablissementRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      codeadministratif: new FormControl(setablissementRawValue.codeadministratif),
      sstructure: new FormControl(setablissementRawValue.sstructure),
    });
  }

  getSetablissement(form: SetablissementFormGroup): ISetablissement | NewSetablissement {
    return form.getRawValue() as ISetablissement | NewSetablissement;
  }

  resetForm(form: SetablissementFormGroup, setablissement: SetablissementFormGroupInput): void {
    const setablissementRawValue = { ...this.getFormDefaults(), ...setablissement };
    form.reset(
      {
        ...setablissementRawValue,
        id: { value: setablissementRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SetablissementFormDefaults {
    return {
      id: null,
    };
  }
}
