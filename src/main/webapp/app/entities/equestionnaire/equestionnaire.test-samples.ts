import { IEquestionnaire, NewEquestionnaire } from './equestionnaire.model';

export const sampleWithRequiredData: IEquestionnaire = {
  id: 77891,
};

export const sampleWithPartialData: IEquestionnaire = {
  id: 52037,
  descriptionquest: 'Ouzbékistan Awesome',
  isActive: false,
};

export const sampleWithFullData: IEquestionnaire = {
  id: 34731,
  objetquest: 'up neural',
  descriptionquest: 'structure bus Limousin',
  isActive: true,
};

export const sampleWithNewData: NewEquestionnaire = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
