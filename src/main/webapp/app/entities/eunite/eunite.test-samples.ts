import { IEunite, NewEunite } from './eunite.model';

export const sampleWithRequiredData: IEunite = {
  id: 47559,
};

export const sampleWithPartialData: IEunite = {
  id: 58179,
  nomunite: 'pixel proactive Account',
  symboleunite: 'web',
  facteur: 32787,
};

export const sampleWithFullData: IEunite = {
  id: 18028,
  nomunite: 'Marshall Haute-Normandie Proactive',
  symboleunite: 'Shoes Unbranded',
  facteur: 35628,
};

export const sampleWithNewData: NewEunite = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
