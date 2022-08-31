import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EgroupeComponent } from '../list/egroupe.component';
import { EgroupeDetailComponent } from '../detail/egroupe-detail.component';
import { EgroupeUpdateComponent } from '../update/egroupe-update.component';
import { EgroupeRoutingResolveService } from './egroupe-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const egroupeRoute: Routes = [
  {
    path: '',
    component: EgroupeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EgroupeDetailComponent,
    resolve: {
      egroupe: EgroupeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EgroupeUpdateComponent,
    resolve: {
      egroupe: EgroupeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EgroupeUpdateComponent,
    resolve: {
      egroupe: EgroupeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(egroupeRoute)],
  exports: [RouterModule],
})
export class EgroupeRoutingModule {}
