import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEeventualite, NewEeventualite } from '../eeventualite.model';

export type PartialUpdateEeventualite = Partial<IEeventualite> & Pick<IEeventualite, 'id'>;

export type EntityResponseType = HttpResponse<IEeventualite>;
export type EntityArrayResponseType = HttpResponse<IEeventualite[]>;

@Injectable({ providedIn: 'root' })
export class EeventualiteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/eeventualites');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/eeventualites');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(eeventualite: NewEeventualite): Observable<EntityResponseType> {
    return this.http.post<IEeventualite>(this.resourceUrl, eeventualite, { observe: 'response' });
  }

  update(eeventualite: IEeventualite): Observable<EntityResponseType> {
    return this.http.put<IEeventualite>(`${this.resourceUrl}/${this.getEeventualiteIdentifier(eeventualite)}`, eeventualite, {
      observe: 'response',
    });
  }

  partialUpdate(eeventualite: PartialUpdateEeventualite): Observable<EntityResponseType> {
    return this.http.patch<IEeventualite>(`${this.resourceUrl}/${this.getEeventualiteIdentifier(eeventualite)}`, eeventualite, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEeventualite>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEeventualite[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEeventualite[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getEeventualiteIdentifier(eeventualite: Pick<IEeventualite, 'id'>): number {
    return eeventualite.id;
  }

  compareEeventualite(o1: Pick<IEeventualite, 'id'> | null, o2: Pick<IEeventualite, 'id'> | null): boolean {
    return o1 && o2 ? this.getEeventualiteIdentifier(o1) === this.getEeventualiteIdentifier(o2) : o1 === o2;
  }

  addEeventualiteToCollectionIfMissing<Type extends Pick<IEeventualite, 'id'>>(
    eeventualiteCollection: Type[],
    ...eeventualitesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const eeventualites: Type[] = eeventualitesToCheck.filter(isPresent);
    if (eeventualites.length > 0) {
      const eeventualiteCollectionIdentifiers = eeventualiteCollection.map(
        eeventualiteItem => this.getEeventualiteIdentifier(eeventualiteItem)!
      );
      const eeventualitesToAdd = eeventualites.filter(eeventualiteItem => {
        const eeventualiteIdentifier = this.getEeventualiteIdentifier(eeventualiteItem);
        if (eeventualiteCollectionIdentifiers.includes(eeventualiteIdentifier)) {
          return false;
        }
        eeventualiteCollectionIdentifiers.push(eeventualiteIdentifier);
        return true;
      });
      return [...eeventualitesToAdd, ...eeventualiteCollection];
    }
    return eeventualiteCollection;
  }
}
