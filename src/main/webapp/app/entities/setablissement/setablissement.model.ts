import { ISstructure } from 'app/entities/sstructure/sstructure.model';

export interface ISetablissement {
  id: number;
  codeadministratif?: string | null;
  sstructure?: Pick<ISstructure, 'id'> | null;
}

export type NewSetablissement = Omit<ISetablissement, 'id'> & { id: null };
