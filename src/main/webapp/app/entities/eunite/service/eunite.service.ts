import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEunite, NewEunite } from '../eunite.model';

export type PartialUpdateEunite = Partial<IEunite> & Pick<IEunite, 'id'>;

export type EntityResponseType = HttpResponse<IEunite>;
export type EntityArrayResponseType = HttpResponse<IEunite[]>;

@Injectable({ providedIn: 'root' })
export class EuniteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/eunites');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/eunites');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(eunite: NewEunite): Observable<EntityResponseType> {
    return this.http.post<IEunite>(this.resourceUrl, eunite, { observe: 'response' });
  }

  update(eunite: IEunite): Observable<EntityResponseType> {
    return this.http.put<IEunite>(`${this.resourceUrl}/${this.getEuniteIdentifier(eunite)}`, eunite, { observe: 'response' });
  }

  partialUpdate(eunite: PartialUpdateEunite): Observable<EntityResponseType> {
    return this.http.patch<IEunite>(`${this.resourceUrl}/${this.getEuniteIdentifier(eunite)}`, eunite, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEunite>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEunite[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEunite[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getEuniteIdentifier(eunite: Pick<IEunite, 'id'>): number {
    return eunite.id;
  }

  compareEunite(o1: Pick<IEunite, 'id'> | null, o2: Pick<IEunite, 'id'> | null): boolean {
    return o1 && o2 ? this.getEuniteIdentifier(o1) === this.getEuniteIdentifier(o2) : o1 === o2;
  }

  addEuniteToCollectionIfMissing<Type extends Pick<IEunite, 'id'>>(
    euniteCollection: Type[],
    ...eunitesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const eunites: Type[] = eunitesToCheck.filter(isPresent);
    if (eunites.length > 0) {
      const euniteCollectionIdentifiers = euniteCollection.map(euniteItem => this.getEuniteIdentifier(euniteItem)!);
      const eunitesToAdd = eunites.filter(euniteItem => {
        const euniteIdentifier = this.getEuniteIdentifier(euniteItem);
        if (euniteCollectionIdentifiers.includes(euniteIdentifier)) {
          return false;
        }
        euniteCollectionIdentifiers.push(euniteIdentifier);
        return true;
      });
      return [...eunitesToAdd, ...euniteCollection];
    }
    return euniteCollection;
  }
}
