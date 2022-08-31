export interface IEunite {
  id: number;
  nomunite?: string | null;
  symboleunite?: string | null;
  facteur?: number | null;
  unitebase?: Pick<IEunite, 'id'> | null;
}

export type NewEunite = Omit<IEunite, 'id'> & { id: null };
