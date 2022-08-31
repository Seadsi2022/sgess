import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EeventualitevariableComponent } from './list/eeventualitevariable.component';
import { EeventualitevariableDetailComponent } from './detail/eeventualitevariable-detail.component';
import { EeventualitevariableUpdateComponent } from './update/eeventualitevariable-update.component';
import { EeventualitevariableDeleteDialogComponent } from './delete/eeventualitevariable-delete-dialog.component';
import { EeventualitevariableRoutingModule } from './route/eeventualitevariable-routing.module';

@NgModule({
  imports: [SharedModule, EeventualitevariableRoutingModule],
  declarations: [
    EeventualitevariableComponent,
    EeventualitevariableDetailComponent,
    EeventualitevariableUpdateComponent,
    EeventualitevariableDeleteDialogComponent,
  ],
})
export class EeventualitevariableModule {}
