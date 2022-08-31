import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EvaleurvariableComponent } from '../list/evaleurvariable.component';
import { EvaleurvariableDetailComponent } from '../detail/evaleurvariable-detail.component';
import { EvaleurvariableUpdateComponent } from '../update/evaleurvariable-update.component';
import { EvaleurvariableRoutingResolveService } from './evaleurvariable-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const evaleurvariableRoute: Routes = [
  {
    path: '',
    component: EvaleurvariableComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EvaleurvariableDetailComponent,
    resolve: {
      evaleurvariable: EvaleurvariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EvaleurvariableUpdateComponent,
    resolve: {
      evaleurvariable: EvaleurvariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EvaleurvariableUpdateComponent,
    resolve: {
      evaleurvariable: EvaleurvariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(evaleurvariableRoute)],
  exports: [RouterModule],
})
export class EvaleurvariableRoutingModule {}
