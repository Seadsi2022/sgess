import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEgroupevariable } from '../egroupevariable.model';
import { EgroupevariableService } from '../service/egroupevariable.service';

@Injectable({ providedIn: 'root' })
export class EgroupevariableRoutingResolveService implements Resolve<IEgroupevariable | null> {
  constructor(protected service: EgroupevariableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEgroupevariable | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((egroupevariable: HttpResponse<IEgroupevariable>) => {
          if (egroupevariable.body) {
            return of(egroupevariable.body);
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
