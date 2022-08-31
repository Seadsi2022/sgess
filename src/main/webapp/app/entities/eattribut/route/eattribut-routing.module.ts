import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EattributComponent } from '../list/eattribut.component';
import { EattributDetailComponent } from '../detail/eattribut-detail.component';
import { EattributUpdateComponent } from '../update/eattribut-update.component';
import { EattributRoutingResolveService } from './eattribut-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const eattributRoute: Routes = [
  {
    path: '',
    component: EattributComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EattributDetailComponent,
    resolve: {
      eattribut: EattributRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EattributUpdateComponent,
    resolve: {
      eattribut: EattributRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EattributUpdateComponent,
    resolve: {
      eattribut: EattributRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(eattributRoute)],
  exports: [RouterModule],
})
export class EattributRoutingModule {}
