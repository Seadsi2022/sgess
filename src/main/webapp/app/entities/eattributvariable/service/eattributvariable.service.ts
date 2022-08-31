import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEattributvariable, NewEattributvariable } from '../eattributvariable.model';

export type PartialUpdateEattributvariable = Partial<IEattributvariable> & Pick<IEattributvariable, 'id'>;

export type EntityResponseType = HttpResponse<IEattributvariable>;
export type EntityArrayResponseType = HttpResponse<IEattributvariable[]>;

@Injectable({ providedIn: 'root' })
export class EattributvariableService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/eattributvariables');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/eattributvariables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(eattributvariable: NewEattributvariable): Observable<EntityResponseType> {
    return this.http.post<IEattributvariable>(this.resourceUrl, eattributvariable, { observe: 'response' });
  }

  update(eattributvariable: IEattributvariable): Observable<EntityResponseType> {
    return this.http.put<IEattributvariable>(
      `${this.resourceUrl}/${this.getEattributvariableIdentifier(eattributvariable)}`,
      eattributvariable,
      { observe: 'response' }
    );
  }

  partialUpdate(eattributvariable: PartialUpdateEattributvariable): Observable<EntityResponseType> {
    return this.http.patch<IEattributvariable>(
      `${this.resourceUrl}/${this.getEattributvariableIdentifier(eattributvariable)}`,
      eattributvariable,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEattributvariable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEattributvariable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEattributvariable[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getEattributvariableIdentifier(eattributvariable: Pick<IEattributvariable, 'id'>): number {
    return eattributvariable.id;
  }

  compareEattributvariable(o1: Pick<IEattributvariable, 'id'> | null, o2: Pick<IEattributvariable, 'id'> | null): boolean {
    return o1 && o2 ? this.getEattributvariableIdentifier(o1) === this.getEattributvariableIdentifier(o2) : o1 === o2;
  }

  addEattributvariableToCollectionIfMissing<Type extends Pick<IEattributvariable, 'id'>>(
    eattributvariableCollection: Type[],
    ...eattributvariablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const eattributvariables: Type[] = eattributvariablesToCheck.filter(isPresent);
    if (eattributvariables.length > 0) {
      const eattributvariableCollectionIdentifiers = eattributvariableCollection.map(
        eattributvariableItem => this.getEattributvariableIdentifier(eattributvariableItem)!
      );
      const eattributvariablesToAdd = eattributvariables.filter(eattributvariableItem => {
        const eattributvariableIdentifier = this.getEattributvariableIdentifier(eattributvariableItem);
        if (eattributvariableCollectionIdentifiers.includes(eattributvariableIdentifier)) {
          return false;
        }
        eattributvariableCollectionIdentifiers.push(eattributvariableIdentifier);
        return true;
      });
      return [...eattributvariablesToAdd, ...eattributvariableCollection];
    }
    return eattributvariableCollection;
  }
}
