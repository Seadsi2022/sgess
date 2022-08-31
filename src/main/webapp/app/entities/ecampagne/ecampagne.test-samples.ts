import dayjs from 'dayjs/esm';

import { IEcampagne, NewEcampagne } from './ecampagne.model';

export const sampleWithRequiredData: IEcampagne = {
  id: 21651,
};

export const sampleWithPartialData: IEcampagne = {
  id: 89700,
  debutcampagne: dayjs('2022-08-31T01:45'),
  isopen: true,
};

export const sampleWithFullData: IEcampagne = {
  id: 921,
  objetcampagne: 'strategize programming',
  description: 'toolset',
  debutcampagne: dayjs('2022-08-30T14:05'),
  fincampagne: dayjs('2022-08-30T20:08'),
  debutreelcamp: dayjs('2022-08-31T06:06'),
  finreelcamp: dayjs('2022-08-31T09:22'),
  isopen: false,
};

export const sampleWithNewData: NewEcampagne = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
