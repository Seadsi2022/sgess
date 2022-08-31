export interface IScode {
  id: number;
  codeLib?: string | null;
}

export type NewScode = Omit<IScode, 'id'> & { id: null };
