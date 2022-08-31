import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EeventualiteComponent } from '../list/eeventualite.component';
import { EeventualiteDetailComponent } from '../detail/eeventualite-detail.component';
import { EeventualiteUpdateComponent } from '../update/eeventualite-update.component';
import { EeventualiteRoutingResolveService } from './eeventualite-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const eeventualiteRoute: Routes = [
  {
    path: '',
    component: EeventualiteComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EeventualiteDetailComponent,
    resolve: {
      eeventualite: EeventualiteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EeventualiteUpdateComponent,
    resolve: {
      eeventualite: EeventualiteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EeventualiteUpdateComponent,
    resolve: {
      eeventualite: EeventualiteRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(eeventualiteRoute)],
  exports: [RouterModule],
})
export class EeventualiteRoutingModule {}
