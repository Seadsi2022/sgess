import { IEvaleurvariable, NewEvaleurvariable } from './evaleurvariable.model';

export const sampleWithRequiredData: IEvaleurvariable = {
  id: 19335,
};

export const sampleWithPartialData: IEvaleurvariable = {
  id: 53642,
  valeur: 'digital synthesizing circuit',
  isActive: true,
};

export const sampleWithFullData: IEvaleurvariable = {
  id: 36415,
  valeur: 'b Franc',
  ligne: 66040,
  colonne: 95720,
  isActive: true,
};

export const sampleWithNewData: NewEvaleurvariable = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
