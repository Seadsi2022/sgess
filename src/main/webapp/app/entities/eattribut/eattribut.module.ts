import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EattributComponent } from './list/eattribut.component';
import { EattributDetailComponent } from './detail/eattribut-detail.component';
import { EattributUpdateComponent } from './update/eattribut-update.component';
import { EattributDeleteDialogComponent } from './delete/eattribut-delete-dialog.component';
import { EattributRoutingModule } from './route/eattribut-routing.module';

@NgModule({
  imports: [SharedModule, EattributRoutingModule],
  declarations: [EattributComponent, EattributDetailComponent, EattributUpdateComponent, EattributDeleteDialogComponent],
})
export class EattributModule {}
