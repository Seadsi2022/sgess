import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEtypechamp } from '../etypechamp.model';
import { EtypechampService } from '../service/etypechamp.service';

@Injectable({ providedIn: 'root' })
export class EtypechampRoutingResolveService implements Resolve<IEtypechamp | null> {
  constructor(protected service: EtypechampService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEtypechamp | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((etypechamp: HttpResponse<IEtypechamp>) => {
          if (etypechamp.body) {
            return of(etypechamp.body);
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
