import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEquestionnaire, NewEquestionnaire } from '../equestionnaire.model';

export type PartialUpdateEquestionnaire = Partial<IEquestionnaire> & Pick<IEquestionnaire, 'id'>;

export type EntityResponseType = HttpResponse<IEquestionnaire>;
export type EntityArrayResponseType = HttpResponse<IEquestionnaire[]>;

@Injectable({ providedIn: 'root' })
export class EquestionnaireService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/equestionnaires');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/equestionnaires');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(equestionnaire: NewEquestionnaire): Observable<EntityResponseType> {
    return this.http.post<IEquestionnaire>(this.resourceUrl, equestionnaire, { observe: 'response' });
  }

  update(equestionnaire: IEquestionnaire): Observable<EntityResponseType> {
    return this.http.put<IEquestionnaire>(`${this.resourceUrl}/${this.getEquestionnaireIdentifier(equestionnaire)}`, equestionnaire, {
      observe: 'response',
    });
  }

  partialUpdate(equestionnaire: PartialUpdateEquestionnaire): Observable<EntityResponseType> {
    return this.http.patch<IEquestionnaire>(`${this.resourceUrl}/${this.getEquestionnaireIdentifier(equestionnaire)}`, equestionnaire, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IEquestionnaire>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEquestionnaire[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEquestionnaire[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getEquestionnaireIdentifier(equestionnaire: Pick<IEquestionnaire, 'id'>): number {
    return equestionnaire.id;
  }

  compareEquestionnaire(o1: Pick<IEquestionnaire, 'id'> | null, o2: Pick<IEquestionnaire, 'id'> | null): boolean {
    return o1 && o2 ? this.getEquestionnaireIdentifier(o1) === this.getEquestionnaireIdentifier(o2) : o1 === o2;
  }

  addEquestionnaireToCollectionIfMissing<Type extends Pick<IEquestionnaire, 'id'>>(
    equestionnaireCollection: Type[],
    ...equestionnairesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const equestionnaires: Type[] = equestionnairesToCheck.filter(isPresent);
    if (equestionnaires.length > 0) {
      const equestionnaireCollectionIdentifiers = equestionnaireCollection.map(
        equestionnaireItem => this.getEquestionnaireIdentifier(equestionnaireItem)!
      );
      const equestionnairesToAdd = equestionnaires.filter(equestionnaireItem => {
        const equestionnaireIdentifier = this.getEquestionnaireIdentifier(equestionnaireItem);
        if (equestionnaireCollectionIdentifiers.includes(equestionnaireIdentifier)) {
          return false;
        }
        equestionnaireCollectionIdentifiers.push(equestionnaireIdentifier);
        return true;
      });
      return [...equestionnairesToAdd, ...equestionnaireCollection];
    }
    return equestionnaireCollection;
  }
}
