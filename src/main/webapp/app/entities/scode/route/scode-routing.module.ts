import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ScodeComponent } from '../list/scode.component';
import { ScodeDetailComponent } from '../detail/scode-detail.component';
import { ScodeUpdateComponent } from '../update/scode-update.component';
import { ScodeRoutingResolveService } from './scode-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const scodeRoute: Routes = [
  {
    path: '',
    component: ScodeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ScodeDetailComponent,
    resolve: {
      scode: ScodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ScodeUpdateComponent,
    resolve: {
      scode: ScodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ScodeUpdateComponent,
    resolve: {
      scode: ScodeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(scodeRoute)],
  exports: [RouterModule],
})
export class ScodeRoutingModule {}
