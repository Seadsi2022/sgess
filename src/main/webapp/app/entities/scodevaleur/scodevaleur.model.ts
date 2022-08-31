import { IScode } from 'app/entities/scode/scode.model';
import { ISstructure } from 'app/entities/sstructure/sstructure.model';

export interface IScodevaleur {
  id: number;
  codevaleurLib?: string | null;
  parent?: Pick<IScodevaleur, 'id'> | null;
  scode?: Pick<IScode, 'id'> | null;
  sstructures?: Pick<ISstructure, 'id'>[] | null;
}

export type NewScodevaleur = Omit<IScodevaleur, 'id'> & { id: null };
