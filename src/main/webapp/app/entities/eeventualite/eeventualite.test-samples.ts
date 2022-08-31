import { IEeventualite, NewEeventualite } from './eeventualite.model';

export const sampleWithRequiredData: IEeventualite = {
  id: 83308,
};

export const sampleWithPartialData: IEeventualite = {
  id: 42807,
  eventualitevalue: 'Haute-Normandie',
  isActive: false,
};

export const sampleWithFullData: IEeventualite = {
  id: 49777,
  eventualitevalue: 'Cotton',
  isActive: false,
};

export const sampleWithNewData: NewEeventualite = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
