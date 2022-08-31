import { IScodevaleur } from 'app/entities/scodevaleur/scodevaleur.model';
import { ISlocalite } from 'app/entities/slocalite/slocalite.model';

export interface ISstructure {
  id: number;
  nomstructure?: string | null;
  siglestructure?: string | null;
  telstructure?: string | null;
  bpstructure?: string | null;
  emailstructure?: string | null;
  profondeur?: number | null;
  parent?: Pick<ISstructure, 'id'> | null;
  scodes?: Pick<IScodevaleur, 'id'>[] | null;
  slocalite?: Pick<ISlocalite, 'id'> | null;
}

export type NewSstructure = Omit<ISstructure, 'id'> & { id: null };
