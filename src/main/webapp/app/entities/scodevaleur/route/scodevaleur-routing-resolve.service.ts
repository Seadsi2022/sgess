import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IScodevaleur } from '../scodevaleur.model';
import { ScodevaleurService } from '../service/scodevaleur.service';

@Injectable({ providedIn: 'root' })
export class ScodevaleurRoutingResolveService implements Resolve<IScodevaleur | null> {
  constructor(protected service: ScodevaleurService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IScodevaleur | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((scodevaleur: HttpResponse<IScodevaleur>) => {
          if (scodevaleur.body) {
            return of(scodevaleur.body);
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
