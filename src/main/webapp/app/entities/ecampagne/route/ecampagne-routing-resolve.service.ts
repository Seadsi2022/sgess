import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEcampagne } from '../ecampagne.model';
import { EcampagneService } from '../service/ecampagne.service';

@Injectable({ providedIn: 'root' })
export class EcampagneRoutingResolveService implements Resolve<IEcampagne | null> {
  constructor(protected service: EcampagneService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEcampagne | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((ecampagne: HttpResponse<IEcampagne>) => {
          if (ecampagne.body) {
            return of(ecampagne.body);
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
