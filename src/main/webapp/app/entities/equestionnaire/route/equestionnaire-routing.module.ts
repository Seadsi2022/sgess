import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EquestionnaireComponent } from '../list/equestionnaire.component';
import { EquestionnaireDetailComponent } from '../detail/equestionnaire-detail.component';
import { EquestionnaireUpdateComponent } from '../update/equestionnaire-update.component';
import { EquestionnaireRoutingResolveService } from './equestionnaire-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const equestionnaireRoute: Routes = [
  {
    path: '',
    component: EquestionnaireComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EquestionnaireDetailComponent,
    resolve: {
      equestionnaire: EquestionnaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EquestionnaireUpdateComponent,
    resolve: {
      equestionnaire: EquestionnaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EquestionnaireUpdateComponent,
    resolve: {
      equestionnaire: EquestionnaireRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(equestionnaireRoute)],
  exports: [RouterModule],
})
export class EquestionnaireRoutingModule {}
