import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EtypevariableComponent } from '../list/etypevariable.component';
import { EtypevariableDetailComponent } from '../detail/etypevariable-detail.component';
import { EtypevariableUpdateComponent } from '../update/etypevariable-update.component';
import { EtypevariableRoutingResolveService } from './etypevariable-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const etypevariableRoute: Routes = [
  {
    path: '',
    component: EtypevariableComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EtypevariableDetailComponent,
    resolve: {
      etypevariable: EtypevariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EtypevariableUpdateComponent,
    resolve: {
      etypevariable: EtypevariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EtypevariableUpdateComponent,
    resolve: {
      etypevariable: EtypevariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(etypevariableRoute)],
  exports: [RouterModule],
})
export class EtypevariableRoutingModule {}
