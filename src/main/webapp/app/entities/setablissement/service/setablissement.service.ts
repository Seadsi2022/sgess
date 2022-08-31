import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISetablissement, NewSetablissement } from '../setablissement.model';

export type PartialUpdateSetablissement = Partial<ISetablissement> & Pick<ISetablissement, 'id'>;

export type EntityResponseType = HttpResponse<ISetablissement>;
export type EntityArrayResponseType = HttpResponse<ISetablissement[]>;

@Injectable({ providedIn: 'root' })
export class SetablissementService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/setablissements');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/setablissements');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(setablissement: NewSetablissement): Observable<EntityResponseType> {
    return this.http.post<ISetablissement>(this.resourceUrl, setablissement, { observe: 'response' });
  }

  update(setablissement: ISetablissement): Observable<EntityResponseType> {
    return this.http.put<ISetablissement>(`${this.resourceUrl}/${this.getSetablissementIdentifier(setablissement)}`, setablissement, {
      observe: 'response',
    });
  }

  partialUpdate(setablissement: PartialUpdateSetablissement): Observable<EntityResponseType> {
    return this.http.patch<ISetablissement>(`${this.resourceUrl}/${this.getSetablissementIdentifier(setablissement)}`, setablissement, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISetablissement>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISetablissement[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISetablissement[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getSetablissementIdentifier(setablissement: Pick<ISetablissement, 'id'>): number {
    return setablissement.id;
  }

  compareSetablissement(o1: Pick<ISetablissement, 'id'> | null, o2: Pick<ISetablissement, 'id'> | null): boolean {
    return o1 && o2 ? this.getSetablissementIdentifier(o1) === this.getSetablissementIdentifier(o2) : o1 === o2;
  }

  addSetablissementToCollectionIfMissing<Type extends Pick<ISetablissement, 'id'>>(
    setablissementCollection: Type[],
    ...setablissementsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const setablissements: Type[] = setablissementsToCheck.filter(isPresent);
    if (setablissements.length > 0) {
      const setablissementCollectionIdentifiers = setablissementCollection.map(
        setablissementItem => this.getSetablissementIdentifier(setablissementItem)!
      );
      const setablissementsToAdd = setablissements.filter(setablissementItem => {
        const setablissementIdentifier = this.getSetablissementIdentifier(setablissementItem);
        if (setablissementCollectionIdentifiers.includes(setablissementIdentifier)) {
          return false;
        }
        setablissementCollectionIdentifiers.push(setablissementIdentifier);
        return true;
      });
      return [...setablissementsToAdd, ...setablissementCollection];
    }
    return setablissementCollection;
  }
}
