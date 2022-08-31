import { IEcampagne } from 'app/entities/ecampagne/ecampagne.model';
import { IScodevaleur } from 'app/entities/scodevaleur/scodevaleur.model';

export interface IEquestionnaire {
  id: number;
  objetquest?: string | null;
  descriptionquest?: string | null;
  isActive?: boolean | null;
  parent?: Pick<IEquestionnaire, 'id'> | null;
  ecampagne?: Pick<IEcampagne, 'id'> | null;
  typestructure?: Pick<IScodevaleur, 'id'> | null;
}

export type NewEquestionnaire = Omit<IEquestionnaire, 'id'> & { id: null };
