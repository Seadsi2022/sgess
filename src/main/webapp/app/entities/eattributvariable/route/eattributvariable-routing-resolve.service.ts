import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEattributvariable } from '../eattributvariable.model';
import { EattributvariableService } from '../service/eattributvariable.service';

@Injectable({ providedIn: 'root' })
export class EattributvariableRoutingResolveService implements Resolve<IEattributvariable | null> {
  constructor(protected service: EattributvariableService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEattributvariable | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((eattributvariable: HttpResponse<IEattributvariable>) => {
          if (eattributvariable.body) {
            return of(eattributvariable.body);
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
