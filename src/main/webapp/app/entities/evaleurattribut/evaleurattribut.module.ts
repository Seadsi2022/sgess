import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EvaleurattributComponent } from './list/evaleurattribut.component';
import { EvaleurattributDetailComponent } from './detail/evaleurattribut-detail.component';
import { EvaleurattributUpdateComponent } from './update/evaleurattribut-update.component';
import { EvaleurattributDeleteDialogComponent } from './delete/evaleurattribut-delete-dialog.component';
import { EvaleurattributRoutingModule } from './route/evaleurattribut-routing.module';

@NgModule({
  imports: [SharedModule, EvaleurattributRoutingModule],
  declarations: [
    EvaleurattributComponent,
    EvaleurattributDetailComponent,
    EvaleurattributUpdateComponent,
    EvaleurattributDeleteDialogComponent,
  ],
})
export class EvaleurattributModule {}
