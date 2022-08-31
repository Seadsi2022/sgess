import { IEvariable } from 'app/entities/evariable/evariable.model';
import { IEeventualite } from 'app/entities/eeventualite/eeventualite.model';

export interface IEeventualitevariable {
  id: number;
  evariable?: Pick<IEvariable, 'id'> | null;
  eeventualite?: Pick<IEeventualite, 'id'> | null;
}

export type NewEeventualitevariable = Omit<IEeventualitevariable, 'id'> & { id: null };
