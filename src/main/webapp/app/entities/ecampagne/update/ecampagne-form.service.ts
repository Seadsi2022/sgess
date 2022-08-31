import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { IEcampagne, NewEcampagne } from '../ecampagne.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IEcampagne for edit and NewEcampagneFormGroupInput for create.
 */
type EcampagneFormGroupInput = IEcampagne | PartialWithRequiredKeyOf<NewEcampagne>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends IEcampagne | NewEcampagne> = Omit<T, 'debutcampagne' | 'fincampagne' | 'debutreelcamp' | 'finreelcamp'> & {
  debutcampagne?: string | null;
  fincampagne?: string | null;
  debutreelcamp?: string | null;
  finreelcamp?: string | null;
};

type EcampagneFormRawValue = FormValueOf<IEcampagne>;

type NewEcampagneFormRawValue = FormValueOf<NewEcampagne>;

type EcampagneFormDefaults = Pick<NewEcampagne, 'id' | 'debutcampagne' | 'fincampagne' | 'debutreelcamp' | 'finreelcamp' | 'isopen'>;

type EcampagneFormGroupContent = {
  id: FormControl<EcampagneFormRawValue['id'] | NewEcampagne['id']>;
  objetcampagne: FormControl<EcampagneFormRawValue['objetcampagne']>;
  description: FormControl<EcampagneFormRawValue['description']>;
  debutcampagne: FormControl<EcampagneFormRawValue['debutcampagne']>;
  fincampagne: FormControl<EcampagneFormRawValue['fincampagne']>;
  debutreelcamp: FormControl<EcampagneFormRawValue['debutreelcamp']>;
  finreelcamp: FormControl<EcampagneFormRawValue['finreelcamp']>;
  isopen: FormControl<EcampagneFormRawValue['isopen']>;
};

export type EcampagneFormGroup = FormGroup<EcampagneFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class EcampagneFormService {
  createEcampagneFormGroup(ecampagne: EcampagneFormGroupInput = { id: null }): EcampagneFormGroup {
    const ecampagneRawValue = this.convertEcampagneToEcampagneRawValue({
      ...this.getFormDefaults(),
      ...ecampagne,
    });
    return new FormGroup<EcampagneFormGroupContent>({
      id: new FormControl(
        { value: ecampagneRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      objetcampagne: new FormControl(ecampagneRawValue.objetcampagne),
      description: new FormControl(ecampagneRawValue.description),
      debutcampagne: new FormControl(ecampagneRawValue.debutcampagne),
      fincampagne: new FormControl(ecampagneRawValue.fincampagne),
      debutreelcamp: new FormControl(ecampagneRawValue.debutreelcamp),
      finreelcamp: new FormControl(ecampagneRawValue.finreelcamp),
      isopen: new FormControl(ecampagneRawValue.isopen),
    });
  }

  getEcampagne(form: EcampagneFormGroup): IEcampagne | NewEcampagne {
    return this.convertEcampagneRawValueToEcampagne(form.getRawValue() as EcampagneFormRawValue | NewEcampagneFormRawValue);
  }

  resetForm(form: EcampagneFormGroup, ecampagne: EcampagneFormGroupInput): void {
    const ecampagneRawValue = this.convertEcampagneToEcampagneRawValue({ ...this.getFormDefaults(), ...ecampagne });
    form.reset(
      {
        ...ecampagneRawValue,
        id: { value: ecampagneRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): EcampagneFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      debutcampagne: currentTime,
      fincampagne: currentTime,
      debutreelcamp: currentTime,
      finreelcamp: currentTime,
      isopen: false,
    };
  }

  private convertEcampagneRawValueToEcampagne(rawEcampagne: EcampagneFormRawValue | NewEcampagneFormRawValue): IEcampagne | NewEcampagne {
    return {
      ...rawEcampagne,
      debutcampagne: dayjs(rawEcampagne.debutcampagne, DATE_TIME_FORMAT),
      fincampagne: dayjs(rawEcampagne.fincampagne, DATE_TIME_FORMAT),
      debutreelcamp: dayjs(rawEcampagne.debutreelcamp, DATE_TIME_FORMAT),
      finreelcamp: dayjs(rawEcampagne.finreelcamp, DATE_TIME_FORMAT),
    };
  }

  private convertEcampagneToEcampagneRawValue(
    ecampagne: IEcampagne | (Partial<NewEcampagne> & EcampagneFormDefaults)
  ): EcampagneFormRawValue | PartialWithRequiredKeyOf<NewEcampagneFormRawValue> {
    return {
      ...ecampagne,
      debutcampagne: ecampagne.debutcampagne ? ecampagne.debutcampagne.format(DATE_TIME_FORMAT) : undefined,
      fincampagne: ecampagne.fincampagne ? ecampagne.fincampagne.format(DATE_TIME_FORMAT) : undefined,
      debutreelcamp: ecampagne.debutreelcamp ? ecampagne.debutreelcamp.format(DATE_TIME_FORMAT) : undefined,
      finreelcamp: ecampagne.finreelcamp ? ecampagne.finreelcamp.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
