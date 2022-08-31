import { IEtypevariable } from 'app/entities/etypevariable/etypevariable.model';
import { IEunite } from 'app/entities/eunite/eunite.model';

export interface IEvariable {
  id: number;
  nomvariable?: string | null;
  descvariable?: string | null;
  champ?: string | null;
  isActive?: boolean | null;
  etypevariable?: Pick<IEtypevariable, 'id'> | null;
  eunite?: Pick<IEunite, 'id'> | null;
}

export type NewEvariable = Omit<IEvariable, 'id'> & { id: null };
