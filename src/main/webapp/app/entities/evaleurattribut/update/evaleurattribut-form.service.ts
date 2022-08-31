import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEvaleurattribut, NewEvaleurattribut } from '../evaleurattribut.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEvaleurattribut for edit and NewEvaleurattributFormGroupInput for create.
 */
type EvaleurattributFormGroupInput = IEvaleurattribut | PartialWithRequiredKeyOf<NewEvaleurattribut>;

type EvaleurattributFormDefaults = Pick<NewEvaleurattribut, 'id'>;

type EvaleurattributFormGroupContent = {
  id: FormControl<IEvaleurattribut['id'] | NewEvaleurattribut['id']>;
  valeur: FormControl<IEvaleurattribut['valeur']>;
  valeurdisplayname: FormControl<IEvaleurattribut['valeurdisplayname']>;
};

export type EvaleurattributFormGroup = FormGroup<EvaleurattributFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EvaleurattributFormService {
  createEvaleurattributFormGroup(evaleurattribut: EvaleurattributFormGroupInput = { id: null }): EvaleurattributFormGroup {
    const evaleurattributRawValue = {
      ...this.getFormDefaults(),
      ...evaleurattribut,
    };
    return new FormGroup<EvaleurattributFormGroupContent>({
      id: new FormControl(
        { value: evaleurattributRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      valeur: new FormControl(evaleurattributRawValue.valeur),
      valeurdisplayname: new FormControl(evaleurattributRawValue.valeurdisplayname),
    });
  }

  getEvaleurattribut(form: EvaleurattributFormGroup): IEvaleurattribut | NewEvaleurattribut {
    return form.getRawValue() as IEvaleurattribut | NewEvaleurattribut;
  }

  resetForm(form: EvaleurattributFormGroup, evaleurattribut: EvaleurattributFormGroupInput): void {
    const evaleurattributRawValue = { ...this.getFormDefaults(), ...evaleurattribut };
    form.reset(
      {
        ...evaleurattributRawValue,
        id: { value: evaleurattributRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EvaleurattributFormDefaults {
    return {
      id: null,
    };
  }
}
