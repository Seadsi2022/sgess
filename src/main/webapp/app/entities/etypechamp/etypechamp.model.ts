import { IEattribut } from 'app/entities/eattribut/eattribut.model';

export interface IEtypechamp {
  id: number;
  name?: string | null;
  displayname?: string | null;
  eattributs?: Pick<IEattribut, 'id'>[] | null;
}

export type NewEtypechamp = Omit<IEtypechamp, 'id'> & { id: null };
