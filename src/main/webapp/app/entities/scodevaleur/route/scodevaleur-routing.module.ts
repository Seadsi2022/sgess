import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ScodevaleurComponent } from '../list/scodevaleur.component';
import { ScodevaleurDetailComponent } from '../detail/scodevaleur-detail.component';
import { ScodevaleurUpdateComponent } from '../update/scodevaleur-update.component';
import { ScodevaleurRoutingResolveService } from './scodevaleur-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const scodevaleurRoute: Routes = [
  {
    path: '',
    component: ScodevaleurComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ScodevaleurDetailComponent,
    resolve: {
      scodevaleur: ScodevaleurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ScodevaleurUpdateComponent,
    resolve: {
      scodevaleur: ScodevaleurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ScodevaleurUpdateComponent,
    resolve: {
      scodevaleur: ScodevaleurRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(scodevaleurRoute)],
  exports: [RouterModule],
})
export class ScodevaleurRoutingModule {}
