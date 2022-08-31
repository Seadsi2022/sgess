import { IEattribut, NewEattribut } from './eattribut.model';

export const sampleWithRequiredData: IEattribut = {
  id: 36529,
};

export const sampleWithPartialData: IEattribut = {
  id: 89607,
  attrname: 'input back-end Loan',
};

export const sampleWithFullData: IEattribut = {
  id: 9803,
  attrname: 'black collaboration',
  attrdisplayname: 'Networked grey',
};

export const sampleWithNewData: NewEattribut = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
