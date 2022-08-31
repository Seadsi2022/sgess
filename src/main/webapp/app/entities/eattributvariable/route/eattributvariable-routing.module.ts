import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EattributvariableComponent } from '../list/eattributvariable.component';
import { EattributvariableDetailComponent } from '../detail/eattributvariable-detail.component';
import { EattributvariableUpdateComponent } from '../update/eattributvariable-update.component';
import { EattributvariableRoutingResolveService } from './eattributvariable-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const eattributvariableRoute: Routes = [
  {
    path: '',
    component: EattributvariableComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EattributvariableDetailComponent,
    resolve: {
      eattributvariable: EattributvariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EattributvariableUpdateComponent,
    resolve: {
      eattributvariable: EattributvariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EattributvariableUpdateComponent,
    resolve: {
      eattributvariable: EattributvariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(eattributvariableRoute)],
  exports: [RouterModule],
})
export class EattributvariableRoutingModule {}
