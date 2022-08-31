import { IEtypevariable, NewEtypevariable } from './etypevariable.model';

export const sampleWithRequiredData: IEtypevariable = {
  id: 72633,
};

export const sampleWithPartialData: IEtypevariable = {
  id: 98154,
  desctypevariable: 'Refined synergies',
};

export const sampleWithFullData: IEtypevariable = {
  id: 105,
  nomtypevar: 'USB Sum Market',
  desctypevariable: 'withdrawal integrate',
  isActive: true,
};

export const sampleWithNewData: NewEtypevariable = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
