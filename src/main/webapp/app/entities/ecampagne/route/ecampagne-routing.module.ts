import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EcampagneComponent } from '../list/ecampagne.component';
import { EcampagneDetailComponent } from '../detail/ecampagne-detail.component';
import { EcampagneUpdateComponent } from '../update/ecampagne-update.component';
import { EcampagneRoutingResolveService } from './ecampagne-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const ecampagneRoute: Routes = [
  {
    path: '',
    component: EcampagneComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EcampagneDetailComponent,
    resolve: {
      ecampagne: EcampagneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EcampagneUpdateComponent,
    resolve: {
      ecampagne: EcampagneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EcampagneUpdateComponent,
    resolve: {
      ecampagne: EcampagneRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ecampagneRoute)],
  exports: [RouterModule],
})
export class EcampagneRoutingModule {}
