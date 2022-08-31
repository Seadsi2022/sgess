import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IScode, NewScode } from '../scode.model';

export type PartialUpdateScode = Partial<IScode> & Pick<IScode, 'id'>;

export type EntityResponseType = HttpResponse<IScode>;
export type EntityArrayResponseType = HttpResponse<IScode[]>;

@Injectable({ providedIn: 'root' })
export class ScodeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/scodes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/scodes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(scode: NewScode): Observable<EntityResponseType> {
    return this.http.post<IScode>(this.resourceUrl, scode, { observe: 'response' });
  }

  update(scode: IScode): Observable<EntityResponseType> {
    return this.http.put<IScode>(`${this.resourceUrl}/${this.getScodeIdentifier(scode)}`, scode, { observe: 'response' });
  }

  partialUpdate(scode: PartialUpdateScode): Observable<EntityResponseType> {
    return this.http.patch<IScode>(`${this.resourceUrl}/${this.getScodeIdentifier(scode)}`, scode, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IScode>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IScode[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IScode[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getScodeIdentifier(scode: Pick<IScode, 'id'>): number {
    return scode.id;
  }

  compareScode(o1: Pick<IScode, 'id'> | null, o2: Pick<IScode, 'id'> | null): boolean {
    return o1 && o2 ? this.getScodeIdentifier(o1) === this.getScodeIdentifier(o2) : o1 === o2;
  }

  addScodeToCollectionIfMissing<Type extends Pick<IScode, 'id'>>(
    scodeCollection: Type[],
    ...scodesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const scodes: Type[] = scodesToCheck.filter(isPresent);
    if (scodes.length > 0) {
      const scodeCollectionIdentifiers = scodeCollection.map(scodeItem => this.getScodeIdentifier(scodeItem)!);
      const scodesToAdd = scodes.filter(scodeItem => {
        const scodeIdentifier = this.getScodeIdentifier(scodeItem);
        if (scodeCollectionIdentifiers.includes(scodeIdentifier)) {
          return false;
        }
        scodeCollectionIdentifiers.push(scodeIdentifier);
        return true;
      });
      return [...scodesToAdd, ...scodeCollection];
    }
    return scodeCollection;
  }
}
