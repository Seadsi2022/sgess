import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EgroupevariableComponent } from '../list/egroupevariable.component';
import { EgroupevariableDetailComponent } from '../detail/egroupevariable-detail.component';
import { EgroupevariableUpdateComponent } from '../update/egroupevariable-update.component';
import { EgroupevariableRoutingResolveService } from './egroupevariable-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const egroupevariableRoute: Routes = [
  {
    path: '',
    component: EgroupevariableComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EgroupevariableDetailComponent,
    resolve: {
      egroupevariable: EgroupevariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EgroupevariableUpdateComponent,
    resolve: {
      egroupevariable: EgroupevariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EgroupevariableUpdateComponent,
    resolve: {
      egroupevariable: EgroupevariableRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(egroupevariableRoute)],
  exports: [RouterModule],
})
export class EgroupevariableRoutingModule {}
