import { IEtypechamp, NewEtypechamp } from './etypechamp.model';

export const sampleWithRequiredData: IEtypechamp = {
  id: 52708,
};

export const sampleWithPartialData: IEtypechamp = {
  id: 91805,
  displayname: 'Account b salmon',
};

export const sampleWithFullData: IEtypechamp = {
  id: 45429,
  name: 'Customizable',
  displayname: 'bypass',
};

export const sampleWithNewData: NewEtypechamp = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
