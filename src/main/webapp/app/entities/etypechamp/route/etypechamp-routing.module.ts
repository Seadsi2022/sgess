import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EtypechampComponent } from '../list/etypechamp.component';
import { EtypechampDetailComponent } from '../detail/etypechamp-detail.component';
import { EtypechampUpdateComponent } from '../update/etypechamp-update.component';
import { EtypechampRoutingResolveService } from './etypechamp-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const etypechampRoute: Routes = [
  {
    path: '',
    component: EtypechampComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EtypechampDetailComponent,
    resolve: {
      etypechamp: EtypechampRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EtypechampUpdateComponent,
    resolve: {
      etypechamp: EtypechampRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EtypechampUpdateComponent,
    resolve: {
      etypechamp: EtypechampRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(etypechampRoute)],
  exports: [RouterModule],
})
export class EtypechampRoutingModule {}
