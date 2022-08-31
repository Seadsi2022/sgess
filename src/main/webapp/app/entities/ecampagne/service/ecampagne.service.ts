import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IEcampagne, NewEcampagne } from '../ecampagne.model';

export type PartialUpdateEcampagne = Partial<IEcampagne> & Pick<IEcampagne, 'id'>;

type RestOf<T extends IEcampagne | NewEcampagne> = Omit<T, 'debutcampagne' | 'fincampagne' | 'debutreelcamp' | 'finreelcamp'> & {
  debutcampagne?: string | null;
  fincampagne?: string | null;
  debutreelcamp?: string | null;
  finreelcamp?: string | null;
};

export type RestEcampagne = RestOf<IEcampagne>;

export type NewRestEcampagne = RestOf<NewEcampagne>;

export type PartialUpdateRestEcampagne = RestOf<PartialUpdateEcampagne>;

export type EntityResponseType = HttpResponse<IEcampagne>;
export type EntityArrayResponseType = HttpResponse<IEcampagne[]>;

@Injectable({ providedIn: 'root' })
export class EcampagneService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/ecampagnes');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/ecampagnes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(ecampagne: NewEcampagne): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ecampagne);
    return this.http
      .post<RestEcampagne>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(ecampagne: IEcampagne): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ecampagne);
    return this.http
      .put<RestEcampagne>(`${this.resourceUrl}/${this.getEcampagneIdentifier(ecampagne)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(ecampagne: PartialUpdateEcampagne): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(ecampagne);
    return this.http
      .patch<RestEcampagne>(`${this.resourceUrl}/${this.getEcampagneIdentifier(ecampagne)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestEcampagne>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEcampagne[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestEcampagne[]>(this.resourceSearchUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getEcampagneIdentifier(ecampagne: Pick<IEcampagne, 'id'>): number {
    return ecampagne.id;
  }

  compareEcampagne(o1: Pick<IEcampagne, 'id'> | null, o2: Pick<IEcampagne, 'id'> | null): boolean {
    return o1 && o2 ? this.getEcampagneIdentifier(o1) === this.getEcampagneIdentifier(o2) : o1 === o2;
  }

  addEcampagneToCollectionIfMissing<Type extends Pick<IEcampagne, 'id'>>(
    ecampagneCollection: Type[],
    ...ecampagnesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const ecampagnes: Type[] = ecampagnesToCheck.filter(isPresent);
    if (ecampagnes.length > 0) {
      const ecampagneCollectionIdentifiers = ecampagneCollection.map(ecampagneItem => this.getEcampagneIdentifier(ecampagneItem)!);
      const ecampagnesToAdd = ecampagnes.filter(ecampagneItem => {
        const ecampagneIdentifier = this.getEcampagneIdentifier(ecampagneItem);
        if (ecampagneCollectionIdentifiers.includes(ecampagneIdentifier)) {
          return false;
        }
        ecampagneCollectionIdentifiers.push(ecampagneIdentifier);
        return true;
      });
      return [...ecampagnesToAdd, ...ecampagneCollection];
    }
    return ecampagneCollection;
  }

  protected convertDateFromClient<T extends IEcampagne | NewEcampagne | PartialUpdateEcampagne>(ecampagne: T): RestOf<T> {
    return {
      ...ecampagne,
      debutcampagne: ecampagne.debutcampagne?.toJSON() ?? null,
      fincampagne: ecampagne.fincampagne?.toJSON() ?? null,
      debutreelcamp: ecampagne.debutreelcamp?.toJSON() ?? null,
      finreelcamp: ecampagne.finreelcamp?.toJSON() ?? null,
    };
  }

  protected convertDateFromServer(restEcampagne: RestEcampagne): IEcampagne {
    return {
      ...restEcampagne,
      debutcampagne: restEcampagne.debutcampagne ? dayjs(restEcampagne.debutcampagne) : undefined,
      fincampagne: restEcampagne.fincampagne ? dayjs(restEcampagne.fincampagne) : undefined,
      debutreelcamp: restEcampagne.debutreelcamp ? dayjs(restEcampagne.debutreelcamp) : undefined,
      finreelcamp: restEcampagne.finreelcamp ? dayjs(restEcampagne.finreelcamp) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestEcampagne>): HttpResponse<IEcampagne> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestEcampagne[]>): HttpResponse<IEcampagne[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
