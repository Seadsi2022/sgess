import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEtypechamp, NewEtypechamp } from '../etypechamp.model';

export type PartialUpdateEtypechamp = Partial<IEtypechamp> & Pick<IEtypechamp, 'id'>;

export type EntityResponseType = HttpResponse<IEtypechamp>;
export type EntityArrayResponseType = HttpResponse<IEtypechamp[]>;

@Injectable({ providedIn: 'root' })
export class EtypechampService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/etypechamps');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/etypechamps');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(etypechamp: NewEtypechamp): Observable<EntityResponseType> {
    return this.http.post<IEtypechamp>(this.resourceUrl, etypechamp, { observe: 'response' });
  }

  update(etypechamp: IEtypechamp): Observable<EntityResponseType> {
    return this.http.put<IEtypechamp>(`${this.resourceUrl}/${this.getEtypechampIdentifier(etypechamp)}`, etypechamp, {
      observe: 'response',
    });
  }

  partialUpdate(etypechamp: PartialUpdateEtypechamp): Observable<EntityResponseType> {
    return this.http.patch<IEtypechamp>(`${this.resourceUrl}/${this.getEtypechampIdentifier(etypechamp)}`, etypechamp, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEtypechamp>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEtypechamp[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEtypechamp[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getEtypechampIdentifier(etypechamp: Pick<IEtypechamp, 'id'>): number {
    return etypechamp.id;
  }

  compareEtypechamp(o1: Pick<IEtypechamp, 'id'> | null, o2: Pick<IEtypechamp, 'id'> | null): boolean {
    return o1 && o2 ? this.getEtypechampIdentifier(o1) === this.getEtypechampIdentifier(o2) : o1 === o2;
  }

  addEtypechampToCollectionIfMissing<Type extends Pick<IEtypechamp, 'id'>>(
    etypechampCollection: Type[],
    ...etypechampsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const etypechamps: Type[] = etypechampsToCheck.filter(isPresent);
    if (etypechamps.length > 0) {
      const etypechampCollectionIdentifiers = etypechampCollection.map(etypechampItem => this.getEtypechampIdentifier(etypechampItem)!);
      const etypechampsToAdd = etypechamps.filter(etypechampItem => {
        const etypechampIdentifier = this.getEtypechampIdentifier(etypechampItem);
        if (etypechampCollectionIdentifiers.includes(etypechampIdentifier)) {
          return false;
        }
        etypechampCollectionIdentifiers.push(etypechampIdentifier);
        return true;
      });
      return [...etypechampsToAdd, ...etypechampCollection];
    }
    return etypechampCollection;
  }
}
