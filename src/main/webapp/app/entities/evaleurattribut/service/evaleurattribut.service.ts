import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEvaleurattribut, NewEvaleurattribut } from '../evaleurattribut.model';

export type PartialUpdateEvaleurattribut = Partial<IEvaleurattribut> & Pick<IEvaleurattribut, 'id'>;

export type EntityResponseType = HttpResponse<IEvaleurattribut>;
export type EntityArrayResponseType = HttpResponse<IEvaleurattribut[]>;

@Injectable({ providedIn: 'root' })
export class EvaleurattributService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/evaleurattributs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/evaleurattributs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(evaleurattribut: NewEvaleurattribut): Observable<EntityResponseType> {
    return this.http.post<IEvaleurattribut>(this.resourceUrl, evaleurattribut, { observe: 'response' });
  }

  update(evaleurattribut: IEvaleurattribut): Observable<EntityResponseType> {
    return this.http.put<IEvaleurattribut>(`${this.resourceUrl}/${this.getEvaleurattributIdentifier(evaleurattribut)}`, evaleurattribut, {
      observe: 'response',
    });
  }

  partialUpdate(evaleurattribut: PartialUpdateEvaleurattribut): Observable<EntityResponseType> {
    return this.http.patch<IEvaleurattribut>(`${this.resourceUrl}/${this.getEvaleurattributIdentifier(evaleurattribut)}`, evaleurattribut, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEvaleurattribut>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEvaleurattribut[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEvaleurattribut[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getEvaleurattributIdentifier(evaleurattribut: Pick<IEvaleurattribut, 'id'>): number {
    return evaleurattribut.id;
  }

  compareEvaleurattribut(o1: Pick<IEvaleurattribut, 'id'> | null, o2: Pick<IEvaleurattribut, 'id'> | null): boolean {
    return o1 && o2 ? this.getEvaleurattributIdentifier(o1) === this.getEvaleurattributIdentifier(o2) : o1 === o2;
  }

  addEvaleurattributToCollectionIfMissing<Type extends Pick<IEvaleurattribut, 'id'>>(
    evaleurattributCollection: Type[],
    ...evaleurattributsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const evaleurattributs: Type[] = evaleurattributsToCheck.filter(isPresent);
    if (evaleurattributs.length > 0) {
      const evaleurattributCollectionIdentifiers = evaleurattributCollection.map(
        evaleurattributItem => this.getEvaleurattributIdentifier(evaleurattributItem)!
      );
      const evaleurattributsToAdd = evaleurattributs.filter(evaleurattributItem => {
        const evaleurattributIdentifier = this.getEvaleurattributIdentifier(evaleurattributItem);
        if (evaleurattributCollectionIdentifiers.includes(evaleurattributIdentifier)) {
          return false;
        }
        evaleurattributCollectionIdentifiers.push(evaleurattributIdentifier);
        return true;
      });
      return [...evaleurattributsToAdd, ...evaleurattributCollection];
    }
    return evaleurattributCollection;
  }
}
