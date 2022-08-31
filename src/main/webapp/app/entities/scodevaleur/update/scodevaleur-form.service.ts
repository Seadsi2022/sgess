import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IScodevaleur, NewScodevaleur } from '../scodevaleur.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IScodevaleur for edit and NewScodevaleurFormGroupInput for create.
 */
type ScodevaleurFormGroupInput = IScodevaleur | PartialWithRequiredKeyOf<NewScodevaleur>;

type ScodevaleurFormDefaults = Pick<NewScodevaleur, 'id' | 'sstructures'>;

type ScodevaleurFormGroupContent = {
  id: FormControl<IScodevaleur['id'] | NewScodevaleur['id']>;
  codevaleurLib: FormControl<IScodevaleur['codevaleurLib']>;
  parent: FormControl<IScodevaleur['parent']>;
  scode: FormControl<IScodevaleur['scode']>;
  sstructures: FormControl<IScodevaleur['sstructures']>;
};

export type ScodevaleurFormGroup = FormGroup<ScodevaleurFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ScodevaleurFormService {
  createScodevaleurFormGroup(scodevaleur: ScodevaleurFormGroupInput = { id: null }): ScodevaleurFormGroup {
    const scodevaleurRawValue = {
      ...this.getFormDefaults(),
      ...scodevaleur,
    };
    return new FormGroup<ScodevaleurFormGroupContent>({
      id: new FormControl(
        { value: scodevaleurRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      codevaleurLib: new FormControl(scodevaleurRawValue.codevaleurLib),
      parent: new FormControl(scodevaleurRawValue.parent),
      scode: new FormControl(scodevaleurRawValue.scode),
      sstructures: new FormControl(scodevaleurRawValue.sstructures ?? []),
    });
  }

  getScodevaleur(form: ScodevaleurFormGroup): IScodevaleur | NewScodevaleur {
    return form.getRawValue() as IScodevaleur | NewScodevaleur;
  }

  resetForm(form: ScodevaleurFormGroup, scodevaleur: ScodevaleurFormGroupInput): void {
    const scodevaleurRawValue = { ...this.getFormDefaults(), ...scodevaleur };
    form.reset(
      {
        ...scodevaleurRawValue,
        id: { value: scodevaleurRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ScodevaleurFormDefaults {
    return {
      id: null,
      sstructures: [],
    };
  }
}
