import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EeventualitevariableComponent } from '../list/eeventualitevariable.component';
import { EeventualitevariableDetailComponent } from '../detail/eeventualitevariable-detail.component';
import { EeventualitevariableUpdateComponent } from '../update/eeventualitevariable-update.component';
import { EeventualitevariableRoutingResolveService } from './eeventualitevariable-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const eeventualitevariableRoute: Routes = [
  {
    path: '',
    component: EeventualitevariableComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EeventualitevariableDetailComponent,
    resolve: {
      eeventualitevariable: EeventualitevariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EeventualitevariableUpdateComponent,
    resolve: {
      eeventualitevariable: EeventualitevariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EeventualitevariableUpdateComponent,
    resolve: {
      eeventualitevariable: EeventualitevariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(eeventualitevariableRoute)],
  exports: [RouterModule],
})
export class EeventualitevariableRoutingModule {}
