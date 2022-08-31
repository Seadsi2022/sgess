import { IScodevaleur } from 'app/entities/scodevaleur/scodevaleur.model';

export interface ISlocalite {
  id: number;
  codelocalite?: number | null;
  nomlocalite?: string | null;
  parent?: Pick<ISlocalite, 'id'> | null;
  natureLocalite?: Pick<IScodevaleur, 'id'> | null;
  typeLocalite?: Pick<IScodevaleur, 'id'> | null;
}

export type NewSlocalite = Omit<ISlocalite, 'id'> & { id: null };
