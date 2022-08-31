import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IEattribut, NewEattribut } from '../eattribut.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEattribut for edit and NewEattributFormGroupInput for create.
 */
type EattributFormGroupInput = IEattribut | PartialWithRequiredKeyOf<NewEattribut>;

type EattributFormDefaults = Pick<NewEattribut, 'id' | 'etypechamps'>;

type EattributFormGroupContent = {
  id: FormControl<IEattribut['id'] | NewEattribut['id']>;
  attrname: FormControl<IEattribut['attrname']>;
  attrdisplayname: FormControl<IEattribut['attrdisplayname']>;
  evaleurattribut: FormControl<IEattribut['evaleurattribut']>;
  etypechamps: FormControl<IEattribut['etypechamps']>;
};

export type EattributFormGroup = FormGroup<EattributFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EattributFormService {
  createEattributFormGroup(eattribut: EattributFormGroupInput = { id: null }): EattributFormGroup {
    const eattributRawValue = {
      ...this.getFormDefaults(),
      ...eattribut,
    };
    return new FormGroup<EattributFormGroupContent>({
      id: new FormControl(
        { value: eattributRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      attrname: new FormControl(eattributRawValue.attrname),
      attrdisplayname: new FormControl(eattributRawValue.attrdisplayname),
      evaleurattribut: new FormControl(eattributRawValue.evaleurattribut),
      etypechamps: new FormControl(eattributRawValue.etypechamps ?? []),
    });
  }

  getEattribut(form: EattributFormGroup): IEattribut | NewEattribut {
    return form.getRawValue() as IEattribut | NewEattribut;
  }

  resetForm(form: EattributFormGroup, eattribut: EattributFormGroupInput): void {
    const eattributRawValue = { ...this.getFormDefaults(), ...eattribut };
    form.reset(
      {
        ...eattributRawValue,
        id: { value: eattributRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EattributFormDefaults {
    return {
      id: null,
      etypechamps: [],
    };
  }
}
