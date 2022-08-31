import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { EvaleurattributComponent } from '../list/evaleurattribut.component';
import { EvaleurattributDetailComponent } from '../detail/evaleurattribut-detail.component';
import { EvaleurattributUpdateComponent } from '../update/evaleurattribut-update.component';
import { EvaleurattributRoutingResolveService } from './evaleurattribut-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const evaleurattributRoute: Routes = [
  {
    path: '',
    component: EvaleurattributComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: EvaleurattributDetailComponent,
    resolve: {
      evaleurattribut: EvaleurattributRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: EvaleurattributUpdateComponent,
    resolve: {
      evaleurattribut: EvaleurattributRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: EvaleurattributUpdateComponent,
    resolve: {
      evaleurattribut: EvaleurattributRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(evaleurattributRoute)],
  exports: [RouterModule],
})
export class EvaleurattributRoutingModule {}
