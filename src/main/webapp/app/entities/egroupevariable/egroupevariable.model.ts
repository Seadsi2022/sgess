import { IEvariable } from 'app/entities/evariable/evariable.model';
import { IEgroupe } from 'app/entities/egroupe/egroupe.model';

export interface IEgroupevariable {
  id: number;
  ordrevariable?: number | null;
  isActive?: boolean | null;
  suivant?: Pick<IEgroupevariable, 'id'> | null;
  evariable?: Pick<IEvariable, 'id'> | null;
  egroupe?: Pick<IEgroupe, 'id'> | null;
}

export type NewEgroupevariable = Omit<IEgroupevariable, 'id'> & { id: null };
