import { IEgroupevariable, NewEgroupevariable } from './egroupevariable.model';

export const sampleWithRequiredData: IEgroupevariable = {
  id: 9531,
};

export const sampleWithPartialData: IEgroupevariable = {
  id: 11218,
  isActive: true,
};

export const sampleWithFullData: IEgroupevariable = {
  id: 13047,
  ordrevariable: 62821,
  isActive: false,
};

export const sampleWithNewData: NewEgroupevariable = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
