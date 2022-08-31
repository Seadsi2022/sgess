import { IEvariable } from 'app/entities/evariable/evariable.model';

export interface IEattributvariable {
  id: number;
  attrname?: string | null;
  attrvalue?: string | null;
  isActive?: boolean | null;
  evariable?: Pick<IEvariable, 'id'> | null;
}

export type NewEattributvariable = Omit<IEattributvariable, 'id'> & { id: null };
