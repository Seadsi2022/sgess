import { ISetablissement, NewSetablissement } from './setablissement.model';

export const sampleWithRequiredData: ISetablissement = {
  id: 66136,
};

export const sampleWithPartialData: ISetablissement = {
  id: 51658,
  codeadministratif: 'Designer a hack',
};

export const sampleWithFullData: ISetablissement = {
  id: 22599,
  codeadministratif: 'Table withdrawal Architecte',
};

export const sampleWithNewData: NewSetablissement = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
