import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISstructure, NewSstructure } from '../sstructure.model';

export type PartialUpdateSstructure = Partial<ISstructure> & Pick<ISstructure, 'id'>;

export type EntityResponseType = HttpResponse<ISstructure>;
export type EntityArrayResponseType = HttpResponse<ISstructure[]>;

@Injectable({ providedIn: 'root' })
export class SstructureService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/sstructures');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/sstructures');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(sstructure: NewSstructure): Observable<EntityResponseType> {
    return this.http.post<ISstructure>(this.resourceUrl, sstructure, { observe: 'response' });
  }

  update(sstructure: ISstructure): Observable<EntityResponseType> {
    return this.http.put<ISstructure>(`${this.resourceUrl}/${this.getSstructureIdentifier(sstructure)}`, sstructure, {
      observe: 'response',
    });
  }

  partialUpdate(sstructure: PartialUpdateSstructure): Observable<EntityResponseType> {
    return this.http.patch<ISstructure>(`${this.resourceUrl}/${this.getSstructureIdentifier(sstructure)}`, sstructure, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISstructure>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISstructure[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISstructure[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getSstructureIdentifier(sstructure: Pick<ISstructure, 'id'>): number {
    return sstructure.id;
  }

  compareSstructure(o1: Pick<ISstructure, 'id'> | null, o2: Pick<ISstructure, 'id'> | null): boolean {
    return o1 && o2 ? this.getSstructureIdentifier(o1) === this.getSstructureIdentifier(o2) : o1 === o2;
  }

  addSstructureToCollectionIfMissing<Type extends Pick<ISstructure, 'id'>>(
    sstructureCollection: Type[],
    ...sstructuresToCheck: (Type | null | undefined)[]
  ): Type[] {
    const sstructures: Type[] = sstructuresToCheck.filter(isPresent);
    if (sstructures.length > 0) {
      const sstructureCollectionIdentifiers = sstructureCollection.map(sstructureItem => this.getSstructureIdentifier(sstructureItem)!);
      const sstructuresToAdd = sstructures.filter(sstructureItem => {
        const sstructureIdentifier = this.getSstructureIdentifier(sstructureItem);
        if (sstructureCollectionIdentifiers.includes(sstructureIdentifier)) {
          return false;
        }
        sstructureCollectionIdentifiers.push(sstructureIdentifier);
        return true;
      });
      return [...sstructuresToAdd, ...sstructureCollection];
    }
    return sstructureCollection;
  }
}
