import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEgroupe, NewEgroupe } from '../egroupe.model';

export type PartialUpdateEgroupe = Partial<IEgroupe> & Pick<IEgroupe, 'id'>;

export type EntityResponseType = HttpResponse<IEgroupe>;
export type EntityArrayResponseType = HttpResponse<IEgroupe[]>;

@Injectable({ providedIn: 'root' })
export class EgroupeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/egroupes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/egroupes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(egroupe: NewEgroupe): Observable<EntityResponseType> {
    return this.http.post<IEgroupe>(this.resourceUrl, egroupe, { observe: 'response' });
  }

  update(egroupe: IEgroupe): Observable<EntityResponseType> {
    return this.http.put<IEgroupe>(`${this.resourceUrl}/${this.getEgroupeIdentifier(egroupe)}`, egroupe, { observe: 'response' });
  }

  partialUpdate(egroupe: PartialUpdateEgroupe): Observable<EntityResponseType> {
    return this.http.patch<IEgroupe>(`${this.resourceUrl}/${this.getEgroupeIdentifier(egroupe)}`, egroupe, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEgroupe>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEgroupe[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEgroupe[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getEgroupeIdentifier(egroupe: Pick<IEgroupe, 'id'>): number {
    return egroupe.id;
  }

  compareEgroupe(o1: Pick<IEgroupe, 'id'> | null, o2: Pick<IEgroupe, 'id'> | null): boolean {
    return o1 && o2 ? this.getEgroupeIdentifier(o1) === this.getEgroupeIdentifier(o2) : o1 === o2;
  }

  addEgroupeToCollectionIfMissing<Type extends Pick<IEgroupe, 'id'>>(
    egroupeCollection: Type[],
    ...egroupesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const egroupes: Type[] = egroupesToCheck.filter(isPresent);
    if (egroupes.length > 0) {
      const egroupeCollectionIdentifiers = egroupeCollection.map(egroupeItem => this.getEgroupeIdentifier(egroupeItem)!);
      const egroupesToAdd = egroupes.filter(egroupeItem => {
        const egroupeIdentifier = this.getEgroupeIdentifier(egroupeItem);
        if (egroupeCollectionIdentifiers.includes(egroupeIdentifier)) {
          return false;
        }
        egroupeCollectionIdentifiers.push(egroupeIdentifier);
        return true;
      });
      return [...egroupesToAdd, ...egroupeCollection];
    }
    return egroupeCollection;
  }
}
