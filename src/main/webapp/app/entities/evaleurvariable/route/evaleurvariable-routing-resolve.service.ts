import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEvaleurvariable } from '../evaleurvariable.model';
import { EvaleurvariableService } from '../service/evaleurvariable.service';

@Injectable({ providedIn: 'root' })
export class EvaleurvariableRoutingResolveService implements Resolve<IEvaleurvariable | null> {
  constructor(protected service: EvaleurvariableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEvaleurvariable | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((evaleurvariable: HttpResponse<IEvaleurvariable>) => {
          if (evaleurvariable.body) {
            return of(evaleurvariable.body);
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
