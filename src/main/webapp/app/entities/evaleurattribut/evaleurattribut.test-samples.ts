import { IEvaleurattribut, NewEvaleurattribut } from './evaleurattribut.model';

export const sampleWithRequiredData: IEvaleurattribut = {
  id: 16454,
};

export const sampleWithPartialData: IEvaleurattribut = {
  id: 50936,
  valeur: 'a markets',
};

export const sampleWithFullData: IEvaleurattribut = {
  id: 74144,
  valeur: 'Rustic',
  valeurdisplayname: 'c',
};

export const sampleWithNewData: NewEvaleurattribut = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
