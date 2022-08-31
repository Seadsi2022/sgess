export interface IEvaleurattribut {
  id: number;
  valeur?: string | null;
  valeurdisplayname?: string | null;
}

export type NewEvaleurattribut = Omit<IEvaleurattribut, 'id'> & { id: null };
