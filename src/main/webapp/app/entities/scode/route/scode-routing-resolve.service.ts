import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IScode } from '../scode.model';
import { ScodeService } from '../service/scode.service';

@Injectable({ providedIn: 'root' })
export class ScodeRoutingResolveService implements Resolve<IScode | null> {
  constructor(protected service: ScodeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IScode | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((scode: HttpResponse<IScode>) => {
          if (scode.body) {
            return of(scode.body);
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
