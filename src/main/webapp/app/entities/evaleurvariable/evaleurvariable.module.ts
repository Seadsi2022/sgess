import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EvaleurvariableComponent } from './list/evaleurvariable.component';
import { EvaleurvariableDetailComponent } from './detail/evaleurvariable-detail.component';
import { EvaleurvariableUpdateComponent } from './update/evaleurvariable-update.component';
import { EvaleurvariableDeleteDialogComponent } from './delete/evaleurvariable-delete-dialog.component';
import { EvaleurvariableRoutingModule } from './route/evaleurvariable-routing.module';

@NgModule({
  imports: [SharedModule, EvaleurvariableRoutingModule],
  declarations: [
    EvaleurvariableComponent,
    EvaleurvariableDetailComponent,
    EvaleurvariableUpdateComponent,
    EvaleurvariableDeleteDialogComponent,
  ],
})
export class EvaleurvariableModule {}
