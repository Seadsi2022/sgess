import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SstructureComponent } from '../list/sstructure.component';
import { SstructureDetailComponent } from '../detail/sstructure-detail.component';
import { SstructureUpdateComponent } from '../update/sstructure-update.component';
import { SstructureRoutingResolveService } from './sstructure-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const sstructureRoute: Routes = [
  {
    path: '',
    component: SstructureComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SstructureDetailComponent,
    resolve: {
      sstructure: SstructureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SstructureUpdateComponent,
    resolve: {
      sstructure: SstructureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SstructureUpdateComponent,
    resolve: {
      sstructure: SstructureRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(sstructureRoute)],
  exports: [RouterModule],
})
export class SstructureRoutingModule {}
