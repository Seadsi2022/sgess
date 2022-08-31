import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEvaleurvariable, NewEvaleurvariable } from '../evaleurvariable.model';

export type PartialUpdateEvaleurvariable = Partial<IEvaleurvariable> & Pick<IEvaleurvariable, 'id'>;

export type EntityResponseType = HttpResponse<IEvaleurvariable>;
export type EntityArrayResponseType = HttpResponse<IEvaleurvariable[]>;

@Injectable({ providedIn: 'root' })
export class EvaleurvariableService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/evaleurvariables');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/evaleurvariables');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(evaleurvariable: NewEvaleurvariable): Observable<EntityResponseType> {
    return this.http.post<IEvaleurvariable>(this.resourceUrl, evaleurvariable, { observe: 'response' });
  }

  update(evaleurvariable: IEvaleurvariable): Observable<EntityResponseType> {
    return this.http.put<IEvaleurvariable>(`${this.resourceUrl}/${this.getEvaleurvariableIdentifier(evaleurvariable)}`, evaleurvariable, {
      observe: 'response',
    });
  }

  partialUpdate(evaleurvariable: PartialUpdateEvaleurvariable): Observable<EntityResponseType> {
    return this.http.patch<IEvaleurvariable>(`${this.resourceUrl}/${this.getEvaleurvariableIdentifier(evaleurvariable)}`, evaleurvariable, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEvaleurvariable>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEvaleurvariable[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEvaleurvariable[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getEvaleurvariableIdentifier(evaleurvariable: Pick<IEvaleurvariable, 'id'>): number {
    return evaleurvariable.id;
  }

  compareEvaleurvariable(o1: Pick<IEvaleurvariable, 'id'> | null, o2: Pick<IEvaleurvariable, 'id'> | null): boolean {
    return o1 && o2 ? this.getEvaleurvariableIdentifier(o1) === this.getEvaleurvariableIdentifier(o2) : o1 === o2;
  }

  addEvaleurvariableToCollectionIfMissing<Type extends Pick<IEvaleurvariable, 'id'>>(
    evaleurvariableCollection: Type[],
    ...evaleurvariablesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const evaleurvariables: Type[] = evaleurvariablesToCheck.filter(isPresent);
    if (evaleurvariables.length > 0) {
      const evaleurvariableCollectionIdentifiers = evaleurvariableCollection.map(
        evaleurvariableItem => this.getEvaleurvariableIdentifier(evaleurvariableItem)!
      );
      const evaleurvariablesToAdd = evaleurvariables.filter(evaleurvariableItem => {
        const evaleurvariableIdentifier = this.getEvaleurvariableIdentifier(evaleurvariableItem);
        if (evaleurvariableCollectionIdentifiers.includes(evaleurvariableIdentifier)) {
          return false;
        }
        evaleurvariableCollectionIdentifiers.push(evaleurvariableIdentifier);
        return true;
      });
      return [...evaleurvariablesToAdd, ...evaleurvariableCollection];
    }
    return evaleurvariableCollection;
  }
}
