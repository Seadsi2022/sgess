import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEattribut } from '../eattribut.model';
import { EattributService } from '../service/eattribut.service';

@Injectable({ providedIn: 'root' })
export class EattributRoutingResolveService implements Resolve<IEattribut | null> {
  constructor(protected service: EattributService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEattribut | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((eattribut: HttpResponse<IEattribut>) => {
          if (eattribut.body) {
            return of(eattribut.body);
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
