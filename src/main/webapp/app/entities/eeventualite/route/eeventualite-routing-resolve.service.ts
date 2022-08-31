import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEeventualite } from '../eeventualite.model';
import { EeventualiteService } from '../service/eeventualite.service';

@Injectable({ providedIn: 'root' })
export class EeventualiteRoutingResolveService implements Resolve<IEeventualite | null> {
  constructor(protected service: EeventualiteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEeventualite | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((eeventualite: HttpResponse<IEeventualite>) => {
          if (eeventualite.body) {
            return of(eeventualite.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
