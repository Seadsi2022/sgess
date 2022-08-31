import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEgroupe } from '../egroupe.model';
import { EgroupeService } from '../service/egroupe.service';

@Injectable({ providedIn: 'root' })
export class EgroupeRoutingResolveService implements Resolve<IEgroupe | null> {
  constructor(protected service: EgroupeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEgroupe | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((egroupe: HttpResponse<IEgroupe>) => {
          if (egroupe.body) {
            return of(egroupe.body);
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
