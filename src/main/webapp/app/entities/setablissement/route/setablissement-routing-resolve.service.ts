import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISetablissement } from '../setablissement.model';
import { SetablissementService } from '../service/setablissement.service';

@Injectable({ providedIn: 'root' })
export class SetablissementRoutingResolveService implements Resolve<ISetablissement | null> {
  constructor(protected service: SetablissementService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISetablissement | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((setablissement: HttpResponse<ISetablissement>) => {
          if (setablissement.body) {
            return of(setablissement.body);
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
