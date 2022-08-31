import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EuniteComponent } from '../list/eunite.component';
import { EuniteDetailComponent } from '../detail/eunite-detail.component';
import { EuniteUpdateComponent } from '../update/eunite-update.component';
import { EuniteRoutingResolveService } from './eunite-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const euniteRoute: Routes = [
  {
    path: '',
    component: EuniteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EuniteDetailComponent,
    resolve: {
      eunite: EuniteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EuniteUpdateComponent,
    resolve: {
      eunite: EuniteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EuniteUpdateComponent,
    resolve: {
      eunite: EuniteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(euniteRoute)],
  exports: [RouterModule],
})
export class EuniteRoutingModule {}
