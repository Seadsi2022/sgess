import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEeventualitevariable } from '../eeventualitevariable.model';
import { EeventualitevariableService } from '../service/eeventualitevariable.service';

@Injectable({ providedIn: 'root' })
export class EeventualitevariableRoutingResolveService implements Resolve<IEeventualitevariable | null> {
  constructor(protected service: EeventualitevariableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEeventualitevariable | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((eeventualitevariable: HttpResponse<IEeventualitevariable>) => {
          if (eeventualitevariable.body) {
            return of(eeventualitevariable.body);
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
