import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { SearchWithPagination } from 'app/core/request/request.model';
import { IScodevaleur, NewScodevaleur } from '../scodevaleur.model';

export type PartialUpdateScodevaleur = Partial<IScodevaleur> & Pick<IScodevaleur, 'id'>;

export type EntityResponseType = HttpResponse<IScodevaleur>;
export type EntityArrayResponseType = HttpResponse<IScodevaleur[]>;

@Injectable({ providedIn: 'root' })
export class ScodevaleurService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/scodevaleurs');
  protected resourceSearchUrl = this.applicationConfigService.getEndpointFor('api/_search/scodevaleurs');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(scodevaleur: NewScodevaleur): Observable<EntityResponseType> {
    return this.http.post<IScodevaleur>(this.resourceUrl, scodevaleur, { observe: 'response' });
  }

  update(scodevaleur: IScodevaleur): Observable<EntityResponseType> {
    return this.http.put<IScodevaleur>(`${this.resourceUrl}/${this.getScodevaleurIdentifier(scodevaleur)}`, scodevaleur, {
      observe: 'response',
    });
  }

  partialUpdate(scodevaleur: PartialUpdateScodevaleur): Observable<EntityResponseType> {
    return this.http.patch<IScodevaleur>(`${this.resourceUrl}/${this.getScodevaleurIdentifier(scodevaleur)}`, scodevaleur, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IScodevaleur>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IScodevaleur[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  search(req: SearchWithPagination): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IScodevaleur[]>(this.resourceSearchUrl, { params: options, observe: 'response' });
  }

  getScodevaleurIdentifier(scodevaleur: Pick<IScodevaleur, 'id'>): number {
    return scodevaleur.id;
  }

  compareScodevaleur(o1: Pick<IScodevaleur, 'id'> | null, o2: Pick<IScodevaleur, 'id'> | null): boolean {
    return o1 && o2 ? this.getScodevaleurIdentifier(o1) === this.getScodevaleurIdentifier(o2) : o1 === o2;
  }

  addScodevaleurToCollectionIfMissing<Type extends Pick<IScodevaleur, 'id'>>(
    scodevaleurCollection: Type[],
    ...scodevaleursToCheck: (Type | null | undefined)[]
  ): Type[] {
    const scodevaleurs: Type[] = scodevaleursToCheck.filter(isPresent);
    if (scodevaleurs.length > 0) {
      const scodevaleurCollectionIdentifiers = scodevaleurCollection.map(
        scodevaleurItem => this.getScodevaleurIdentifier(scodevaleurItem)!
      );
      const scodevaleursToAdd = scodevaleurs.filter(scodevaleurItem => {
        const scodevaleurIdentifier = this.getScodevaleurIdentifier(scodevaleurItem);
        if (scodevaleurCollectionIdentifiers.includes(scodevaleurIdentifier)) {
          return false;
        }
        scodevaleurCollectionIdentifiers.push(scodevaleurIdentifier);
        return true;
      });
      return [...scodevaleursToAdd, ...scodevaleurCollection];
    }
    return scodevaleurCollection;
  }
}
