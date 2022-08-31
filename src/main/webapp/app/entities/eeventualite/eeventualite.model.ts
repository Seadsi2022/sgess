export interface IEeventualite {
  id: number;
  eventualitevalue?: string | null;
  isActive?: boolean | null;
}

export type NewEeventualite = Omit<IEeventualite, 'id'> & { id: null };
