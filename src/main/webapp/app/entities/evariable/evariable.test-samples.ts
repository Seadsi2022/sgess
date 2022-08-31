import { IEvariable, NewEvariable } from './evariable.model';

export const sampleWithRequiredData: IEvariable = {
  id: 23068,
};

export const sampleWithPartialData: IEvariable = {
  id: 87011,
  isActive: true,
};

export const sampleWithFullData: IEvariable = {
  id: 48887,
  nomvariable: 'yellow silver functionalities',
  descvariable: 'port',
  champ: 'Assimilated static Riyal',
  isActive: true,
};

export const sampleWithNewData: NewEvariable = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
