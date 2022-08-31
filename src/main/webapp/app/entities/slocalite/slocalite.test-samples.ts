import { ISlocalite, NewSlocalite } from './slocalite.model';

export const sampleWithRequiredData: ISlocalite = {
  id: 70869,
};

export const sampleWithPartialData: ISlocalite = {
  id: 93077,
  codelocalite: 79912,
};

export const sampleWithFullData: ISlocalite = {
  id: 95325,
  codelocalite: 14608,
  nomlocalite: 'De-engineered Pologne',
};

export const sampleWithNewData: NewSlocalite = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
