import { IEattributvariable, NewEattributvariable } from './eattributvariable.model';

export const sampleWithRequiredData: IEattributvariable = {
  id: 9844,
};

export const sampleWithPartialData: IEattributvariable = {
  id: 45607,
  attrvalue: 'Namibia Wooden Bac',
  isActive: false,
};

export const sampleWithFullData: IEattributvariable = {
  id: 3050,
  attrname: 'Coordinateur Sports Hat',
  attrvalue: 'uniform a Bacon',
  isActive: true,
};

export const sampleWithNewData: NewEattributvariable = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
