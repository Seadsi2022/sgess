import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEtypevariable } from '../etypevariable.model';
import { EtypevariableService } from '../service/etypevariable.service';

@Injectable({ providedIn: 'root' })
export class EtypevariableRoutingResolveService implements Resolve<IEtypevariable | null> {
  constructor(protected service: EtypevariableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEtypevariable | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((etypevariable: HttpResponse<IEtypevariable>) => {
          if (etypevariable.body) {
            return of(etypevariable.body);
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
