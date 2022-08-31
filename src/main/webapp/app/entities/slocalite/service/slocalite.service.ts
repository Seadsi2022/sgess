import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { ISlocalite, NewSlocalite } from '../slocalite.model';

export type PartialUpdateSlocalite = Partial<ISlocalite> & Pick<ISlocalite, 'id'>;

export type EntityResponseType = HttpResponse<ISlocalite>;
export type EntityArrayResponseType = HttpResponse<ISlocalite[]>;

@Injectable({ providedIn: 'root' })
export class SlocaliteService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/slocalites');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/slocalites');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(slocalite: NewSlocalite): Observable<EntityResponseType> {
    return this.http.post<ISlocalite>(this.resourceUrl, slocalite, { observe: 'response' });
  }

  update(slocalite: ISlocalite): Observable<EntityResponseType> {
    return this.http.put<ISlocalite>(`${this.resourceUrl}/${this.getSlocaliteIdentifier(slocalite)}`, slocalite, { observe: 'response' });
  }

  partialUpdate(slocalite: PartialUpdateSlocalite): Observable<EntityResponseType> {
    return this.http.patch<ISlocalite>(`${this.resourceUrl}/${this.getSlocaliteIdentifier(slocalite)}`, slocalite, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISlocalite>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISlocalite[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISlocalite[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getSlocaliteIdentifier(slocalite: Pick<ISlocalite, 'id'>): number {
    return slocalite.id;
  }

  compareSlocalite(o1: Pick<ISlocalite, 'id'> | null, o2: Pick<ISlocalite, 'id'> | null): boolean {
    return o1 && o2 ? this.getSlocaliteIdentifier(o1) === this.getSlocaliteIdentifier(o2) : o1 === o2;
  }

  addSlocaliteToCollectionIfMissing<Type extends Pick<ISlocalite, 'id'>>(
    slocaliteCollection: Type[],
    ...slocalitesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const slocalites: Type[] = slocalitesToCheck.filter(isPresent);
    if (slocalites.length > 0) {
      const slocaliteCollectionIdentifiers = slocaliteCollection.map(slocaliteItem => this.getSlocaliteIdentifier(slocaliteItem)!);
      const slocalitesToAdd = slocalites.filter(slocaliteItem => {
        const slocaliteIdentifier = this.getSlocaliteIdentifier(slocaliteItem);
        if (slocaliteCollectionIdentifiers.includes(slocaliteIdentifier)) {
          return false;
        }
        slocaliteCollectionIdentifiers.push(slocaliteIdentifier);
        return true;
      });
      return [...slocalitesToAdd, ...slocaliteCollection];
    }
    return slocaliteCollection;
  }
}
