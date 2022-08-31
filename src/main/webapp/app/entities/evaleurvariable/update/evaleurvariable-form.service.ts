import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEvaleurvariable, NewEvaleurvariable } from '../evaleurvariable.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEvaleurvariable for edit and NewEvaleurvariableFormGroupInput for create.
 */
type EvaleurvariableFormGroupInput = IEvaleurvariable | PartialWithRequiredKeyOf<NewEvaleurvariable>;

type EvaleurvariableFormDefaults = Pick<NewEvaleurvariable, 'id' | 'isActive'>;

type EvaleurvariableFormGroupContent = {
  id: FormControl<IEvaleurvariable['id'] | NewEvaleurvariable['id']>;
  valeur: FormControl<IEvaleurvariable['valeur']>;
  ligne: FormControl<IEvaleurvariable['ligne']>;
  colonne: FormControl<IEvaleurvariable['colonne']>;
  isActive: FormControl<IEvaleurvariable['isActive']>;
  egroupevariable: FormControl<IEvaleurvariable['egroupevariable']>;
  sstructure: FormControl<IEvaleurvariable['sstructure']>;
};

export type EvaleurvariableFormGroup = FormGroup<EvaleurvariableFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EvaleurvariableFormService {
  createEvaleurvariableFormGroup(evaleurvariable: EvaleurvariableFormGroupInput = { id: null }): EvaleurvariableFormGroup {
    const evaleurvariableRawValue = {
      ...this.getFormDefaults(),
      ...evaleurvariable,
    };
    return new FormGroup<EvaleurvariableFormGroupContent>({
      id: new FormControl(
        { value: evaleurvariableRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      valeur: new FormControl(evaleurvariableRawValue.valeur),
      ligne: new FormControl(evaleurvariableRawValue.ligne),
      colonne: new FormControl(evaleurvariableRawValue.colonne),
      isActive: new FormControl(evaleurvariableRawValue.isActive),
      egroupevariable: new FormControl(evaleurvariableRawValue.egroupevariable),
      sstructure: new FormControl(evaleurvariableRawValue.sstructure),
    });
  }

  getEvaleurvariable(form: EvaleurvariableFormGroup): IEvaleurvariable | NewEvaleurvariable {
    return form.getRawValue() as IEvaleurvariable | NewEvaleurvariable;
  }

  resetForm(form: EvaleurvariableFormGroup, evaleurvariable: EvaleurvariableFormGroupInput): void {
    const evaleurvariableRawValue = { ...this.getFormDefaults(), ...evaleurvariable };
    form.reset(
      {
        ...evaleurvariableRawValue,
        id: { value: evaleurvariableRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EvaleurvariableFormDefaults {
    return {
      id: null,
      isActive: false,
    };
  }
}
