import { IScodevaleur, NewScodevaleur } from './scodevaleur.model';

export const sampleWithRequiredData: IScodevaleur = {
  id: 18287,
};

export const sampleWithPartialData: IScodevaleur = {
  id: 68929,
  codevaleurLib: 'AI e-services Berkshire',
};

export const sampleWithFullData: IScodevaleur = {
  id: 33294,
  codevaleurLib: 'compressing Cotton Berkshire',
};

export const sampleWithNewData: NewScodevaleur = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
