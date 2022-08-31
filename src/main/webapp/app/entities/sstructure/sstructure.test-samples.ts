import { ISstructure, NewSstructure } from './sstructure.model';

export const sampleWithRequiredData: ISstructure = {
  id: 7773,
};

export const sampleWithPartialData: ISstructure = {
  id: 63265,
  siglestructure: 'withdrawal synergistic b',
  bpstructure: 'Intelligent',
};

export const sampleWithFullData: ISstructure = {
  id: 26172,
  nomstructure: 'SQL Salvador b',
  siglestructure: 'CSS',
  telstructure: 'a',
  bpstructure: 'Saint-Jacques Electronics dynamic',
  emailstructure: 'microchip Cedi reintermediate',
  profondeur: 52985,
};

export const sampleWithNewData: NewSstructure = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
