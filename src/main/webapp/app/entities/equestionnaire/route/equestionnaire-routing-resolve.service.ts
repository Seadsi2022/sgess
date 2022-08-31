import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IEquestionnaire } from '../equestionnaire.model';
import { EquestionnaireService } from '../service/equestionnaire.service';

@Injectable({ providedIn: 'root' })
export class EquestionnaireRoutingResolveService implements Resolve<IEquestionnaire | null> {
  constructor(protected service: EquestionnaireService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IEquestionnaire | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((equestionnaire: HttpResponse<IEquestionnaire>) => {
          if (equestionnaire.body) {
            return of(equestionnaire.body);
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
