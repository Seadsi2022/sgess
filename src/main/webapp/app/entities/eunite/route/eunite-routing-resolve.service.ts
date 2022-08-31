import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEunite } from '../eunite.model';
import { EuniteService } from '../service/eunite.service';

@Injectable({ providedIn: 'root' })
export class EuniteRoutingResolveService implements Resolve<IEunite | null> {
  constructor(protected service: EuniteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEunite | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((eunite: HttpResponse<IEunite>) => {
          if (eunite.body) {
            return of(eunite.body);
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
