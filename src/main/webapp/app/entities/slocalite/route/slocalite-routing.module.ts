import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SlocaliteComponent } from '../list/slocalite.component';
import { SlocaliteDetailComponent } from '../detail/slocalite-detail.component';
import { SlocaliteUpdateComponent } from '../update/slocalite-update.component';
import { SlocaliteRoutingResolveService } from './slocalite-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const slocaliteRoute: Routes = [
  {
    path: '',
    component: SlocaliteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SlocaliteDetailComponent,
    resolve: {
      slocalite: SlocaliteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SlocaliteUpdateComponent,
    resolve: {
      slocalite: SlocaliteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SlocaliteUpdateComponent,
    resolve: {
      slocalite: SlocaliteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(slocaliteRoute)],
  exports: [RouterModule],
})
export class SlocaliteRoutingModule {}
