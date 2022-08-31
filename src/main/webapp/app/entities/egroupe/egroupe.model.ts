import { IEquestionnaire } from 'app/entities/equestionnaire/equestionnaire.model';

export interface IEgroupe {
  id: number;
  titregroupe?: string | null;
  ordregroupe?: number | null;
  isActive?: boolean | null;
  equestionnaire?: Pick<IEquestionnaire, 'id'> | null;
}

export type NewEgroupe = Omit<IEgroupe, 'id'> & { id: null };
