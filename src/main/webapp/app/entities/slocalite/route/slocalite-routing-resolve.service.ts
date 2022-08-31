import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISlocalite } from '../slocalite.model';
import { SlocaliteService } from '../service/slocalite.service';

@Injectable({ providedIn: 'root' })
export class SlocaliteRoutingResolveService implements Resolve<ISlocalite | null> {
  constructor(protected service: SlocaliteService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISlocalite | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((slocalite: HttpResponse<ISlocalite>) => {
          if (slocalite.body) {
            return of(slocalite.body);
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
