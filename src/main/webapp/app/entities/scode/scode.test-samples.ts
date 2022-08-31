import { IScode, NewScode } from './scode.model';

export const sampleWithRequiredData: IScode = {
  id: 43071,
};

export const sampleWithPartialData: IScode = {
  id: 47049,
};

export const sampleWithFullData: IScode = {
  id: 89095,
  codeLib: 'neutral deposit',
};

export const sampleWithNewData: NewScode = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
