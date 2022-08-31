import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEvariable, NewEvariable } from '../evariable.model';

export type PartialUpdateEvariable = Partial<IEvariable> & Pick<IEvariable, 'id'>;

export type EntityResponseType = HttpResponse<IEvariable>;
export type EntityArrayResponseType = HttpResponse<IEvariable[]>;

@Injectable({ providedIn: 'root' })
export class EvariableService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/evariables');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/evariables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(evariable: NewEvariable): Observable<EntityResponseType> {
    return this.http.post<IEvariable>(this.resourceUrl, evariable, { observe: 'response' });
  }

  update(evariable: IEvariable): Observable<EntityResponseType> {
    return this.http.put<IEvariable>(`${this.resourceUrl}/${this.getEvariableIdentifier(evariable)}`, evariable, { observe: 'response' });
  }

  partialUpdate(evariable: PartialUpdateEvariable): Observable<EntityResponseType> {
    return this.http.patch<IEvariable>(`${this.resourceUrl}/${this.getEvariableIdentifier(evariable)}`, evariable, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEvariable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEvariable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEvariable[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getEvariableIdentifier(evariable: Pick<IEvariable, 'id'>): number {
    return evariable.id;
  }

  compareEvariable(o1: Pick<IEvariable, 'id'> | null, o2: Pick<IEvariable, 'id'> | null): boolean {
    return o1 && o2 ? this.getEvariableIdentifier(o1) === this.getEvariableIdentifier(o2) : o1 === o2;
  }

  addEvariableToCollectionIfMissing<Type extends Pick<IEvariable, 'id'>>(
    evariableCollection: Type[],
    ...evariablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const evariables: Type[] = evariablesToCheck.filter(isPresent);
    if (evariables.length > 0) {
      const evariableCollectionIdentifiers = evariableCollection.map(evariableItem => this.getEvariableIdentifier(evariableItem)!);
      const evariablesToAdd = evariables.filter(evariableItem => {
        const evariableIdentifier = this.getEvariableIdentifier(evariableItem);
        if (evariableCollectionIdentifiers.includes(evariableIdentifier)) {
          return false;
        }
        evariableCollectionIdentifiers.push(evariableIdentifier);
        return true;
      });
      return [...evariablesToAdd, ...evariableCollection];
    }
    return evariableCollection;
  }
}
