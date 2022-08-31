import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { EvariableComponent } from './list/evariable.component';
import { EvariableDetailComponent } from './detail/evariable-detail.component';
import { EvariableUpdateComponent } from './update/evariable-update.component';
import { EvariableDeleteDialogComponent } from './delete/evariable-delete-dialog.component';
import { EvariableRoutingModule } from './route/evariable-routing.module';

@NgModule({
  imports: [SharedModule, EvariableRoutingModule],
  declarations: [EvariableComponent, EvariableDetailComponent, EvariableUpdateComponent, EvariableDeleteDialogComponent],
})
export class EvariableModule {}
