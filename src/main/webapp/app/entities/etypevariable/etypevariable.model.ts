export interface IEtypevariable {
  id: number;
  nomtypevar?: string | null;
  desctypevariable?: string | null;
  isActive?: boolean | null;
}

export type NewEtypevariable = Omit<IEtypevariable, 'id'> & { id: null };
