import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEgroupevariable, NewEgroupevariable } from '../egroupevariable.model';

export type PartialUpdateEgroupevariable = Partial<IEgroupevariable> & Pick<IEgroupevariable, 'id'>;

export type EntityResponseType = HttpResponse<IEgroupevariable>;
export type EntityArrayResponseType = HttpResponse<IEgroupevariable[]>;

@Injectable({ providedIn: 'root' })
export class EgroupevariableService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/egroupevariables');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/egroupevariables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(egroupevariable: NewEgroupevariable): Observable<EntityResponseType> {
    return this.http.post<IEgroupevariable>(this.resourceUrl, egroupevariable, { observe: 'response' });
  }

  update(egroupevariable: IEgroupevariable): Observable<EntityResponseType> {
    return this.http.put<IEgroupevariable>(`${this.resourceUrl}/${this.getEgroupevariableIdentifier(egroupevariable)}`, egroupevariable, {
      observe: 'response',
    });
  }

  partialUpdate(egroupevariable: PartialUpdateEgroupevariable): Observable<EntityResponseType> {
    return this.http.patch<IEgroupevariable>(`${this.resourceUrl}/${this.getEgroupevariableIdentifier(egroupevariable)}`, egroupevariable, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEgroupevariable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEgroupevariable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEgroupevariable[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getEgroupevariableIdentifier(egroupevariable: Pick<IEgroupevariable, 'id'>): number {
    return egroupevariable.id;
  }

  compareEgroupevariable(o1: Pick<IEgroupevariable, 'id'> | null, o2: Pick<IEgroupevariable, 'id'> | null): boolean {
    return o1 && o2 ? this.getEgroupevariableIdentifier(o1) === this.getEgroupevariableIdentifier(o2) : o1 === o2;
  }

  addEgroupevariableToCollectionIfMissing<Type extends Pick<IEgroupevariable, 'id'>>(
    egroupevariableCollection: Type[],
    ...egroupevariablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const egroupevariables: Type[] = egroupevariablesToCheck.filter(isPresent);
    if (egroupevariables.length > 0) {
      const egroupevariableCollectionIdentifiers = egroupevariableCollection.map(
        egroupevariableItem => this.getEgroupevariableIdentifier(egroupevariableItem)!
      );
      const egroupevariablesToAdd = egroupevariables.filter(egroupevariableItem => {
        const egroupevariableIdentifier = this.getEgroupevariableIdentifier(egroupevariableItem);
        if (egroupevariableCollectionIdentifiers.includes(egroupevariableIdentifier)) {
          return false;
        }
        egroupevariableCollectionIdentifiers.push(egroupevariableIdentifier);
        return true;
      });
      return [...egroupevariablesToAdd, ...egroupevariableCollection];
    }
    return egroupevariableCollection;
  }
}
