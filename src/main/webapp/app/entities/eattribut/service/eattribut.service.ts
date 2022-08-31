import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEattribut, NewEattribut } from '../eattribut.model';

export type PartialUpdateEattribut = Partial<IEattribut> & Pick<IEattribut, 'id'>;

export type EntityResponseType = HttpResponse<IEattribut>;
export type EntityArrayResponseType = HttpResponse<IEattribut[]>;

@Injectable({ providedIn: 'root' })
export class EattributService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/eattributs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/eattributs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(eattribut: NewEattribut): Observable<EntityResponseType> {
    return this.http.post<IEattribut>(this.resourceUrl, eattribut, { observe: 'response' });
  }

  update(eattribut: IEattribut): Observable<EntityResponseType> {
    return this.http.put<IEattribut>(`${this.resourceUrl}/${this.getEattributIdentifier(eattribut)}`, eattribut, { observe: 'response' });
  }

  partialUpdate(eattribut: PartialUpdateEattribut): Observable<EntityResponseType> {
    return this.http.patch<IEattribut>(`${this.resourceUrl}/${this.getEattributIdentifier(eattribut)}`, eattribut, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEattribut>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEattribut[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEattribut[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getEattributIdentifier(eattribut: Pick<IEattribut, 'id'>): number {
    return eattribut.id;
  }

  compareEattribut(o1: Pick<IEattribut, 'id'> | null, o2: Pick<IEattribut, 'id'> | null): boolean {
    return o1 && o2 ? this.getEattributIdentifier(o1) === this.getEattributIdentifier(o2) : o1 === o2;
  }

  addEattributToCollectionIfMissing<Type extends Pick<IEattribut, 'id'>>(
    eattributCollection: Type[],
    ...eattributsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const eattributs: Type[] = eattributsToCheck.filter(isPresent);
    if (eattributs.length > 0) {
      const eattributCollectionIdentifiers = eattributCollection.map(eattributItem => this.getEattributIdentifier(eattributItem)!);
      const eattributsToAdd = eattributs.filter(eattributItem => {
        const eattributIdentifier = this.getEattributIdentifier(eattributItem);
        if (eattributCollectionIdentifiers.includes(eattributIdentifier)) {
          return false;
        }
        eattributCollectionIdentifiers.push(eattributIdentifier);
        return true;
      });
      return [...eattributsToAdd, ...eattributCollection];
    }
    return eattributCollection;
  }
}
