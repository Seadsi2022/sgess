import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEvaleurattribut } from '../evaleurattribut.model';
import { EvaleurattributService } from '../service/evaleurattribut.service';

@Injectable({ providedIn: 'root' })
export class EvaleurattributRoutingResolveService implements Resolve<IEvaleurattribut | null> {
  constructor(protected service: EvaleurattributService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEvaleurattribut | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((evaleurattribut: HttpResponse<IEvaleurattribut>) => {
          if (evaleurattribut.body) {
            return of(evaleurattribut.body);
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
