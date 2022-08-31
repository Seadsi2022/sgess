import { IEgroupe, NewEgroupe } from './egroupe.model';

export const sampleWithRequiredData: IEgroupe = {
  id: 43387,
};

export const sampleWithPartialData: IEgroupe = {
  id: 60180,
  titregroupe: 'violet bypassing primary',
  ordregroupe: 12153,
  isActive: false,
};

export const sampleWithFullData: IEgroupe = {
  id: 94124,
  titregroupe: 'Frozen',
  ordregroupe: 99868,
  isActive: true,
};

export const sampleWithNewData: NewEgroupe = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
