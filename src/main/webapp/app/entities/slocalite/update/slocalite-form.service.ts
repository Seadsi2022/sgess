import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISlocalite, NewSlocalite } from '../slocalite.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISlocalite for edit and NewSlocaliteFormGroupInput for create.
 */
type SlocaliteFormGroupInput = ISlocalite | PartialWithRequiredKeyOf<NewSlocalite>;

type SlocaliteFormDefaults = Pick<NewSlocalite, 'id'>;

type SlocaliteFormGroupContent = {
  id: FormControl<ISlocalite['id'] | NewSlocalite['id']>;
  codelocalite: FormControl<ISlocalite['codelocalite']>;
  nomlocalite: FormControl<ISlocalite['nomlocalite']>;
  parent: FormControl<ISlocalite['parent']>;
  natureLocalite: FormControl<ISlocalite['natureLocalite']>;
  typeLocalite: FormControl<ISlocalite['typeLocalite']>;
};

export type SlocaliteFormGroup = FormGroup<SlocaliteFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SlocaliteFormService {
  createSlocaliteFormGroup(slocalite: SlocaliteFormGroupInput = { id: null }): SlocaliteFormGroup {
    const slocaliteRawValue = {
      ...this.getFormDefaults(),
      ...slocalite,
    };
    return new FormGroup<SlocaliteFormGroupContent>({
      id: new FormControl(
        { value: slocaliteRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      codelocalite: new FormControl(slocaliteRawValue.codelocalite),
      nomlocalite: new FormControl(slocaliteRawValue.nomlocalite),
      parent: new FormControl(slocaliteRawValue.parent),
      natureLocalite: new FormControl(slocaliteRawValue.natureLocalite),
      typeLocalite: new FormControl(slocaliteRawValue.typeLocalite),
    });
  }

  getSlocalite(form: SlocaliteFormGroup): ISlocalite | NewSlocalite {
    return form.getRawValue() as ISlocalite | NewSlocalite;
  }

  resetForm(form: SlocaliteFormGroup, slocalite: SlocaliteFormGroupInput): void {
    const slocaliteRawValue = { ...this.getFormDefaults(), ...slocalite };
    form.reset(
      {
        ...slocaliteRawValue,
        id: { value: slocaliteRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SlocaliteFormDefaults {
    return {
      id: null,
    };
  }
}
