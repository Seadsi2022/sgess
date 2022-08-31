import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEtypevariable, NewEtypevariable } from '../etypevariable.model';

export type PartialUpdateEtypevariable = Partial<IEtypevariable> & Pick<IEtypevariable, 'id'>;

export type EntityResponseType = HttpResponse<IEtypevariable>;
export type EntityArrayResponseType = HttpResponse<IEtypevariable[]>;

@Injectable({ providedIn: 'root' })
export class EtypevariableService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/etypevariables');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/etypevariables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(etypevariable: NewEtypevariable): Observable<EntityResponseType> {
    return this.http.post<IEtypevariable>(this.resourceUrl, etypevariable, { observe: 'response' });
  }

  update(etypevariable: IEtypevariable): Observable<EntityResponseType> {
    return this.http.put<IEtypevariable>(`${this.resourceUrl}/${this.getEtypevariableIdentifier(etypevariable)}`, etypevariable, {
      observe: 'response',
    });
  }

  partialUpdate(etypevariable: PartialUpdateEtypevariable): Observable<EntityResponseType> {
    return this.http.patch<IEtypevariable>(`${this.resourceUrl}/${this.getEtypevariableIdentifier(etypevariable)}`, etypevariable, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEtypevariable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEtypevariable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEtypevariable[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getEtypevariableIdentifier(etypevariable: Pick<IEtypevariable, 'id'>): number {
    return etypevariable.id;
  }

  compareEtypevariable(o1: Pick<IEtypevariable, 'id'> | null, o2: Pick<IEtypevariable, 'id'> | null): boolean {
    return o1 && o2 ? this.getEtypevariableIdentifier(o1) === this.getEtypevariableIdentifier(o2) : o1 === o2;
  }

  addEtypevariableToCollectionIfMissing<Type extends Pick<IEtypevariable, 'id'>>(
    etypevariableCollection: Type[],
    ...etypevariablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const etypevariables: Type[] = etypevariablesToCheck.filter(isPresent);
    if (etypevariables.length > 0) {
      const etypevariableCollectionIdentifiers = etypevariableCollection.map(
        etypevariableItem => this.getEtypevariableIdentifier(etypevariableItem)!
      );
      const etypevariablesToAdd = etypevariables.filter(etypevariableItem => {
        const etypevariableIdentifier = this.getEtypevariableIdentifier(etypevariableItem);
        if (etypevariableCollectionIdentifiers.includes(etypevariableIdentifier)) {
          return false;
        }
        etypevariableCollectionIdentifiers.push(etypevariableIdentifier);
        return true;
      });
      return [...etypevariablesToAdd, ...etypevariableCollection];
    }
    return etypevariableCollection;
  }
}
