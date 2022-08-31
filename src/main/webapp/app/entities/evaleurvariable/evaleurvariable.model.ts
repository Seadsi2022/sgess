import { IEgroupevariable } from 'app/entities/egroupevariable/egroupevariable.model';
import { ISstructure } from 'app/entities/sstructure/sstructure.model';

export interface IEvaleurvariable {
  id: number;
  valeur?: string | null;
  ligne?: number | null;
  colonne?: number | null;
  isActive?: boolean | null;
  egroupevariable?: Pick<IEgroupevariable, 'id'> | null;
  sstructure?: Pick<ISstructure, 'id'> | null;
}

export type NewEvaleurvariable = Omit<IEvaleurvariable, 'id'> & { id: null };
