import dayjs from 'dayjs/esm';

export interface IEcampagne {
  id: number;
  objetcampagne?: string | null;
  description?: string | null;
  debutcampagne?: dayjs.Dayjs | null;
  fincampagne?: dayjs.Dayjs | null;
  debutreelcamp?: dayjs.Dayjs | null;
  finreelcamp?: dayjs.Dayjs | null;
  isopen?: boolean | null;
}

export type NewEcampagne = Omit<IEcampagne, 'id'> & { id: null };
