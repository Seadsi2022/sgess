import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISstructure } from '../sstructure.model';
import { SstructureService } from '../service/sstructure.service';

@Injectable({ providedIn: 'root' })
export class SstructureRoutingResolveService implements Resolve<ISstructure | null> {
  constructor(protected service: SstructureService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISstructure | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((sstructure: HttpResponse<ISstructure>) => {
          if (sstructure.body) {
            return of(sstructure.body);
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
