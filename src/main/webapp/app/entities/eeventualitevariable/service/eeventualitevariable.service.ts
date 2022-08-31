import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEeventualitevariable, NewEeventualitevariable } from '../eeventualitevariable.model';

export type PartialUpdateEeventualitevariable = Partial<IEeventualitevariable> & Pick<IEeventualitevariable, 'id'>;

export type EntityResponseType = HttpResponse<IEeventualitevariable>;
export type EntityArrayResponseType = HttpResponse<IEeventualitevariable[]>;

@Injectable({ providedIn: 'root' })
export class EeventualitevariableService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/eeventualitevariables');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/eeventualitevariables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(eeventualitevariable: NewEeventualitevariable): Observable<EntityResponseType> {
    return this.http.post<IEeventualitevariable>(this.resourceUrl, eeventualitevariable, { observe: 'response' });
  }

  update(eeventualitevariable: IEeventualitevariable): Observable<EntityResponseType> {
    return this.http.put<IEeventualitevariable>(
      `${this.resourceUrl}/${this.getEeventualitevariableIdentifier(eeventualitevariable)}`,
      eeventualitevariable,
      { observe: 'response' }
    );
  }

  partialUpdate(eeventualitevariable: PartialUpdateEeventualitevariable): Observable<EntityResponseType> {
    return this.http.patch<IEeventualitevariable>(
      `${this.resourceUrl}/${this.getEeventualitevariableIdentifier(eeventualitevariable)}`,
      eeventualitevariable,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEeventualitevariable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEeventualitevariable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEeventualitevariable[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getEeventualitevariableIdentifier(eeventualitevariable: Pick<IEeventualitevariable, 'id'>): number {
    return eeventualitevariable.id;
  }

  compareEeventualitevariable(o1: Pick<IEeventualitevariable, 'id'> | null, o2: Pick<IEeventualitevariable, 'id'> | null): boolean {
    return o1 && o2 ? this.getEeventualitevariableIdentifier(o1) === this.getEeventualitevariableIdentifier(o2) : o1 === o2;
  }

  addEeventualitevariableToCollectionIfMissing<Type extends Pick<IEeventualitevariable, 'id'>>(
    eeventualitevariableCollection: Type[],
    ...eeventualitevariablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const eeventualitevariables: Type[] = eeventualitevariablesToCheck.filter(isPresent);
    if (eeventualitevariables.length > 0) {
      const eeventualitevariableCollectionIdentifiers = eeventualitevariableCollection.map(
        eeventualitevariableItem => this.getEeventualitevariableIdentifier(eeventualitevariableItem)!
      );
      const eeventualitevariablesToAdd = eeventualitevariables.filter(eeventualitevariableItem => {
        const eeventualitevariableIdentifier = this.getEeventualitevariableIdentifier(eeventualitevariableItem);
        if (eeventualitevariableCollectionIdentifiers.includes(eeventualitevariableIdentifier)) {
          return false;
        }
        eeventualitevariableCollectionIdentifiers.push(eeventualitevariableIdentifier);
        return true;
      });
      return [...eeventualitevariablesToAdd, ...eeventualitevariableCollection];
    }
    return eeventualitevariableCollection;
  }
}
