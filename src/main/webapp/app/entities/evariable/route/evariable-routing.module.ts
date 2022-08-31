import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EvariableComponent } from '../list/evariable.component';
import { EvariableDetailComponent } from '../detail/evariable-detail.component';
import { EvariableUpdateComponent } from '../update/evariable-update.component';
import { EvariableRoutingResolveService } from './evariable-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const evariableRoute: Routes = [
  {
    path: '',
    component: EvariableComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EvariableDetailComponent,
    resolve: {
      evariable: EvariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EvariableUpdateComponent,
    resolve: {
      evariable: EvariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EvariableUpdateComponent,
    resolve: {
      evariable: EvariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(evariableRoute)],
  exports: [RouterModule],
})
export class EvariableRoutingModule {}
