import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEvariable } from '../evariable.model';
import { EvariableService } from '../service/evariable.service';

@Injectable({ providedIn: 'root' })
export class EvariableRoutingResolveService implements Resolve<IEvariable | null> {
  constructor(protected service: EvariableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEvariable | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((evariable: HttpResponse<IEvariable>) => {
          if (evariable.body) {
            return of(evariable.body);
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
