import { IEvaleurattribut } from 'app/entities/evaleurattribut/evaleurattribut.model';
import { IEtypechamp } from 'app/entities/etypechamp/etypechamp.model';

export interface IEattribut {
  id: number;
  attrname?: string | null;
  attrdisplayname?: string | null;
  evaleurattribut?: Pick<IEvaleurattribut, 'id'> | null;
  etypechamps?: Pick<IEtypechamp, 'id'>[] | null;
}

export type NewEattribut = Omit<IEattribut, 'id'> & { id: null };
