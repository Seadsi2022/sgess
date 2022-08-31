import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SetablissementComponent } from '../list/setablissement.component';
import { SetablissementDetailComponent } from '../detail/setablissement-detail.component';
import { SetablissementUpdateComponent } from '../update/setablissement-update.component';
import { SetablissementRoutingResolveService } from './setablissement-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const setablissementRoute: Routes = [
  {
    path: '',
    component: SetablissementComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SetablissementDetailComponent,
    resolve: {
      setablissement: SetablissementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SetablissementUpdateComponent,
    resolve: {
      setablissement: SetablissementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SetablissementUpdateComponent,
    resolve: {
      setablissement: SetablissementRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(setablissementRoute)],
  exports: [RouterModule],
})
export class SetablissementRoutingModule {}
