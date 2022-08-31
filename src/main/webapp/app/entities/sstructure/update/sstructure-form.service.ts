import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ISstructure, NewSstructure } from '../sstructure.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ISstructure for edit and NewSstructureFormGroupInput for create.
 */
type SstructureFormGroupInput = ISstructure | PartialWithRequiredKeyOf<NewSstructure>;

type SstructureFormDefaults = Pick<NewSstructure, 'id' | 'scodes'>;

type SstructureFormGroupContent = {
  id: FormControl<ISstructure['id'] | NewSstructure['id']>;
  nomstructure: FormControl<ISstructure['nomstructure']>;
  siglestructure: FormControl<ISstructure['siglestructure']>;
  telstructure: FormControl<ISstructure['telstructure']>;
  bpstructure: FormControl<ISstructure['bpstructure']>;
  emailstructure: FormControl<ISstructure['emailstructure']>;
  profondeur: FormControl<ISstructure['profondeur']>;
  parent: FormControl<ISstructure['parent']>;
  scodes: FormControl<ISstructure['scodes']>;
  slocalite: FormControl<ISstructure['slocalite']>;
};

export type SstructureFormGroup = FormGroup<SstructureFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class SstructureFormService {
  createSstructureFormGroup(sstructure: SstructureFormGroupInput = { id: null }): SstructureFormGroup {
    const sstructureRawValue = {
      ...this.getFormDefaults(),
      ...sstructure,
    };
    return new FormGroup<SstructureFormGroupContent>({
      id: new FormControl(
        { value: sstructureRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      nomstructure: new FormControl(sstructureRawValue.nomstructure),
      siglestructure: new FormControl(sstructureRawValue.siglestructure),
      telstructure: new FormControl(sstructureRawValue.telstructure),
      bpstructure: new FormControl(sstructureRawValue.bpstructure),
      emailstructure: new FormControl(sstructureRawValue.emailstructure),
      profondeur: new FormControl(sstructureRawValue.profondeur),
      parent: new FormControl(sstructureRawValue.parent),
      scodes: new FormControl(sstructureRawValue.scodes ?? []),
      slocalite: new FormControl(sstructureRawValue.slocalite),
    });
  }

  getSstructure(form: SstructureFormGroup): ISstructure | NewSstructure {
    return form.getRawValue() as ISstructure | NewSstructure;
  }

  resetForm(form: SstructureFormGroup, sstructure: SstructureFormGroupInput): void {
    const sstructureRawValue = { ...this.getFormDefaults(), ...sstructure };
    form.reset(
      {
        ...sstructureRawValue,
        id: { value: sstructureRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): SstructureFormDefaults {
    return {
      id: null,
      scodes: [],
    };
  }
}
